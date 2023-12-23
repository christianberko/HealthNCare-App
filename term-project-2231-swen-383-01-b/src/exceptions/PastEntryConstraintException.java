package exceptions;

public class PastEntryConstraintException extends Exception {
  private final String MESSAGE =
      "Daily limits cannot be set for past dates. If you are setting a limit on the current date, ensure that no entries have been logged for the day before configuring the limit. \n\nPlease review and correct the date before attempting to set a new daily limit.";

  public String getMessage() {
    return MESSAGE;
  }
}
