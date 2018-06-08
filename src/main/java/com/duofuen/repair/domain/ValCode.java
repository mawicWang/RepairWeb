package com.duofuen.repair.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class ValCode {
    @Id
    private String phoneNum;
    private String code;
    private Date createTime;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
