package ba.unsa.etf.rpr.bugtracker.common.enums;

public enum Urgency {
    LOW("Little bug"),
    MEDIUM("Medium sized issue"),
    HIGH("Large problem"),
    CRITICAL("Critical company problem");

    private final String name;

    Urgency(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
