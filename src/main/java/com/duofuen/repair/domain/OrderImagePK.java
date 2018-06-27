package com.duofuen.repair.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OrderImagePK implements Serializable {
    private Integer orderId;
    private Integer imageId;

    public OrderImagePK() {
    }

    public OrderImagePK(Integer orderId, Integer imageId) {
        this.orderId = orderId;
        this.imageId = imageId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
