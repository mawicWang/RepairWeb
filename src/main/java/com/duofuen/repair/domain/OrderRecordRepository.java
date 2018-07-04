package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface OrderRecordRepository extends CrudRepository<OrderRecord, Integer> {

    Iterable<OrderRecord> findAllByOrderId(Integer orderId);
}
