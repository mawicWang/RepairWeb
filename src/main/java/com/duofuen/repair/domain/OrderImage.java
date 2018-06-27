package com.duofuen.repair.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class OrderImage {

    @EmbeddedId
    private OrderImagePK id;

    public OrderImagePK getId() {
        return id;
    }

    public void setId(OrderImagePK id) {
        this.id = id;
    }
}
