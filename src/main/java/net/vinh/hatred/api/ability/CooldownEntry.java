package net.vinh.hatred.api.ability;

public class CooldownEntry {

    public long readyTick;

    public int charges;
    public int maxCharges;

    public CooldownEntry(long readyTick, int charges, int maxCharges) {
        this.readyTick = readyTick;
        this.charges = charges;
        this.maxCharges = maxCharges;
    }
}
