package net.vinh.hatred.api.ability;

public enum AbilityResult {
    SUCCESS,
    FAIL,
    CANCELLED,
    PASS;

    public boolean isSuccessful() { return this == SUCCESS || this == PASS; }
}
