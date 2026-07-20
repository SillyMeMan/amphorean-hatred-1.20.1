package net.vinh.hatred.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("The detective game is over. You shouldn't be here");
    }
}
