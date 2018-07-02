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
    @Transient
    private Character character;

    public Address2() {
    }

    public Address2(Address2 address2, Character character) {
        this.id = address2.getId();
        this.addr1Id = address2.addr1Id;
        this.value = address2.value;
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address2 address2 = (Address2) o;

        return id.equals(address2.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
