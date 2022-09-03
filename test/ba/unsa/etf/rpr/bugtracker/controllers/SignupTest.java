package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class SignupTest implements Showable {
    private Signup controller;

    //CANNOT test email functionality, because API has only limited number of
    //free email verifications to do!

    @Start
    public void start(Stage stage) throws Exception{
        stage.setResizable(Boolean.parseBoolean("true"));
        controller = (Signup) showStage(stage, "/views/signup.fxml", "app.signup.title", 800, 300);
        controller.btnOk.setDisable(false); //for testing purposes only!
    }

    private static boolean hasCssClass(Node element, String cssClass) {
        return element.getStyleClass().contains(cssClass);
    }

    private static void inputTest(FxRobot robot, String id, String input, boolean isValidInput) {
        robot.clickOn(id);
        robot.write(input);

        var field = robot.lookup(id).queryAs(TextField.class);

        if (!isValidInput)
            assertTrue(hasCssClass(field, "errorColor") && !hasCssClass(field, "successColor"));
        else
            assertTrue(hasCssClass(field, "successColor") && !hasCssClass(field, "errorColor"));
    }

    private void assertErrorOpened(FxRobot robot) throws InterruptedException {
        boolean validTest = false;
        for (Window w: robot.listWindows()) {
            Stage s = (Stage)w;
            System.out.println(s.getTitle());
            if (s.getTitle().equals("Greška") || s.getTitle().equals("Error")) {
                Platform.runLater(() -> s.close());
                validTest = true;
                break;
            }
        }
        Thread.sleep(100);
        assertTrue(validTest);
    }

    @Test
    public void fieldLastnameTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#fldFirstname");
        robot.write("Armin");
        inputTest(robot, "#fldLastname", "Armin", true);
        robot.doubleClickOn("#fldLastname");
        robot.write("");
        inputTest(robot, "#fldLastname", "a", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertErrorOpened(robot);
    }

    @Test
    public void fieldFirstnameTest(FxRobot robot) throws InterruptedException {
        inputTest(robot, "#fldFirstname", "Petrović", true);
        robot.doubleClickOn("#fldFirstname");
        robot.write("");
        inputTest(robot, "#fldFirstname", "p", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertErrorOpened(robot);
    }

    @Test
    public void fieldUsernameTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#fldFirstname");
        robot.write("Armin");
        robot.clickOn("#fldLastname");
        robot.write("Petrović");
        inputTest(robot, "#fldUsername", "arminPetrovich", true);
        robot.doubleClickOn("#fldUsername");
        robot.write("");
        inputTest(robot, "#fldUsername", "adi", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertErrorOpened(robot);
    }

    @Test
    public void fieldPasswordTest(FxRobot robot) throws InterruptedException {
        robot.clickOn("#fldFirstname");
        robot.write("Armin");
        robot.clickOn("#fldLastname");
        robot.write("Petrović");
        robot.clickOn("#fldUsername");
        robot.write("BosnianBeast23");
        inputTest(robot, "#fldPassword", "Pa$$w0rd####", true);
        robot.doubleClickOn("#fldPassword");
        robot.write("");
        inputTest(robot, "#fldPassword", "Pa$$w0rd", false);
        robot.clickOn("#btnOk");
        Thread.sleep(200);
        assertErrorOpened(robot);
    }

    @Test
    public void testEmail(FxRobot robot) throws InterruptedException {
        robot.clickOn("#fldEmail");
        robot.write("neispravan email!");
        robot.clickOn("#btnVerify");
        Thread.sleep(100);
        var statusButton = robot.lookup("#btnStatus").queryButton();
        ImageView imageView = (ImageView) statusButton.getGraphic();
        assertTrue(imageView.getImage().getUrl().endsWith("/images/error.png"));
        robot.clickOn("#btnCancel");
    }
}