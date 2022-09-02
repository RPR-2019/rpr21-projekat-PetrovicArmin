package ba.unsa.etf.rpr.bugtracker.common.exceptions;

public class AlreadyInDatabaseException extends Exception{
    public AlreadyInDatabaseException(String message) {
        super(message);
    }
}
