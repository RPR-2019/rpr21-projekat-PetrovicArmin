package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.Solution;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ViewSolution extends AbstractController implements Initializable, Showable {
    private Details parent;
    private Database database;
    private ResourceBundle resourceBundle;
    private User currentUser;
    private Solution solution;
    public TextArea areaDescription;
    public HTMLEditor areaCode;
    public Button btnCancel;
    public Button btnImage;
    private File selectedFile;
    public Label lblStatus;


    public ViewSolution(Details parent, User currentUser, Solution solution) {
        this.parent = parent;
        this.database = Database.getInstance();
        this.currentUser = currentUser;
        this.solution = solution;
    }


    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        lblStatus.setText(resourceBundle.getString("app.solution.first") + " " + solution.getUserWhoSolved().getUsername() + " " + resourceBundle.getString("app.solution.second") + " " + solution.getDatePosted().toString());
        areaDescription.setText(solution.getDescription());
        areaCode.setHtmlText(solution.getCode());
    }

    public void onBtnImage(ActionEvent actionEvent) {
        if (solution.getImageUrl() == null || solution.getImageUrl().equals("") || !Files.exists(Paths.get(solution.getImageUrl()))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setHeaderText(resourceBundle.getString("app.details.noImage"));

            alert.showAndWait();
            return;
        }

        Stage newStage = new Stage();
        ShowImage shImage = new ShowImage(solution.getImageUrl());
        showStage(newStage, "/views/showImage.fxml", "app.showImage.title", 800, 600, shImage);
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
}

