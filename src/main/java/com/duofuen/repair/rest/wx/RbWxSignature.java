package com.duofuen.repair.rest.wx;

import com.duofuen.repair.rest.BaseResultBody;

/**
 * The type Wx signature.
 */
public class RbWxSignature extends BaseResultBody {

    /**
     * The App id.
     */
    private String appId;
    /**
     * The Timestamp.
     */
    private String timestamp;
    /**
     * The Nonce str.
     */
    private String nonceStr;
    /**
     * The Signature.
     */
    private String signature;

    /**
     * Gets app id.
     *
     * @return the app id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Sets app id.
     *
     * @param appId the app id
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets nonce str.
     *
     * @return the nonce str
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * Sets nonce str.
     *
     * @param nonceStr the nonce str
     */
    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    /**
     * Gets signature.
     *
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets signature.
     *
     * @param signature the signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
