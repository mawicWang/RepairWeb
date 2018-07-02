package com.duofuen.repair.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Address1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr1_id")
    private List<Address2> listAddress2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Address2> getListAddress2() {
        return listAddress2;
    }

    public void setListAddress2(List<Address2> listAddress2) {
        this.listAddress2 = listAddress2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address1 address1 = (Address1) o;

        return id.equals(address1.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
