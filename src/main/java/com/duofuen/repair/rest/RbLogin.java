package com.duofuen.repair.rest;

public class RbLogin extends BaseResultBody {

    // 00 - manager ; 01 - repairman
    private String roleCode;

    private String userId;

    public RbLogin(String roleCode, String userId) {
        this.roleCode = roleCode;
        this.userId = userId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
