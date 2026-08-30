package com.caedral.sdk.model;

public class UsagePools {

    private UsagePool caedral;
    private UsagePool external;

    public UsagePool getCaedral() {
        return caedral;
    }

    public void setCaedral(UsagePool caedral) {
        this.caedral = caedral;
    }

    public UsagePool getExternal() {
        return external;
    }

    public void setExternal(UsagePool external) {
        this.external = external;
    }
}
