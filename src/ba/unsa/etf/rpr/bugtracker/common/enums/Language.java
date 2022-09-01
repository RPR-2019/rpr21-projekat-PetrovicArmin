package ba.unsa.etf.rpr.bugtracker.common.enums;

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

    Language(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
