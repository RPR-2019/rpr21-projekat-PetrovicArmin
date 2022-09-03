package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidPasswordException;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.ShortParameterException;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile extends AbstractController implements Showable, Initializable {
    private ResourceBundle resourceBundle;
    private User user;
    private Database database;
    private Dashboard parentController;

    public Button btnOk;
    public Button btnCancel;
    public TextField fldFirstname;
    public TextField fldLastname;
    public TextField fldPassword;
    public ChoiceBox<Department> choiceDepartment;

    public Profile(User user, Dashboard controller) {
        //we need deep copy in order to work properly!
        this.user = new User(user.getId(), user.getUsername(), user.getLastname(), user.getFirstname(), user.getEmail(), user.getPassword(), user.getDepartment());
        this.database = Database.getInstance();
        this.parentController = controller;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        choiceDepartment.setItems(Department.getObservableList());

        choiceDepartment.setValue(user.getDepartment());
        fldLastname.setText(user.getLastname());
        fldFirstname.setText(user.getFirstname());
        fldPassword.setText(user.getPassword());

        user.departmentProperty().bind(choiceDepartment.getSelectionModel().selectedItemProperty());
        user.lastnameProperty().bind(fldLastname.textProperty());
        user.firstnameProperty().bind(fldFirstname.textProperty());
        user.passwordProperty().bind(fldPassword.textProperty());

        fldFirstname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (hasAtLeastNCharacters(newValue, 2))
                changeBackgroundColor(fldFirstname, "success");
            else
                changeBackgroundColor(fldFirstname, "error");
        });

        fldLastname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (hasAtLeastNCharacters(newValue, 2))
                changeBackgroundColor(fldLastname, "success");
            else
                changeBackgroundColor(fldLastname, "error");
        });

        fldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidPassword(newValue))
                changeBackgroundColor(fldPassword, "success");
            else
                changeBackgroundColor(fldPassword, "error");
        });
    }

    private static void changeBackgroundColor(TextField field, String status) {
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

    private boolean hasSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find();
    }

    private boolean isValidPassword(String password) {
        password = password.trim();
        return (
                hasAtLeastNCharacters(password, 10) &&
                        password.chars().filter(Character::isLowerCase).count() >= 1 &&
                        password.chars().filter(Character::isUpperCase).count() >= 1 &&
                        hasSpecialCharacter(password) &&
                        password.chars().filter(Character::isDigit).count() >= 1
        );
    }

    public void onOk(ActionEvent actionEvent) {
        try {
            if (!hasAtLeastNCharacters(fldFirstname.getText(), 2))
                throw new ShortParameterException("app.signup.atLeast2CharFirst");
            if (!hasAtLeastNCharacters(fldLastname.getText(), 2))
                throw new ShortParameterException("app.signup.atLeast2CharLast");
            if (!isValidPassword(fldPassword.getText()))
                throw new InvalidPasswordException("app.signup.invalidPassword");

            database.updateUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setContentText(resourceBundle.getString("app.profile.updateSuccess"));

            alert.showAndWait();
            changeBackgroundColor(fldFirstname, "clear");
            changeBackgroundColor(fldLastname, "clear");
            changeBackgroundColor(fldPassword, "clear");

            //if we do not do this, we will not be able to see real time updated information!
            this.parentController.setCurrentUser(user);
            this.parentController.refresh();

            ((Stage)btnCancel.getScene().getWindow()).toFront();
        } catch (ShortParameterException | InvalidPasswordException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString(e.getMessage()));

            alert.showAndWait();
        }
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
}
