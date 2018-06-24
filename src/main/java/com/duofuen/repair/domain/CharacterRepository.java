package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.util.StringUtils;

import java.util.List;

public interface CharacterRepository extends CrudRepository<Character, Integer> {

    Character findByOpenId(String openId);

    Character findByPhoneNum(String phoneNum);

    List<Character> findAllByRoleCode(String roleCode);
}
