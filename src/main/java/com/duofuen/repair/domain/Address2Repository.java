package com.duofuen.repair.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Address2Repository extends CrudRepository<Address2, Integer> {

    Iterable<Address2> findAllByAddr1Id(Integer addr1Id);


    @Query("select distinct new Address2(a2,c) from Address2 a2, CharaAddr2 ca2, Character c " +
            "where a2.id = ca2.id.addr2Id and ca2.id.characterId = c.id " +
            "and c.id = ?1")
    List<Address2> findAllByCharacterId(Integer characterId);

    @Query("select distinct new Address2(a2,c) from Address2 a2, CharaAddr2 ca2, Character c " +
            "where a2.id = ca2.id.addr2Id and ca2.id.characterId = c.id " +
            "and c.id <> ?1 and c.roleCode = ?2 ")
    List<Address2> findAllByCharacterIdExcluded(Integer characterId,String roleCode);
}
