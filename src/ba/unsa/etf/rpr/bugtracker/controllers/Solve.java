package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.Bug;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Solve extends AbstractController implements Initializable, Showable {
    private Details parent;
    private Database database;
    private ResourceBundle resourceBundle;
    private User currentUser;
    private Bug currentBug;

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
    }

    public void onOk(ActionEvent actionEvent) {
        System.out.println("onOk!");
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
