package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
class ProfileTest implements Showable {
    private Profile controller;
    private static Database database = Database.getInstance();

    @Start
    public void start(Stage stage) throws Exception{
        stage.setResizable(true);
        var user = database.getUserByUsername("bb234443");
        assert user != null;
        controller = new Profile(user, new Dashboard(user));
        showStage(stage, "/views/profile.fxml", "app.profile.title", 400, 200, controller);
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
            if (s.getTitle().equals(englishTitle) || s.getTitle().equals(bosnianTitle)) {
                Platform.runLater(() -> s.close());
                validTest = true;
                break;
            }
        }
        Thread.sleep(100);
        assertTrue(validTest);
    }

    private static boolean hasCssClass(Node element, String cssClass) {
        return element.getStyleClass().contains(cssClass);
    }

    private static void inputTest(FxRobot robot, String id, String input, boolean isValidInput) {
        robot.doubleClickOn(id);
        robot.write(input);

        var field = robot.lookup(id).queryAs(TextField.class);

        if (!isValidInput)
            assertTrue(hasCssClass(field, "errorColor") && !hasCssClass(field, "successColor"));
        else
            assertTrue(hasCssClass(field, "successColor") && !hasCssClass(field, "errorColor"));
    }

    @Test
    public void fieldFirstnameTest(FxRobot robot) throws InterruptedException {
        inputTest(robot, "#fldFirstname", "Petrović", true);
        robot.doubleClickOn("#fldFirstname");
        robot.write("");
        inputTest(robot, "#fldFirstname", "p", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertOpened(robot, "Error", "Greška");
    }

    @Test
    public void fieldLastnameTest(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldFirstname");
        robot.write("Armin");
        inputTest(robot, "#fldLastname", "Armin", true);
        robot.doubleClickOn("#fldLastname");
        robot.write("");
        inputTest(robot, "#fldLastname", "a", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertOpened(robot, "Error", "Greška");
    }

    @Test
    public void fieldPasswordTest(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldFirstname");
        robot.write("Armin");
        robot.doubleClickOn("#fldLastname");
        robot.write("Petrović");
        inputTest(robot, "#fldPassword", "Pa$$w0rd####", true);
        robot.doubleClickOn("#fldPassword");
        robot.write("");
        inputTest(robot, "#fldPassword", "Pa$$w0rd", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertOpened(robot, "Error", "Greška");
    }

    @Test
    public void successfullChangeInDatabase(FxRobot robot) throws InterruptedException {
        robot.doubleClickOn("#fldFirstname");
        robot.write("Mujo");
        robot.doubleClickOn("#fldLastname");
        robot.write("Mujić");
        robot.doubleClickOn("#fldPassword");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("");
        robot.write("Pa$$w0rdChanged101");

        robot.clickOn("#btnOk");
        assertOpened(robot, "Informative dialog", "Informativni dijalog");

        Thread.sleep(200);
        var user = database.getUserByUsername("bb234443");

        //assert that user is changed in database!
        assertAll(
                () -> assertEquals(user.getFirstname(), "Mujo"),
                () -> assertEquals(user.getLastname(), "Mujić"),
                () -> assertEquals(user.getPassword(), "Pa$$w0rdChanged101")
        );

        robot.clickOn("#btnCancel");
    }
}