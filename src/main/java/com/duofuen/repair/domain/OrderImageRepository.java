package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderImageRepository extends CrudRepository<OrderImage, OrderImagePK> {

    List<OrderImage> findAllById_OrderId(Integer orderId);
}
