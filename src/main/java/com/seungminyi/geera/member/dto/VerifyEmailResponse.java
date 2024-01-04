package com.seungminyi.geera.member.dto;

public class VerifyEmailResponse {
    private String securityCode;

    public VerifyEmailResponse(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}

