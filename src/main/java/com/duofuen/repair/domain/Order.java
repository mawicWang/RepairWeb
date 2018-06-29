package com.duofuen.repair.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "store_id")
    private Integer storeId;
    @Column(name = "manager_id")
    private Integer managerId;
    @Column(name = "repairman_id")
    private Integer repairmanId;
    private String title;
    @Column(name = "descp")
    private String desc;
    private String orderState;
    private Date createTime;
    private Date finishTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Character manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repairman_id", insertable = false, updatable = false)
    private Character repairman;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Character getManager() {
        return manager;
    }

    public void setManager(Character manager) {
        this.manager = manager;
    }

    public Character getRepairman() {
        return repairman;
    }

    public void setRepairman(Character repairman) {
        this.repairman = repairman;
    }
}
