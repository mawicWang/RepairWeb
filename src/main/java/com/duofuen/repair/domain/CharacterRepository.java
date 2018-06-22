package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface CharacterRepository extends CrudRepository<Character, Integer> {

    Character findByOpenId(String openId);

    Character findByPhoneNum(String phoneNum);
}
