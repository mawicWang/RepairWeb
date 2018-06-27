package com.duofuen.repair.rest.order;

import com.duofuen.repair.rest.BaseResultBody;

public class RbOrderId  extends BaseResultBody {

    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public RbOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
