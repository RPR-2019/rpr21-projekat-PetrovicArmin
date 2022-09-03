package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Login extends AbstractController implements Showable, Initializable {
    private Database database;
    public Button btnOk;
    public Button btnCancel;
    public TextField fldUsernameOrEmail;
    public PasswordField fldPassword;

    public Login() {
        database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onOk(ActionEvent actionEvent) {

    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();
    }
}
