package exceptions;

public class ExerciseNotExistsException extends Exception {
    private final String MESSAGE = "An exercise by this name does not exist";

    public String getMessage() {
        return MESSAGE;
    }
}
