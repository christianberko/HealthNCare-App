package exceptions;

public class FoodNotExistsException extends Exception {
    private final String MESSAGE = "This food does not exist";

    public String getMessage() {
        return MESSAGE;
    }
}
