package com.caedral.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Model {

    private String id;
    private String object;
    private long created;

    @JsonProperty("owned_by")
    private String ownedBy;

    private String name;
    private String description;

    @JsonProperty("context_window")
    private int contextWindow;

    @JsonProperty("pricing_tier")
    private String pricingTier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getContextWindow() {
        return contextWindow;
    }

    public void setContextWindow(int contextWindow) {
        this.contextWindow = contextWindow;
    }

    public String getPricingTier() {
        return pricingTier;
    }

    public void setPricingTier(String pricingTier) {
        this.pricingTier = pricingTier;
    }
}
