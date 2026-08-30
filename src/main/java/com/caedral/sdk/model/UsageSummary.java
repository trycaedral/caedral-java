package com.caedral.sdk.model;

public class UsageSummary {

    private String accountStatus;
    private UsagePlan plan;
    private UsagePools pools;
    private UsageOnDemand onDemand;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public UsagePlan getPlan() {
        return plan;
    }

    public void setPlan(UsagePlan plan) {
        this.plan = plan;
    }

    public UsagePools getPools() {
        return pools;
    }

    public void setPools(UsagePools pools) {
        this.pools = pools;
    }

    public UsageOnDemand getOnDemand() {
        return onDemand;
    }

    public void setOnDemand(UsageOnDemand onDemand) {
        this.onDemand = onDemand;
    }
}
