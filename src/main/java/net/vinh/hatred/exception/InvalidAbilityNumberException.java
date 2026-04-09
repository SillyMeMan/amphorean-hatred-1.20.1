package net.vinh.hatred.exception;

@Deprecated
public class InvalidAbilityNumberException extends RuntimeException {
    public InvalidAbilityNumberException() {
        super("Ability number needs to be an integer between 1 - 4");
    }
}
