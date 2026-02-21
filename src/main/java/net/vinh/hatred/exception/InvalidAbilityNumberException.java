package net.vinh.hatred.exception;

public class InvalidAbilityNumberException extends RuntimeException {
    public InvalidAbilityNumberException() {
        super("Ability number needs to be an integer between 1 - 4");
    }
}
