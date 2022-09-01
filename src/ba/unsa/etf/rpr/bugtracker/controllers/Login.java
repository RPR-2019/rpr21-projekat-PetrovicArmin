package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Login extends AbstractController implements Showable, Initializable {
    private Database database;

    public Login() {
        database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
