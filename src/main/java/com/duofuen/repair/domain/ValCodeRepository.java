package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface ValCodeRepository extends CrudRepository<ValCode, String> {

    ValCode findByPhoneNum(String phoneNum);
}
