package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import com.verifalia.api.emailvalidations.WaitingStrategy;
import com.verifalia.api.emailvalidations.models.Validation;
import com.verifalia.api.emailvalidations.models.ValidationEntry;
import com.verifalia.api.exceptions.VerifaliaException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.verifalia.api.VerifaliaRestClient;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Signup extends AbstractController implements Showable, Initializable {
    public Button btnOk;
    public Button btnCancel;
    public TextField fldFirstname;
    public TextField fldLastname;
    public TextField fldUsername;
    public PasswordField fldPassword;
    public TextField fldEmail;
    public ChoiceBox choiceDepartment;
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


    private boolean hasAtLeastNCharacters(String text, int number) {
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

        try {
            validation = verifalia
                    .getEmailValidations()
                    .submit(email, new WaitingStrategy(true));
        } catch (VerifaliaException e) {
            e.printStackTrace();
        }

        String entryString = validation.getEntries().get(0).getClassification().toString();

        Platform.runLater(() -> {
            String imagePath = "";
            if (entryString.equals("Deliverable")) {
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
            verifalia.getEmailValidations().delete(validation.getOverview().getId());
        } catch (VerifaliaException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidPassword(String password) {
        password = password.trim();
        return (
            this.hasAtLeastNCharacters(password, 10) &&
            password.chars().filter(Character::isLowerCase).count() >= 1 &&
            password.chars().filter(Character::isUpperCase).count() >= 1 &&
            hasSpecialCharacter(password) &&
            password.chars().filter(Character::isDigit).count() >= 1
        );
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnStatus.setVisible(false);
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
}
