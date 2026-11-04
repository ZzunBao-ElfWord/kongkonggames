package com.kongling.iap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "apple.iap")
public class AppleIapProperties {
    private String bundleId;
    private Long appAppleId;
    private String issuerId;
    private String keyId;
    private String privateKeyPath;
    private List<String> rootCertificatePaths = new ArrayList<>();
    private String internalToken;
    private boolean sandboxOnly;

    public String getBundleId() { return bundleId; }
    public void setBundleId(String bundleId) { this.bundleId = bundleId; }
    public Long getAppAppleId() { return appAppleId; }
    public void setAppAppleId(Long appAppleId) { this.appAppleId = appAppleId; }
    public String getIssuerId() { return issuerId; }
    public void setIssuerId(String issuerId) { this.issuerId = issuerId; }
    public String getKeyId() { return keyId; }
    public void setKeyId(String keyId) { this.keyId = keyId; }
    public String getPrivateKeyPath() { return privateKeyPath; }
    public void setPrivateKeyPath(String privateKeyPath) { this.privateKeyPath = privateKeyPath; }
    public List<String> getRootCertificatePaths() { return rootCertificatePaths; }
    public void setRootCertificatePaths(List<String> rootCertificatePaths) { this.rootCertificatePaths = rootCertificatePaths; }
    public String getInternalToken() { return internalToken; }
    public void setInternalToken(String internalToken) { this.internalToken = internalToken; }
    public boolean isSandboxOnly() { return sandboxOnly; }
    public void setSandboxOnly(boolean sandboxOnly) { this.sandboxOnly = sandboxOnly; }
}
