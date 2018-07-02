package com.duofuen.repair.domain;

import javax.persistence.*;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String telephone;
    @Column(name = "addr1_id")
    private Integer addr1Id;
    @Column(name = "addr2_id")
    private Integer addr2Id;
    private String addr3;
    private Boolean enabled;
    @Transient
    private String completeAddr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr1_id", insertable = false, updatable = false)
    private Address1 address1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr2_id", insertable = false, updatable = false)
    private Address2 address2;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCompleteAddr() {
        return new StringBuilder().append(address1.getValue()).append(address2.getValue()).append(addr3).toString();
    }

    public void setCompleteAddr(String completeAddr) {
        this.completeAddr = completeAddr;
    }

    public Address1 getAddress1() {
        return address1;
    }

    public void setAddress1(Address1 address1) {
        this.address1 = address1;
    }

    public Address2 getAddress2() {
        return address2;
    }

    public void setAddress2(Address2 address2) {
        this.address2 = address2;
    }
}
