package ba.unsa.etf.rpr.bugtracker.common.other;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public interface Showable {
    //conditions:
    //      event must have been initiated by button control!
    //      in production mode add controller parameter to send to this function!
    default void showStage(Stage stage, String resourcePath, String titleKey, int width, int height) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("ba.unsa.etf.rpr.bugtracker.common.languageProperties", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath), bundle);
            Parent root = loader.load();
            stage.setTitle(bundle.getString(titleKey));
            stage.setScene(new Scene(root, width, height));
            stage.toFront();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
