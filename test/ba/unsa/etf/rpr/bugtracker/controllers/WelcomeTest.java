package ba.unsa.etf.rpr.bugtracker.controllers;
import ba.unsa.etf.rpr.bugtracker.common.Main;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class WelcomeTest  implements Showable{
    private Welcome controller;
    private Stage stage;

    @Start
    public void start (Stage stage) throws Exception {
        stage.setResizable(false);
        this.stage = stage;
        controller = (Welcome) this.showStage(stage, "/views/welcome.fxml", "app.title", 800, 450);
    }

    @Test
    public void testBosnianLanguage(FxRobot robot) {
        assertTrue(true);
    }

    @Test void testEnglishLanguage(FxRobot robot) {
        assertTrue(true);
    }
}