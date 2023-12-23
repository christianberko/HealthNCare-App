package exceptions;

public class HasDependencyException extends Exception {
    private final String MESSAGE = "This item cannot be deleted since it is depended on by other items.";
    private String nameOfDependentItem;

    public HasDependencyException(String nameOfDependentItem) {
        this.nameOfDependentItem = nameOfDependentItem;
    }

    public String getMessage() {
        return MESSAGE + "This item is depended on by: " + nameOfDependentItem;
    }
}
