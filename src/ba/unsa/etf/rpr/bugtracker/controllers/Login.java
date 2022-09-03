package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Login extends AbstractController implements Showable, Initializable {
    private ResourceBundle resourceBundle;
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
        this.resourceBundle = resourceBundle;
    }

    public void onOk(ActionEvent actionEvent) {
        User user = database.getUserByUsername(fldUsernameOrEmail.getText());
        if (user == null) {
            user = database.getUserByEmail(fldUsernameOrEmail.getText());
            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
                alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
                alert.setContentText(resourceBundle.getString("app.login.noUser"));

                alert.showAndWait();
                return;
            }
        }

        if (!user.getPassword().equals(fldPassword.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString("app.login.incorrectPassword"));

            alert.showAndWait();
            return;
        }


        Stage dashboardStage = new Stage();
        Dashboard dashboardController = new Dashboard(user);
        showStage(dashboardStage, "/views/dashboard.fxml", "app.dashboard.title", 800, 600, dashboardController);
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();
    }
}
