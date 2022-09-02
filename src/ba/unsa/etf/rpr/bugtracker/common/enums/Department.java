package ba.unsa.etf.rpr.bugtracker.common.enums;

import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public enum Department {
    BACKEND("Backend development"),
    FRONTEND("Frontend development"),
    MOBILE("Mobile applications"),
    DATABASE("Database administration"),
    CLOUD("Cloud management"),
    SALES("Sales department"),
    HARDWARE("Hardware management"),
    NETWORKING("Networking administration");

    private final String name;
    private static ObservableList<Department> obsList;

    public static ObservableList<Department> getObservableList() {
        if (obsList == null)
            obsList = FXCollections.observableList(List.of(Department.values()));

        return obsList;
    }

    Department(String name) {
         this.name = name;
    }

    @Override
    public String toString() {
         return this.name;
    }

    public static Department intToDepartment(int value) throws InvalidIndexException {
        if (value < 0 || value >= Department.values().length)
            throw new InvalidIndexException("Department index not valid!");
        return Department.values()[value];
    }
}
