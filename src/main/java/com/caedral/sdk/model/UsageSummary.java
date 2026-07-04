package com.caedral.sdk.model;

public class UsageSummary {

    private String accountStatus;
    private String plan;
    private String planStatus;
    private int balanceCents;
    private WeeklyPool weeklyPool;
    private OverageSummary overage;
    private int balanceWeightedUnitsAffordable;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public int getBalanceCents() {
        return balanceCents;
    }

    public void setBalanceCents(int balanceCents) {
        this.balanceCents = balanceCents;
    }

    public WeeklyPool getWeeklyPool() {
        return weeklyPool;
    }

    public void setWeeklyPool(WeeklyPool weeklyPool) {
        this.weeklyPool = weeklyPool;
    }

    public OverageSummary getOverage() {
        return overage;
    }

    public void setOverage(OverageSummary overage) {
        this.overage = overage;
    }

    public int getBalanceWeightedUnitsAffordable() {
        return balanceWeightedUnitsAffordable;
    }

    public void setBalanceWeightedUnitsAffordable(int balanceWeightedUnitsAffordable) {
        this.balanceWeightedUnitsAffordable = balanceWeightedUnitsAffordable;
    }
}
