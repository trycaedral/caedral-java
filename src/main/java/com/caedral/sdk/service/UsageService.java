package com.caedral.sdk.service;

import com.caedral.sdk.http.CaedralHttpClient;
import com.caedral.sdk.model.UsageSummary;

/**
 * Account usage service backing {@code GET /v1/usage}.
 */
public final class UsageService {

    private final CaedralHttpClient http;

    public UsageService(CaedralHttpClient http) {
        this.http = http;
    }

    /**
     * Fetch a snapshot of the authenticated account's current billing
     * state: plan, weekly free pool utilization, prepaid balance, and
     * overage limits.
     *
     * @return the current {@link UsageSummary} for the account
     * @throws com.caedral.sdk.exception.CaedralAPIException if the API
     *         returns a non-2xx response
     */
    public UsageSummary get() {
        return http.get("/v1/usage", UsageSummary.class);
    }
}
