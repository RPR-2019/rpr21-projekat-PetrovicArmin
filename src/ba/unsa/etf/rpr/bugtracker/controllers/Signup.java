package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.AlreadyInDatabaseException;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidPasswordException;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.ShortParameterException;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import com.verifalia.api.VerifaliaRestClient;
import com.verifalia.api.emailvalidations.WaitingStrategy;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.exceptions.VerifaliaException;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Signup extends AbstractController implements Showable, Initializable {
    private ResourceBundle resourceBundle;

    public Button btnOk;
    public Button btnCancel;
    public TextField fldFirstname;
    public TextField fldLastname;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public TextField fldEmail;
    public ChoiceBox<Department> choiceDepartment;
    public Button btnStatus;
    private Database database;
    private VerifaliaRestClient verifalia = new VerifaliaRestClient("b15ba79ae19e4137af18baf6005ded7c", "g*FM#$scz92*3zC");


    public Signup() {
        database = Database.getInstance();
    }

    //username:
    // - needs to be unique, when comparing to other usernames in database

    //email:
    // - needs to be unique, when comparing to ones that are already in database


    private static boolean hasAtLeastNCharacters(String text, int number) {
        return text.trim().length() >= number;
    }

    private boolean hasSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.find();
    }

    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void checkEmailVerifalia(String email) {
        Validation validation = null;
        String entryString = "Non deliverable";

        //if basic email structure is not fulfilled, than look no longer
        //for verifalia validation
        if (isValidEmail(email)) {
            try {
                validation = verifalia
                        .getEmailValidations()
                        .submit(email, new WaitingStrategy(true));
            } catch (VerifaliaException e) {
                e.printStackTrace();
            }

            entryString = validation.getEntries().get(0).getClassification().toString();
        }

        String finalEntryString = entryString;

        Platform.runLater(() -> {
            String imagePath = "";
            if (finalEntryString.equals("Deliverable")) {
                imagePath = "/images/checkmark.png";
                btnOk.setDisable(false);
            }
            else {
                imagePath = "/images/error.png";
                btnOk.setDisable(true);
            }

            ImageView imgView = new ImageView(imagePath);
            imgView.setFitWidth(20);
            imgView.setFitHeight(20);

            btnStatus.setGraphic(imgView);
            btnStatus.setVisible(true);
        });

        try {
            if (validation != null)
                verifalia.getEmailValidations().delete(validation.getOverview().getId());
        } catch (VerifaliaException e) {
            e.printStackTrace();
        }
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

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStatus.setVisible(false);
        this.resourceBundle = resourceBundle;

        ///TODO: !!!!
        ////PRODUCTION MODE FOR OUR OK BUTTON
        btnOk.setDisable(false);
        ////

        changeBackgroundColor(fldFirstname, "clear");
        changeBackgroundColor(fldLastname, "clear");
        changeBackgroundColor(fldPassword, "clear");
        changeBackgroundColor(fldUsername, "clear");


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

        fldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (hasAtLeastNCharacters(newValue, 5))
                changeBackgroundColor(fldUsername, "success");
            else
                changeBackgroundColor(fldUsername, "error");
        });

        fldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidPassword(newValue))
                changeBackgroundColor(fldPassword, "success");
            else
                changeBackgroundColor(fldPassword, "error");
        });

        choiceDepartment.setItems(Department.getObservableList());
        choiceDepartment.setValue(Department.HARDWARE);
    }

    public void startCheckEmail(ActionEvent actionEvent) {
        ImageView iv = new ImageView("/images/loading.gif");
        iv.setFitHeight(20);
        iv.setFitWidth(20);
        btnStatus.setGraphic(iv);
        btnStatus.setVisible(true);

        Thread verifyEmailThread = new Thread(() -> checkEmailVerifalia(fldEmail.getText()));
        verifyEmailThread.start();
    }


    public void onOk(ActionEvent actionEvent) {
        try {
            if (!hasAtLeastNCharacters(fldFirstname.getText(), 2))
                throw new ShortParameterException("app.signup.atLeast2CharFirst");
            if (!hasAtLeastNCharacters(fldLastname.getText(), 2))
                throw new ShortParameterException("app.signup.atLeast2CharLast");
            if (!hasAtLeastNCharacters(fldUsername.getText(), 5))
                throw new ShortParameterException("app.signup.atLeast5CharUser");

            if (!isValidPassword(fldPassword.getText()))
                throw new InvalidPasswordException("app.signup.invalidPassword");


            if (database.getUserByEmail(fldEmail.getText()) != null)
                throw new AlreadyInDatabaseException("app.signup.alreadyInDatabaseEmail");
            if (database.getUserByUsername(fldUsername.getText()) != null)
                throw new AlreadyInDatabaseException("app.signup.alreadyInDatabaseUsername");

            //here we are ready to go into login page, and to save user in database!
        } catch (ShortParameterException | InvalidPasswordException | AlreadyInDatabaseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString(e.getMessage()));

            alert.showAndWait();
        }
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();
    }
}
