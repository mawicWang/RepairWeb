package com.duofuen.repair.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends CrudRepository<Store, Integer> {

    @Query("select s as completeAddr from Store s, Address2 a2, CharaAddr2 ca2, Character c " +
            "where s.addr2Id = a2.id and ca2.id.addr2Id = a2.id and ca2.id.characterId = c.id " +
            "and c.id = ?1 and c.roleCode = ?2")
    List<Store> findAllByUserId(Integer userId, String roleCode);

//    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Store> findById(Integer id);
}
