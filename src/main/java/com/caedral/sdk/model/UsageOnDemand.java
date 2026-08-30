package com.caedral.sdk.model;

public class UsageOnDemand {

    private String mode;
    private boolean allowed;
    private int accruedMilli;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getAccruedMilli() {
        return accruedMilli;
    }

    public void setAccruedMilli(int accruedMilli) {
        this.accruedMilli = accruedMilli;
    }
}
