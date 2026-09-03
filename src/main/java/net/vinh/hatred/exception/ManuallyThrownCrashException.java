package net.vinh.hatred.exception;

public class ManuallyThrownCrashException extends RuntimeException {
    public ManuallyThrownCrashException(String message) {
        super(message);
    }
}
