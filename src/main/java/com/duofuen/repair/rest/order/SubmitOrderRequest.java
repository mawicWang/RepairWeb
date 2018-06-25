package com.duofuen.repair.rest.order;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SubmitOrderRequest {
    @NotNull(message = "managerId must not be empty")
    private Integer managerId;
    @NotNull(message = "storeId must not be empty")
    private Integer storeId;
    @NotEmpty(message = "title must not be empty")
    private String title;
    private String desc;
    private List<Integer> imgs;

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Integer> getImgs() {
        return imgs;
    }

    public void setImgs(List<Integer> imgs) {
        this.imgs = imgs;
    }
}
