package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
import ba.unsa.etf.rpr.bugtracker.models.Bug;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class Details extends AbstractController implements Serializable, Showable, Initializable {
    private String defaultExportName = "exported.bug";
    public Button btnShowImage;
    public Button btnSolve;
    public Button btnOk;
    public TextField fldTitle;
    public TextField choiceUrgency;
    public TextArea descriptionArea;
    public HTMLEditor htmlArea;
    public TextField fldKeywords;
    public TextField choiceLanguage;
    public Button btnExport;
    public Button btnViewSolution;

    public Dashboard getParentController() {
        return parentController;
    }

    public void setParentController(Dashboard parentController) {
        this.parentController = parentController;
    }

    private Dashboard parentController = null;
    public Label lblImageName;
    private ResourceBundle resourceBundle;
    private Database database;
    private User user;
    private Bug currentBug;
    File selectedFile = null;
    private String resourcesPath = Paths.get(System.getProperty("user.home"), "resources").toString();

    public Details(User user, Dashboard parentController, Bug currentBug) {
        database = Database.getInstance();
        this.user = user;
        this.parentController = parentController;
        this.currentBug = currentBug;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        choiceLanguage.setText(currentBug.getLanguage().toString());
        choiceUrgency.setText(currentBug.getUrgency().toString());

        fldTitle.setText(currentBug.getTitle());
        fldKeywords.setText(currentBug.getKeywords());
        descriptionArea.setText(currentBug.getDescription());
        htmlArea.setHtmlText(currentBug.getCode());

        if (currentBug instanceof ActiveBug) {
            btnSolve.setVisible(true);
            btnViewSolution.setVisible(false);
        }
        else {
            btnSolve.setVisible(false);
            btnViewSolution.setVisible(true);
        }
    }

    public void onSolve(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Solve child = new Solve(this, user, currentBug);
        showStage(stage, "/views/solve.fxml", "app.solve.title", 800, 450, child);
    }

    public void onImageView(ActionEvent actionEvent) {
        if (currentBug.getImageUrl() == null || currentBug.getImageUrl().equals("") || !Files.exists(Paths.get(currentBug.getImageUrl()))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setHeaderText(resourceBundle.getString("app.details.noImage"));

            alert.showAndWait();
            return;
        }

        Stage newStage = new Stage();
        ShowImage shImage = new ShowImage(this, currentBug.getImageUrl());
        showStage(newStage, "/views/showImage.fxml", "app.showImage.title", 800, 600, shImage);
    }

    public void onCancel(ActionEvent event) {
        ((Stage)fldTitle.getScene().getWindow()).close();
    }

    public void onExport(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();

        chooser.setTitle(resourceBundle.getString("choose.saveFile"));


        chooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = chooser.showDialog(btnSolve.getScene().getWindow());


        try {
            FileWriter fileWriter = new FileWriter(Paths.get(selectedDirectory.toString(), defaultExportName).toFile());
            fileWriter.write("BUG: " + currentBug.getTitle() + "\n");
            fileWriter.write("BUG: " + currentBug.getDescription() + "\n");
            fileWriter.write("BUG: " + currentBug.getUrgency().ordinal() + "\n");
            fileWriter.write("BUG: " + currentBug.getCode() + "\n");
            fileWriter.write("BUG: " + currentBug.getKeywords() + "\n");
            fileWriter.write("BUG: " + currentBug.getLanguage().ordinal() + "\n");
            fileWriter.write("BUG: " + currentBug.getImageUrl() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
        alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
        alert.setHeaderText(resourceBundle.getString("app.details.exportSuccess"));

        alert.showAndWait();
    }

    public void onViewSolution() {

    }
}
