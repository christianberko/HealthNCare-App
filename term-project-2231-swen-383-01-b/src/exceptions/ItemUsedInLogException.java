package exceptions;

public class ItemUsedInLogException extends Exception {
    private final String MESSAGE = "This item cannot be deleted since it used in log entries and would invalidate them";

    public String getMessage() {
        return MESSAGE;
    }
}
