package ba.unsa.etf.rpr.bugtracker.common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
	    launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/welcome.fxml")));
        stage.setScene(new Scene(root, 800, 450));
        stage.setResizable(false);
        stage.setTitle("Bug tracker app");

        stage.toFront();
        stage.show();
    }
}
