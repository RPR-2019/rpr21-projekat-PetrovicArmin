package ba.unsa.etf.rpr.bugtracker.controllers;
import ba.unsa.etf.rpr.bugtracker.common.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class WelcomeTest {
    private Welcome controller;
    private Stage stage;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("/views/welcome.fxml")));
        Parent mainNode = loader.load();
        stage.setScene(new Scene(mainNode));
        this.stage = stage;
        this.controller = loader.getController();
        stage.show();
        stage.toFront();
    }
}