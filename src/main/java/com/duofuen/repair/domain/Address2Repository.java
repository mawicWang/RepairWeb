package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface Address2Repository extends CrudRepository<Address2, Integer> {

    Iterable<Address2> findAllByAddr1Id(Integer addr1Id);
}
