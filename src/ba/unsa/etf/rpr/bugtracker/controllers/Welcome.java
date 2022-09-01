package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Welcome implements Initializable {
    public Button btnAbout;
    public Button btnLogin;
    public Button btnSignup;
    Database database;

    public Welcome() {
        database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO - later implement this method
    }

    public void showAboutPage(ActionEvent actionEvent) throws IOException {
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/about.fxml")));
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene(root, 600, 450));
        aboutStage.setResizable(false);
        aboutStage.setTitle("About Bug Tracker");
        aboutStage.toFront();
        aboutStage.show();
    }

    public void showLogin(ActionEvent actionEvent) {
        //TODO - go from this to login window
    }

    public void showSignup(ActionEvent actionEvent) {
        //TODO - go from this to signup window
    }
}
