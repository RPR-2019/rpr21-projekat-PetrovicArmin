package ba.unsa.etf.rpr.bugtracker.common.enums;

import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public enum Urgency {
    LOW("Little bug"),
    MEDIUM("Medium sized issue"),
    HIGH("Large problem"),
    CRITICAL("Critical company problem");

    private final String name;
    private static ObservableList<Urgency> obsList;

    Urgency(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }

    public static ObservableList<Urgency> getObservableList() {
        if (obsList == null)
            obsList = FXCollections.observableList(List.of(Urgency.values()));
        return obsList;
    }

    public static Urgency intToUrgency(int value) throws InvalidIndexException {
        if (value < 0 || value >= Urgency.values().length)
            throw new InvalidIndexException("Urgency index not valid!");
        return Urgency.values()[value];
    }
}
