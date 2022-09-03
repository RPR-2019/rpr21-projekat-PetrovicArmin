package ba.unsa.etf.rpr.bugtracker.common.enums;

import ba.unsa.etf.rpr.bugtracker.common.exceptions.InvalidIndexException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public enum Language {
    JAVASCRIPT("Javascript"),
    JAVA("Java"),
    KOTLIN("Kotlin"),
    CSS("Cascading Style Sheets"),
    HTML("HTML5"),
    CPLUSPLUS("C++"),
    C("C"),
    CSHARP("C#"),
    PYTHON("Python"),
    SQL("Structured query language"),
    MYSQL("MySQL"),
    ASSEMBLY("Assembly"),
    BLOCK("Block diagram"),
    LADDER("Ladder diagram");

    private final String name;
    private static  ObservableList<Language> obsList;

    Language(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ObservableList<Language> getObservableList() {
        if (obsList == null)
            obsList = FXCollections.observableList(List.of(Language.values()));
        return obsList;
    }

    public static Language intToLanguage(int value) throws InvalidIndexException {
        if (value < 0 || value >= Language.values().length)
            throw new InvalidIndexException("Language index not valid!");
        return Language.values()[value];
    }
}
