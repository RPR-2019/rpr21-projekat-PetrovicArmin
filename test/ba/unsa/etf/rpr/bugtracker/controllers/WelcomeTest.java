package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.*;

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
        robot.clickOn("#languageBtn").clickOn("#bosnianBtn");
        testBosnianHelper(robot);
    }

    void testEnglishHelper(FxRobot robot) {
        assertAll(
                () -> assertEquals("Log in", robot.lookup("#btnLogin").queryAs(Button.class).getText().replace("_", "")),
                () -> assertEquals("Sign up", robot.lookup("#btnSignup").queryAs(Button.class).getText().replace("_", "")),
                () -> assertEquals("About", robot.lookup("#btnAbout").queryAs(Button.class).getText().replace("_", ""))
        );
    }

    void testBosnianHelper(FxRobot robot) {
        assertAll(
                () -> assertEquals("Uloguj se", robot.lookup("#btnLogin").queryAs(Button.class).getText().replace("_", "")),
                () -> assertEquals("Prijavi se", robot.lookup("#btnSignup").queryAs(Button.class).getText().replace("_", "")),
                () -> assertEquals("O aplikaciji", robot.lookup("#btnAbout").queryAs(Button.class).getText().replace("_", ""))
        );
    }

    @Test
    void testEnglishLanguage(FxRobot robot) {
        robot.clickOn("#languageBtn").clickOn("#englishBtn");
        testEnglishHelper(robot);
    }

    @Test
    void testAboutButton(FxRobot robot) {
        robot.clickOn("#btnAbout");
        Label label = robot.lookup("#header").queryAs(Label.class);
        assertTrue(label.getText().equals("Basic application usage") || label.getText().equals("Osnovna upotreba aplikacije"));
    }

    @Test
    void testKeyboardShortcuts(FxRobot robot) {
        robot.press(KeyCode.CONTROL).press(KeyCode.SHIFT).press(KeyCode.B).release(KeyCode.B).release(KeyCode.SHIFT).release(KeyCode.CONTROL);
        testBosnianHelper(robot);

        robot.press(KeyCode.CONTROL).press(KeyCode.SHIFT).press(KeyCode.E).release(KeyCode.E).release(KeyCode.SHIFT).release(KeyCode.CONTROL);
        testEnglishHelper(robot);
    }

    Stage getStageAfterClick(FxRobot robot, String id) {
        robot.clickOn(id);

        //last window in list is always last window that is opened by robot!
        return (Stage) robot.listTargetWindows().get(robot.listTargetWindows().size() - 1);
    }

    @Test
    void testSignUpButton(FxRobot robot) {
        Stage current = getStageAfterClick(robot, "#btnSignup");
        assertTrue(current.getTitle().equals("Sign up") || current.getTitle().equals("Prijavi se"));
    }

    @Test
    void testLogInButton(FxRobot robot) {
        Stage current = getStageAfterClick(robot, "#btnLogin");
        System.out.println(robot.listTargetWindows().size());
        assertTrue(current.getTitle().equals("Log in") || current.getTitle().equals("Uloguj se"));
    }
}