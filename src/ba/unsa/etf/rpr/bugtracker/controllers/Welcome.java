package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Welcome implements Initializable, Showable {
    public Button btnAbout;
    public Button btnLogin;
    public Button btnSignup;
    public BorderPane borderPane;
    private Database database;

    public Welcome() {
        database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO - later implement this method
    }

    public void showAboutPage(ActionEvent actionEvent) throws IOException {
        Stage aboutStage = new Stage();
        aboutStage.setResizable(false);
        showStage(aboutStage, "/views/about.fxml", "app.about.title", 800, 450);
    }

    public void showLogin(ActionEvent actionEvent) {
        //TODO - go from this to login window
    }

    public void showSignup(ActionEvent actionEvent) {
        //TODO - go from this to signup window
    }

    public void setBosnianLanguage(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("bs", "BA"));
        this.showStage((Stage)btnAbout.getScene().getWindow(), "/views/welcome.fxml", "app.title", 800, 450);
    }

    public void setEnglishLanguage(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("en", "US"));
        this.showStage((Stage)btnAbout.getScene().getWindow(), "/views/welcome.fxml", "app.title", 800, 450);
    }


}
