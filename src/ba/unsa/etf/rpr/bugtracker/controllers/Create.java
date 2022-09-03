package ba.unsa.etf.rpr.bugtracker.controllers;


import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
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

public class Create extends AbstractController implements Showable, Initializable {
    public Button btnOk;
    public TextField fldTitle;
    public ChoiceBox<Urgency> choiceUrgency;
    public TextArea descriptionArea;
    public HTMLEditor htmlArea;
    public TextField fldKeywords;
    public ChoiceBox<Language> choiceLanguage;
    public Label lblImageName;
    private String imageUrl;
    public Button btnAddImage;
    private ResourceBundle resourceBundle;
    private Database database;
    private User user;
    File selectedFile = null;
    private String resourcesPath = Paths.get(System.getProperty("user.home"), "resources").toString();

    public Create(User user) {
        database = Database.getInstance();
        this.user = user;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        choiceLanguage.setItems(Language.getObservableList());
        choiceLanguage.setValue(Language.JAVA);
        choiceUrgency.setItems(Urgency.getObservableList());
        choiceUrgency.setValue(Urgency.LOW);

        fldTitle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!hasAtLeastNCharacters(newValue, 5))
                changeBackgroundColor(fldTitle, "error");
            else
                changeBackgroundColor(fldTitle, "success");
        });

        descriptionArea.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!hasAtLeastNCharacters(newValue, 20))
                changeBackgroundColor(descriptionArea, "error");
            else
                changeBackgroundColor(descriptionArea, "success");
        });

    }

    public void uploadImage(ActionEvent actionEvent) {
        lblImageName.setText("");

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

        selectedFile = fileChooser.showOpenDialog(btnAddImage.getScene().getWindow());

        if (selectedFile != null)
            lblImageName.setText(selectedFile.getName());
    }

    //we can go without this, but this is for those extra generic programming points!
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


    private static boolean hasAtLeastNCharacters(String text, int number) {
        return text.trim().length() >= number;
    }

    private String makeUniqueName(String filename) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + filename;
    }

    public void onOk(ActionEvent actionEvent) {
        imageUrl = "";
        if (!hasAtLeastNCharacters(fldTitle.getText(), 5) || !hasAtLeastNCharacters(descriptionArea.getText(), 20)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString("app.create.errorText"));

            alert.showAndWait();
            return;
        } else if (database.getBugByTitle(fldTitle.getText()) != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString("app.create.databaseErrorText"));

            alert.showAndWait();
            return;
        }

        if (selectedFile != null) {
            try {
                if (!Files.exists(Paths.get(resourcesPath)))
                    Files.createDirectory(Paths.get(resourcesPath)).toFile().setWritable(true);
                var targetpath = Paths.get(resourcesPath, makeUniqueName(selectedFile.getName()));
                imageUrl = targetpath.toString();
                Files.copy(Paths.get(selectedFile.getAbsolutePath()), targetpath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        ActiveBug bug = new ActiveBug(0, fldTitle.getText(), descriptionArea.getText(), choiceLanguage.getSelectionModel().getSelectedItem(), choiceUrgency.getSelectionModel().getSelectedItem(), fldKeywords.getText(), htmlArea.getHtmlText(), imageUrl, user, LocalDate.now());
        database.storeBug(bug);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
        alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
        alert.setContentText(resourceBundle.getString("app.create.infoContent"));
        alert.showAndWait();
    }

    public void onCancel(ActionEvent event) {
        ((Stage)fldTitle.getScene().getWindow()).close();
    }
}
