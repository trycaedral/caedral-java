package com.caedral.sdk.exception;

/**
 * Raised on network failures or timeouts.
 */
public class CaedralNetworkException extends CaedralAPIException {

    public CaedralNetworkException(String message, Throwable cause) {
        super(message, 0, "network_error", null);
        if (cause != null) {
            initCause(cause);
        }
    }
}
