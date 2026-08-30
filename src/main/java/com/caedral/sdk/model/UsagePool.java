package com.caedral.sdk.model;

public class UsagePool {

    private int usedMilli;
    private int limitMilli;
    private double percentUsed;
    private boolean available;

    public int getUsedMilli() {
        return usedMilli;
    }

    public void setUsedMilli(int usedMilli) {
        this.usedMilli = usedMilli;
    }

    public int getLimitMilli() {
        return limitMilli;
    }

    public void setLimitMilli(int limitMilli) {
        this.limitMilli = limitMilli;
    }

    public double getPercentUsed() {
        return percentUsed;
    }

    public void setPercentUsed(double percentUsed) {
        this.percentUsed = percentUsed;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
