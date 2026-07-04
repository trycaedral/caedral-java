package com.caedral.sdk.model;

public class OverageSummary {

    private boolean enabled;
    private Integer limitCents;
    private int usedCents;
    private Integer remainingCents;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getLimitCents() {
        return limitCents;
    }

    public void setLimitCents(Integer limitCents) {
        this.limitCents = limitCents;
    }

    public int getUsedCents() {
        return usedCents;
    }

    public void setUsedCents(int usedCents) {
        this.usedCents = usedCents;
    }

    public Integer getRemainingCents() {
        return remainingCents;
    }

    public void setRemainingCents(Integer remainingCents) {
        this.remainingCents = remainingCents;
    }
}
