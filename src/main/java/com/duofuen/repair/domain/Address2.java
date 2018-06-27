package com.duofuen.repair.domain;

import javax.persistence.*;

@Entity
public class Address2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "addr1_id")
    private Integer addr1Id;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddr1Id() {
        return addr1Id;
    }

    public void setAddr1Id(Integer addr1Id) {
        this.addr1Id = addr1Id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
