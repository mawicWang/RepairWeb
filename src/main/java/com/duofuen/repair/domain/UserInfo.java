package com.duofuen.repair.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class UserInfo {

    @Id
    private String username;
    private String name;
    private String phoneNum;
    private Date lastLoginTime;

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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
