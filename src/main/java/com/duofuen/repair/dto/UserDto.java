package com.duofuen.repair.dto;

import com.duofuen.repair.util.Const;

public class UserDto {
    private String username;
    private String name;
    private String phoneNum;
    private Const.Role role;
    private Boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Const.Role getRole() {
        return role;
    }

    public void setRole(Const.Role role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
