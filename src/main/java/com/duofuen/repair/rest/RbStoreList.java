package com.duofuen.repair.rest;

import java.util.List;

public class RbStoreList extends BaseResultBody {

    private List<Store> storeList;

    public Store giveOneStore(){
        return new Store();
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }

    public class Store {
        private Integer storeId;
        private String storeName;
        private String storeAddr;

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
    }
}
