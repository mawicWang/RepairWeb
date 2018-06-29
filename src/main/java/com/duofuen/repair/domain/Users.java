package com.duofuen.repair.domain;


import javax.persistence.*;

@Entity
public class Users {

    @Id
    private String username;
    private String password;
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private Authorities authorities;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private UserInfo userInfo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Authorities getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authorities authorities) {
        this.authorities = authorities;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}


