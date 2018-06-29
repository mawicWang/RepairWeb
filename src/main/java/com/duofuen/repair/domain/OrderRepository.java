package com.duofuen.repair.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    Page<Order> findAllByManagerIdOrderByCreateTimeDesc(Integer managerId, Pageable pageable);

    Page<Order> findAllByRepairmanIdOrderByCreateTimeDesc(Integer repairmanId, Pageable pageable);

}
