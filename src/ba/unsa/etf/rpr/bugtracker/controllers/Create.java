package ba.unsa.etf.rpr.bugtracker.controllers;


import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Create extends AbstractController implements Showable, Initializable {
    public TextField fldTitle;
    public ChoiceBox choiceUrgency;
    public TextArea descriptionArea;
    public HTMLEditor htmlArea;
    public TextField fldKeywords;
    public ChoiceBox choiceLanguage;
    public Label lblImageName;
    public Button btnAddImage;
    private ResourceBundle resourceBundle;
    private Database database;
    private User user;

    public Create(User user) {
        database = Database.getInstance();
        this.user = user;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        fldTitle.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!hasAtLeastNCharacters(newValue, 5))
                changeBackgroundColor(fldTitle, "error");
            else
                changeBackgroundColor(fldTitle, "success");
        });

        descriptionArea.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!hasAtLeastNCharacters(newValue, 20))
                changeBackgroundColor(descriptionArea, "error");
            else
                changeBackgroundColor(descriptionArea, "success");
        });

    }

    private static <T extends TextInputControl> void changeBackgroundColor(T field, String status) {
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

    public void onOk(ActionEvent actionEvent) {
        if (!hasAtLeastNCharacters(fldTitle.getText(), 5) || !hasAtLeastNCharacters(descriptionArea.getText(), 20)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
            alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
            alert.setContentText(resourceBundle.getString("app.create.errorText"));

            alert.showAndWait();
            return;
        }
    }

    public void onCancel(ActionEvent event) {
        //možda nešto odraditi ovdje kasnije!
        ((Stage)fldTitle.getScene().getWindow()).close();
    }
}
