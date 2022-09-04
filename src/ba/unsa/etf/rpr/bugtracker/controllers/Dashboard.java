package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.enums.Language;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
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
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Dashboard extends AbstractController implements Showable, Initializable {
    private User currentUser;
    public Label testLabel;
    private ResourceBundle resourceBundle;
    private Database database;
    public FlowPane flowPane;
    public GridPane basicGridPane;
    public Button btnOk;
    public ToggleGroup toggleGroup;
    public ChoiceBox<Department> choiceDepartment;
    public ChoiceBox<Language> choiceLanguage;
    public ChoiceBox<Urgency> choiceUrgency;

    public TextField fldUsername;
    public TextField fldLastname;
    public TextField fldFirstname;
    public TextField fldBugTitle;
    public TextField fldKeywords;
    public MenuItem btnImport;


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
        Stage stage = new Stage();
        Details controller = new Details(currentUser, this, database.getBugByTitle(bugTitle));
        showStage(stage, "/views/details.fxml", "app.details.title", 800, 600, controller);
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        testLabel.setText("Hello, " + currentUser);

        choiceDepartment.setItems(Department.getObservableList());
        choiceLanguage.setItems(Language.getObservableList());
        choiceUrgency.setItems(Urgency.getObservableList());

        showFilteredBugs(database.getAllBugs());
    }

    public void showFilteredBugs(List<Bug> bugs) {
        flowPane.getChildren().clear();
        //later do this method for filtering, now only show all of the bugs in database!
        bugs.stream().forEach((Bug bug) -> {
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

    public void onOk(ActionEvent actionEvent) {
        List<Bug> filteredBugs = database.getAllBugs();

        if (((RadioButton)toggleGroup.getSelectedToggle()).getText().equals(resourceBundle.getString("app.dashboard.active")))
            filteredBugs = filteredBugs.stream().filter(bug -> bug instanceof ActiveBug).collect(Collectors.toList());
        else if (((RadioButton)toggleGroup.getSelectedToggle()).getText().equals(resourceBundle.getString("app.dashboard.solved")))
            filteredBugs = filteredBugs.stream().filter(bug -> bug instanceof SolvedBug).collect(Collectors.toList());


        if (choiceDepartment.getSelectionModel().getSelectedItem() != null)
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getUserWhoAsked().getDepartment().toString().toLowerCase().contains(choiceDepartment.getSelectionModel().getSelectedItem().toString().toLowerCase())).collect(Collectors.toList());
        if (choiceLanguage.getSelectionModel().getSelectedItem() != null)
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getLanguage().toString().toLowerCase().contains(choiceLanguage.getSelectionModel().getSelectedItem().toString().toLowerCase())).collect(Collectors.toList());
        if (choiceUrgency.getSelectionModel().getSelectedItem() != null)
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getUrgency().toString().toLowerCase().contains(choiceUrgency.getSelectionModel().getSelectedItem().toString().toLowerCase())).collect(Collectors.toList());

        if (!fldKeywords.getText().trim().isEmpty())
            filteredBugs = filteredBugs.stream().filter((bug -> {
                var hasAllKeywords = true;
                var allKeywords = fldKeywords.getText().split(",");

                for (var keyword: allKeywords) {
                    if (!bug.getKeywords().toLowerCase().contains(keyword.trim().toLowerCase())) {
                        hasAllKeywords = false;
                        break;
                    }
                }
                return hasAllKeywords;
            })).collect(Collectors.toList());

        if (!fldBugTitle.getText().trim().isEmpty())
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getTitle().toLowerCase().contains(fldBugTitle.getText().toLowerCase().trim())).collect(Collectors.toList());

        if (!fldUsername.getText().trim().isEmpty())
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getUserWhoAsked().getUsername().toLowerCase().contains(fldUsername.getText().trim().toLowerCase())).collect(Collectors.toList());

        if (!fldLastname.getText().trim().isEmpty())
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getUserWhoAsked().getLastname().toLowerCase().contains(fldLastname.getText().trim().toLowerCase())).collect(Collectors.toList());

        if (!fldUsername.getText().trim().isEmpty())
            filteredBugs = filteredBugs.stream().filter(bug -> bug.getUserWhoAsked().getFirstname().toLowerCase().contains(fldFirstname.getText().trim().toLowerCase())).collect(Collectors.toList());

        showFilteredBugs(filteredBugs);
    }

    public void onImport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("BUG files (*.bug)", "*.bug");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(btnOk.getScene().getWindow());
        if (file != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setContentText(resourceBundle.getString("app.import.infoText"));

            var option = alert.showAndWait();
            if (!option.isPresent())
                return;

            ActiveBug newBug = new ActiveBug(0, "", "", Language.JAVA, Urgency.LOW, "", "", "", currentUser, LocalDate.now());

            try {
                String[] lines = Files.readString(file.toPath()).split("\n");

                newBug.setId(0);

                if (lines[0].startsWith("BUG:"))
                    newBug.setTitle(lines[0].replace("BUG:", ""));
                if (lines[1].startsWith("BUG:"))
                    newBug.setDescription(lines[1].replace("BUG:", "").trim());
                if (lines[2].startsWith("BUG:"))
                    newBug.setUrgency(Urgency.intToUrgency(Integer.parseInt(lines[2].replace("BUG:", "").trim())));
                if (lines[3].startsWith("BUG:"))
                    newBug.setCode(lines[3].replace("BUG:", "").trim());
                if (lines[4].startsWith("BUG:"))
                    newBug.setKeywords(lines[4].replace("BUG:", "").trim());
                if (lines[5].startsWith("BUG:"))
                    newBug.setLanguage(Language.intToLanguage(Integer.parseInt(lines[5].replace("BUG:", "").trim())));
                if (lines[6].startsWith("BUG:"))
                    newBug.setImageUrl(lines[6].replace("BUG:", "").trim());

            } catch (IOException | InvalidIndexException er) {
                er.printStackTrace();
            }

            if (newBug.getTitle().length() < 5  || newBug.getDescription().length() < 20) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
                alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
                alert.setContentText(resourceBundle.getString("app.create.errorText"));

                alert.showAndWait();
                return;
            } else if (database.getBugByTitle(newBug.getTitle()) != null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(resourceBundle.getString("app.signup.errorTitle"));
                alert.setHeaderText(resourceBundle.getString("app.signup.errorHeader"));
                alert.setContentText(resourceBundle.getString("app.create.databaseErrorText"));

                alert.showAndWait();
                return;
            }

            database.storeBug(newBug);


            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resourceBundle.getString("app.profile.infoTitle"));
            alert.setHeaderText(resourceBundle.getString("app.profile.infoHeader"));
            alert.setContentText(resourceBundle.getString("app.create.infoContent"));
            alert.showAndWait();

            showFilteredBugs(database.getAllBugs());
        }
    }
}
