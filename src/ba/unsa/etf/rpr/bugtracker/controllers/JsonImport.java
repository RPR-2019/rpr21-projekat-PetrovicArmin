package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class JsonImport extends AbstractController implements Initializable, Showable {
    ResourceBundle resourceBundle;
    private User user;
    private Dashboard parent;
    public TextField fldBugId;
    public Button btnOk;
    public Button btnCancel;
    private ActiveBug bug = new ActiveBug();
    private Database database;

    private String xMasterKey = "$2b$10$aN9aF1.uLkmX71hSEaRdHewa2GKFUNUeYDq0Pl94rYUMWzayX4d6u";
    private String xAccessKey = "$2b$10$QAVfgDZ/VRlR32g2.YmXQOAPNG2w612pABhcgNX4QizgtxffaYr4q";

    private final String root = "https://api.jsonbin.io/v3/b/";


    public JsonImport(Dashboard parent, User user) {
        this.parent = parent;
        this.user = user;
        this.database = Database.getInstance();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void concurrentMethod() throws IOException, InvalidIndexException {
        URL url = new URL(root + fldBugId.getText());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Master-Key", xMasterKey);
        con.setRequestProperty("X-Access-Key", xAccessKey);
        con.setRequestProperty("X-Bin-Meta", "false");

        //read returned values - don't need 'em for now!
        BufferedReader ulaz = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String json = "", line = null;
        while ((line = ulaz.readLine()) != null)
            json = json + line;

        //save bug to database!
        JSONObject jsonObject = new JSONObject(json);
        bug.setTitle(jsonObject.getString("title"));
        bug.setDescription(jsonObject.getString("description"));
        bug.setUrgency(Urgency.intToUrgency(jsonObject.getInt("urgency")));
        bug.setCode(jsonObject.getString("code").replace("\\\"", "\""));
        bug.setKeywords(jsonObject.getString("keywords"));
        bug.setLanguage(Language.intToLanguage(jsonObject.getInt("language")));
        bug.setImageUrl(jsonObject.getString("imageUrl"));
        bug.setDatePosted(LocalDate.now());
        bug.setId(0);
        bug.setUserWhoAsked(user);

        if (bug.getTitle().length() < 5  || bug.getDescription().length() < 20) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
                alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
                alert.setContentText(resourceBundle.getString("app.create.errorText"));

                alert.showAndWait();
            });
            return;
        } else if (database.getBugByTitle(bug.getTitle().trim()) != null) {
            Platform.runLater(()-> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
                alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
                alert.setContentText(resourceBundle.getString("app.create.databaseErrorText"));

                alert.showAndWait();
            });
            return;
        }

        database.storeBug(bug);


        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setContentText(resourceBundle.getString("app.json.success"));
            alert.showAndWait();

            parent.refresh();
            ((Stage)btnOk.getScene().getWindow()).close();
        });
    }

    public void onImport(ActionEvent actionEvent) throws IOException, InvalidIndexException {
        Thread getJsonThread = new Thread(() -> {
            try {
                concurrentMethod();
            } catch (IOException | InvalidIndexException e) {
                e.printStackTrace();
            }
        });

        getJsonThread.start();
    }

    public void onCancel(ActionEvent actionEvent) {
        ((Stage)btnOk.getScene().getWindow()).close();
    }
}
