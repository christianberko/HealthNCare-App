package exceptions;

public class FoodAlreadyExistsException extends Exception {
  private final String MESSAGE =
      "This food cannot be added to since a food with the same already exists";

  public String getMessage() {
    return MESSAGE;
  }
}
