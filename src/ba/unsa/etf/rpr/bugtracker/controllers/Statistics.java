package ba.unsa.etf.rpr.bugtracker.controllers;

import ba.unsa.etf.rpr.bugtracker.common.database.Database;
import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import ba.unsa.etf.rpr.bugtracker.common.enums.Urgency;
import ba.unsa.etf.rpr.bugtracker.common.other.Showable;
import ba.unsa.etf.rpr.bugtracker.models.ActiveBug;
import ba.unsa.etf.rpr.bugtracker.models.Bug;
import ba.unsa.etf.rpr.bugtracker.models.SolvedBug;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Statistics extends AbstractController implements Initializable, Showable {
    ResourceBundle resourceBundle;
    Database database;
    public PieChart pieStatusAll;
    public PieChart pieUrgencyAll;
    public BarChart<String, Number> barStatusByDepartments;
    public BarChart<String, Number> barUrgencyByDepartments;

    public Statistics() {
        database = Database.getInstance();
    }

    int numberOfBugsByUrgency(List<Bug> allBugs, Urgency urgency) {
        return (int) allBugs.stream().filter(bug -> bug.getUrgency().equals(urgency)).count();
    }

    private XYChart.Series fillSeries(XYChart.Series series, String status) {
        for (var department: Department.values())
            series.getData().add(new XYChart.Data(department.toString(), getNumberOf(department, status)));
        return series;
    }

    private XYChart.Series fillSeries(XYChart.Series series, Urgency urgency) {
        for (var department: Department.values())
            series.getData().add(new XYChart.Data(department.toString(), getNumberOf(department, urgency)));
        return series;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        var allBugs = database.getAllBugs();

        int numberOfSolved = (int)allBugs.stream().filter(bug -> bug instanceof SolvedBug).count();
        int numberOfActive = allBugs.size() - numberOfSolved;



        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Solved", numberOfSolved),
                        new PieChart.Data("Active", numberOfActive));

        pieStatusAll.setTitle("Out of " + allBugs.size() + " bugs there are:");
        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty()
                        )
                )
        );
        pieStatusAll.setData(pieChartData);

        int lowBugs = numberOfBugsByUrgency(allBugs, Urgency.LOW);
        int midBugs = numberOfBugsByUrgency(allBugs, Urgency.MEDIUM);
        int highBugs = numberOfBugsByUrgency(allBugs, Urgency.HIGH);
        int criticalBugs = numberOfBugsByUrgency(allBugs, Urgency.CRITICAL);

        var pieChartData1 =
                FXCollections.observableArrayList(
                        new PieChart.Data("With low urgency", lowBugs),
                        new PieChart.Data("With mid urgency", midBugs),
                        new PieChart.Data("With high urgency", highBugs),
                        new PieChart.Data("With critical urgency", criticalBugs));

        pieUrgencyAll.setTitle("Out of " + allBugs.size() + " bugs there are:");
        pieChartData1.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " ", data.pieValueProperty()
                        )
                )
        );
        pieUrgencyAll.setData(pieChartData1);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barStatusByDepartments.setTitle("Bug status by departments");
        xAxis.setLabel("Departments");
        yAxis.setLabel("Number of bugs");

        XYChart.Series activeSeries = new XYChart.Series();
        activeSeries.setName("Active");
        activeSeries = fillSeries(activeSeries, "active");


        XYChart.Series solvedSeries = new XYChart.Series();
        solvedSeries.setName("Solved");
        solvedSeries = fillSeries(solvedSeries, "solved");

        barStatusByDepartments.getData().addAll(activeSeries, solvedSeries);

        final CategoryAxis xAxis1 = new CategoryAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        barUrgencyByDepartments.setTitle("Bug status by departments");
        xAxis.setLabel("Departments");
        yAxis.setLabel("Urgency");

        XYChart.Series lowSeries = new XYChart.Series();
        lowSeries.setName("low");
        lowSeries = fillSeries(lowSeries, Urgency.LOW);

        XYChart.Series medSeries = new XYChart.Series();
        medSeries.setName("medium");
        medSeries = fillSeries(medSeries, Urgency.MEDIUM);

        XYChart.Series highSeries = new XYChart.Series();
        highSeries.setName("high");
        highSeries = fillSeries(highSeries, Urgency.HIGH);

        XYChart.Series criticalSeries = new XYChart.Series();
        criticalSeries.setName("critical");
        criticalSeries = fillSeries(criticalSeries, Urgency.CRITICAL);

        barUrgencyByDepartments.getData().addAll(lowSeries, medSeries, highSeries, criticalSeries);
    }

    int getNumberOf(Department department, Urgency urgency) {
        return (int) database.getAllBugs().stream().filter(bug -> bug.getUserWhoAsked().getDepartment().equals(department) && bug.getUrgency().equals(urgency)).count();
    }

    int getNumberOf(Department department, String status) {
        var departmentBugs = database.getAllBugs().stream().filter(bug -> bug.getUserWhoAsked().getDepartment().equals(department));
        if (status.equals("active"))
            return (int) departmentBugs.filter(bug -> bug instanceof ActiveBug).count();
        else
            return (int) departmentBugs.filter(bug -> bug instanceof SolvedBug).count();
    }
}
