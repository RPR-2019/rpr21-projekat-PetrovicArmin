package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class LoginTest implements Showable {
    private Login controller;
    static private Database database = Database.getInstance();

    @Start
    public void start(Stage stage) throws Exception{
        stage.setResizable(true);
        controller = (Login) showStage(stage, "/views/login.fxml", "app.login.title", 400, 120);
    }

    @BeforeAll
    static void beforeAll() {
        try {
            Statement stmt = database.getConn().createStatement();
            stmt.executeUpdate("INSERT INTO User(firstname, lastname, username, password, email, department) VALUES('armin', 'petrovic', 'bb234443', 'Pa$$w0rdaaa', 'apetrovic1@etf.unsa.ba', 3);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void afterAll() {
        try {
            Statement stmt = database.getConn().createStatement();
            stmt.executeUpdate("DELETE FROM User WHERE username = 'bb234443';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void assertOpened(FxRobot robot, String englishTitle, String bosnianTitle) throws InterruptedException {
        boolean validTest = false;
        for (Window w: robot.listWindows()) {
            Stage s = (Stage)w;
            System.out.println(s.getTitle());
            if (s.getTitle().equals(englishTitle) || s.getTitle().equals(bosnianTitle)) {
                Platform.runLater(() -> s.close());
                validTest = true;
                break;
            }
        }
        Thread.sleep(100);
        assertTrue(validTest);
    }

    @Test
    public void testValidCredentials(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldUsernameOrEmail");
        robot.write("bb234443");
        robot.doubleClickOn("#fldPassword");
        robot.write("Pa$$w0rdaaa");
        robot.clickOn("#btnOk");
        Thread.sleep(100);
        assertOpened(robot, "Control panel", "Kontrolni panel");
    }

    @Test
    public void testinValidUsername(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldUsernameOrEmail");
        robot.write("nešto bezveze");
        robot.doubleClickOn("#fldPassword");
        robot.write("Pa$$w0rdaaa");
        robot.clickOn("#btnOk");
        Thread.sleep(100);
        assertOpened(robot, "Error", "Greška");
    }

    @Test
    public void testinValidPassword(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldUsernameOrEmail");
        robot.write("bb234443");
        robot.doubleClickOn("#fldPassword");
        robot.write("Pa$$w0rdaaa - netačna!");
        robot.clickOn("#btnOk");
        Thread.sleep(100);
        assertOpened(robot, "Error", "Greška");
        robot.clickOn("#btnCancel");
    }
}