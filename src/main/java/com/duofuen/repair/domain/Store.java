package com.duofuen.repair.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer addr1Id;
    private Integer addr2Id;
    private String addr3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAddr1Id() {
        return addr1Id;
    }

    public void setAddr1Id(Integer addr1Id) {
        this.addr1Id = addr1Id;
    }

    public Integer getAddr2Id() {
        return addr2Id;
    }

    public void setAddr2Id(Integer addr2Id) {
        this.addr2Id = addr2Id;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }
}
