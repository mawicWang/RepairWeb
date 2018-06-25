package com.duofuen.repair.rest.order;

import com.duofuen.repair.rest.BaseResultBody;

import java.util.List;

public class RbOrderList extends BaseResultBody {

    private List<Order> orderList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public Order giveNewOrder() {
        return new Order();
    }

    public class Order {

        private Integer orderId;
        private Integer storeId;
        private String storeName;
        private String storeAddr;
        private Integer managerId ;
        private Integer repairmanId;
        private String title;
        private String orderState;
        private String createTime;

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreAddr() {
            return storeAddr;
        }

        public void setStoreAddr(String storeAddr) {
            this.storeAddr = storeAddr;
        }

        public Integer getManagerId() {
            return managerId;
        }

        public void setManagerId(Integer managerId) {
            this.managerId = managerId;
        }

        public Integer getRepairmanId() {
            return repairmanId;
        }

        public void setRepairmanId(Integer repairmanId) {
            this.repairmanId = repairmanId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }


}
