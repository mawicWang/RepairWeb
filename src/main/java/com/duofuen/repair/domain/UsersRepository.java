package com.duofuen.repair.domain;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, String> {

    Users findByAuthorities_Authority(String authority);
}
