package ba.unsa.etf.rpr.bugtracker.common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {
	    launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        Locale locale = new Locale("en", "US");
        Locale.setDefault(locale);
        ResourceBundle bundle = ResourceBundle.getBundle("ba.unsa.etf.rpr.bugtracker.common.languageProperties", locale);
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/welcome.fxml")), bundle);
        stage.setScene(new Scene(root, 800, 450));
        stage.setResizable(false);
        stage.setTitle(bundle.getString("app.title"));
        stage.toFront();
        stage.show();
    }
}
