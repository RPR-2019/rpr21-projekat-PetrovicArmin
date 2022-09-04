package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
import ba.unsa.etf.rpr.bugtracker.models.Bug;
import ba.unsa.etf.rpr.bugtracker.models.SolvedBug;
import ba.unsa.etf.rpr.bugtracker.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Dashboard extends AbstractController implements Showable, Initializable {
    private User currentUser;
    public Label testLabel;
    private ResourceBundle resourceBundle;
    private Database database;
    public FlowPane flowPane;
    public GridPane basicGridPane;

    public Dashboard(User currentUser) {
        database = Database.getInstance();
        this.setCurrentUser(currentUser);
    }

    private GridPane gridPaneFactory() {
        GridPane gridPane = new GridPane();
        gridPane.vgapProperty().set(5.0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMinWidth(10);
        gridPane.getColumnConstraints().add(columnConstraints);

        columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);
        columnConstraints.setMinWidth(10);
        gridPane.getColumnConstraints().add(columnConstraints);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10);
        rowConstraints.setPrefHeight(USE_COMPUTED_SIZE);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10);
        rowConstraints.setPrefHeight(USE_COMPUTED_SIZE);
        rowConstraints.setVgrow(Priority.SOMETIMES);

        Label title = new Label("");
        title.setFont(new Font(16));
        gridPane.getChildren().add(title);
        GridPane.setColumnSpan(title, 2);
        GridPane.setHalignment(title, HPos.CENTER);

        Label info = new Label("");
        gridPane.getChildren().add(info);
        GridPane.setColumnSpan(info, 2);
        GridPane.setRowIndex(info, 1);

        gridPane.setPadding(new Insets(5,5,5,5));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(30);
        dropShadow.setRadius(15);
        dropShadow.setWidth(30);
        dropShadow.setColor(Color.color(0,0,0));
        gridPane.setEffect(dropShadow);
        gridPane.setOnMouseClicked((mouse) -> {
            this.onGridClicked(((Label)gridPane.getChildren().get(0)).getText());
        });

        return gridPane;
    }

    public void onGridClicked(String bugTitle) {
        System.out.println("Naslov buga je: " + bugTitle);
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        testLabel.setText("Hello, " + currentUser);
        this.showFilteredBugs(); //if there are no filters, this method shows all bugs that exist in database!
    }

    public void showFilteredBugs() {
        //later do this method for filtering, now only show all of the bugs in database!
        database.getAllBugs().stream().forEach((Bug bug) -> {
            String info = resourceBundle.getString("app.dashboard.cardFirst");
            info += bug.getUserWhoAsked().getUsername() + " " +  resourceBundle.getString("app.dashboard.cardSecond") + " " +  bug.getDatePosted().toString();

            GridPane newGrid = gridPaneFactory();
            ((Label)newGrid.getChildren().get(0)).setText(bug.getTitle());
            ((Label)newGrid.getChildren().get(1)).setText(info);

            switch(bug.getUrgency()) {
                case LOW -> newGrid.getStyleClass().add("cardLittle");
                case MEDIUM -> newGrid.getStyleClass().add("cardMedium");
                case HIGH -> newGrid.getStyleClass().add("cardLarge");
                case CRITICAL -> newGrid.getStyleClass().add("cardCritical");
            }

            flowPane.getChildren().add(newGrid);
        });
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void onProfile(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Profile controller = new Profile(currentUser, this);
        stage.setMinWidth(400);
        stage.setMinHeight(250);
        showStage(stage, "/views/profile.fxml", "app.profile.title", 400, 200, controller);
    }

    public void refresh() {
        if (testLabel == null) //important @ testing
            return;
        Stage s = (Stage)testLabel.getScene().getWindow();
        showStage(s, "/views/dashboard.fxml", "app.dashboard.title", 800, 600, this);
    }

    public void onLogout(ActionEvent actionEvent) {
        ((Stage)testLabel.getScene().getWindow()).close();
    }

    public void createNew(ActionEvent actionEvent) {
        Stage s = new Stage();
        Create controller = new Create(currentUser, this);
        showStage(s, "/views/create.fxml", "app.create.title", 800, 600, controller);
    }
}
