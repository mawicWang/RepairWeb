package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface RestTokenRepository extends CrudRepository<RestToken, Integer> {

    RestToken findByToken(String token);

    RestToken findByCharacterId(Integer id);
}
