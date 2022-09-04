package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
class CreateTest implements Showable {
    private Create controller;
    static private Database database = Database.getInstance();

    @Start
    public void start(Stage stage) throws Exception{
        var user = database.getUserByUsername("bb234443");
        controller = new Create(user, new Dashboard(user));
        showStage(stage, "/views/create.fxml", "app.create.title", 800, 600, controller);
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

        Node field = robot.lookup(id).queryAs(Node.class);

        if (!isValidInput)
            assertTrue(hasCssClass(field, "errorColor") && !hasCssClass(field, "successColor"));
        else
            assertTrue(hasCssClass(field, "successColor") && !hasCssClass(field, "errorColor"));
    }

    @Test
    public void titleTest(FxRobot robot) throws InterruptedException {
        inputTest(robot, "#fldTitle", "Nešto nije u redu - naslov!", true);
        robot.doubleClickOn("#fldTitle");
        robot.write("");

        inputTest(robot, "#fldTitle", "a", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertOpened(robot, "Error", "Greška");
    }

    @Test
    public void descriptionTest(FxRobot robot) throws InterruptedException {
        inputTest(robot, "#descriptionArea", "1234567891011121314151617181920", true);
        robot.doubleClickOn("#descriptionArea");
        robot.write("");

        inputTest(robot, "#descriptionArea", "aarminarmin", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertOpened(robot, "Error", "Greška");
        robot.clickOn("#btnCancel");
    }

    //we cannot test FileChooser because robotfx does not know where to click when dialog occurs!


    @Test
    public void newBugTest(FxRobot robot) throws InterruptedException, SQLException {
        robot.doubleClickOn("#fldTitle");
        robot.write("Ispravan naziv!");

        robot.doubleClickOn("#descriptionArea");
        robot.write("Ovo je jedan ispravan opis greške koja mi se desila i koju prijavljujem!");

        assertNull(database.getBugByTitle("Ispravan naziv!"));
        robot.clickOn("#btnOk");
        assertOpened(robot, "Informative dialog", "Informativni dijalog");
        assertNotNull(database.getBugByTitle("Ispravan naziv!"));

        //clear database:
        Statement stmt = database.getConn().createStatement();
        stmt.executeUpdate("DELETE FROM Bug WHERE title = 'Ispravan naziv!';");
    }

    @Test
    public void sameBugTitle(FxRobot robot) throws InterruptedException, SQLException {
        robot.clickOn("#fldTitle");
        robot.write("My Big Bug title!");
        robot.clickOn("#descriptionArea");
        robot.write("Olsfjlsjfljaljdsljldsjalfjaaačaččačkakakk");
        robot.clickOn("#btnOk");
        assertOpened(robot, "Informative dialog", "Informativni dijalog");
        Thread.sleep(200);
        robot.clickOn("#btnOk");
        assertOpened(robot, "Error", "Greška"); //because of same user in database!

        Statement stmt = database.getConn().createStatement();
        stmt.executeUpdate("DELETE FROM Bug WHERE title = 'My Big Bug title!';");
    }
}