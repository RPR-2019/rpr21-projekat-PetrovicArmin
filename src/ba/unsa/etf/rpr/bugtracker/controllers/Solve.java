package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
import ba.unsa.etf.rpr.bugtracker.models.Bug;
import ba.unsa.etf.rpr.bugtracker.models.Solution;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Solve extends AbstractController implements Initializable, Showable {
    private Details parent;
    private Database database;
    private ResourceBundle resourceBundle;
    private User currentUser;
    private Bug currentBug;
    private String resourcesPath = Paths.get(System.getProperty("user.home"), "resources").toString();
    public TextArea areaDescription;
    public HTMLEditor areaCode;
    public Button btnOk;
    public Button btnCancel;
    public Button btnImage;
    public Label lblImage;
    private File selectedFile;


    public Solve(Details parent, User currentUser, Bug currentBug) {
        this.parent = parent;
        this.database = Database.getInstance();
        this.currentUser = currentUser;
        this.currentBug = currentBug;
    }


    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        areaDescription.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() < 10)
                changeBackgroundColor(areaDescription, "error");
            else
                changeBackgroundColor(areaDescription, "success");
        });
    }

    private static <T extends TextInputControl> void changeBackgroundColor(T field, String status) {
        //error - adding red color
        //success - adding green color
        //halt, clear, etc. - removing all added attributes without any additions
        field.getStyleClass().removeAll("errorColor");
        field.getStyleClass().removeAll("successColor");

        String classForRemoval = "";
        String classForAddition = "";

        switch(status) {
            case "error":
                classForRemoval = "successColor";
                classForAddition = "errorColor";
                break;
            case "success":
                classForRemoval = "errorColor";
                classForAddition = "successColor";
                break;
            default:
                return;
        }

        field.getStyleClass().removeAll(classForRemoval);
        field.getStyleClass().add(classForAddition);
    }

    private String makeUniqueName(String filename) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + filename;
    }

    public void onOk(ActionEvent actionEvent) {
        if (areaDescription.getText().length() < 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.solve.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.solve.errorHeader"));
            alert.setContentText(resourceBundle.getString("app.solve.errorText"));
            alert.showAndWait();
            return;
        }

        Solution solution = new Solution(currentBug.getId(), areaDescription.getText(), LocalDate.now(), areaCode.getHtmlText(), "", currentUser);

        if (selectedFile != null) {
            try {
                if (!Files.exists(Paths.get(resourcesPath))) {
                    var value = Files.createDirectory(Paths.get(resourcesPath)).toFile();
                    value.setWritable(true);
                    value.setReadable(true);
                    value.setExecutable(true);
                }
                var targetpath = Paths.get(resourcesPath, makeUniqueName(selectedFile.getName()));
                solution.setImageUrl(targetpath.toString());
                Files.copy(Paths.get(selectedFile.getAbsolutePath()), targetpath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        database.storeSolution(solution);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("app.solve.infoTitle"));
        alert.setHeaderText(resourceBundle.getString("app.solve.infoHeader"));
        alert.setContentText(resourceBundle.getString("app.solve.infoText"));
        alert.showAndWait();

        parent.exitYourselfAndChild(this);
    }

    public void onBtnImage(ActionEvent actionEvent) {
        lblImage.setText("");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resourceBundle.getString("app.create.chooserTitle"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "PNG Image Files", "*.png"
                ),
                new FileChooser.ExtensionFilter(
                        "JPEG Image files", "*.jpg"
                ),
                new FileChooser.ExtensionFilter(
                        "JPG Image files", "*.jpg"
                ),
                new FileChooser.ExtensionFilter(
                        "JPEG Image files", "*.jpeg"
                )
        );

        selectedFile = fileChooser.showOpenDialog(btnImage.getScene().getWindow());

        if (selectedFile != null)
            lblImage.setText(selectedFile.getName());
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
}
