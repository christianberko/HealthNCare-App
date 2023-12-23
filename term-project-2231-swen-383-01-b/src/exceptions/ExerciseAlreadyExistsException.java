package exceptions;

public class ExerciseAlreadyExistsException extends Exception {
    private final String MESSAGE = "An exercise by this name already exists";

    public String getMessage() {
        return MESSAGE;
    }
}
