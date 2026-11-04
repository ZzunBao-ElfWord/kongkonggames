package com.kongling.iap.api;

import javax.validation.constraints.NotNull;

public class AckGrantRequest {
    @NotNull
    private Long userId;
    private String businessOrderNo;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBusinessOrderNo() { return businessOrderNo; }
    public void setBusinessOrderNo(String businessOrderNo) { this.businessOrderNo = businessOrderNo; }
}
