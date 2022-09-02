package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Signup extends AbstractController implements Showable, Initializable {
    public Button btnOk;
    public Button btnCancel;
    public TextField fldFirstname;
    public TextField fldLastname;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public TextField fldEmail;
    public ChoiceBox fldChoiceBox;
    private Database database;

    public Signup() {
        database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
