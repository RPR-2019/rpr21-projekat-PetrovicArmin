package ba.unsa.etf.rpr.bugtracker.common.enums;

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

     Department(String name) {
         this.name = name;
     }

     @Override
     public String toString() {
         return this.name;
     }
}
