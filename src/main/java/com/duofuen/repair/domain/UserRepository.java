package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByOpenId(String openId);

    User findByPhoneNum(String phoneNum);
}
