package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class AboutTest implements Showable{
    private About controller;
    private Stage stage;

    @Start
    public void start (Stage stage) throws Exception {
        stage.setResizable(false);
        this.stage = stage;
        controller = (About) showStage(stage, "/views/about.fxml", "app.about.title", 800, 400);
    }

    @Test
    public void testBosnianLanguage(FxRobot robot) {
        Locale.setDefault(new Locale("bs", "BA"));
        Platform.runLater(() -> controller = (About) showStage(stage, "/views/about.fxml", "app.about.title", 800, 400));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Label label = robot.lookup("#header").queryAs(Label.class);
        assertEquals("Osnovna upotreba aplikacije", label.getText());
    }

    @Test void testEnglishLanguage(FxRobot robot) {
        Locale.setDefault(new Locale("en", "US"));
        Platform.runLater(() -> controller = (About) showStage(stage, "/views/about.fxml", "app.about.title", 800, 400));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Label label = robot.lookup("#header").queryAs(Label.class);
        assertEquals("Basic application usage", label.getText());
    }
}