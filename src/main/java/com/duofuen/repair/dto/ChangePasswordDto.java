package com.duofuen.repair.dto;

import javax.validation.constraints.NotNull;

public class ChangePasswordDto {

    @NotNull
    private String curPassword;
    @NotNull
    private String password;

    public String getCurPassword() {
        return curPassword;
    }

    public void setCurPassword(String curPassword) {
        this.curPassword = curPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
