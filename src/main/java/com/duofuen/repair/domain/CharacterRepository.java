package com.duofuen.repair.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends CrudRepository<Character, Integer> {

    Optional<Character> findByIdAndEnabledTrue(Integer integer);

    Character findByOpenIdAndEnabledTrue(String openId);

    Character findByPhoneNumAndEnabledTrue(String phoneNum);

    List<Character> findAllByRoleCode(String roleCode);

    @Query("select c from Character c, CharaAddr2 ca2, Address2 a2, Store st " +
            "where c.id = ca2.id.characterId and a2.id = ca2.id.addr2Id and st.addr2Id = a2.id " +
            "and st.id = ?1 and c.roleCode = ?2 and c.enabled = true")
    Character findByStoreIdAndRoleCodeAndEnabledTrue(Integer storeId, String roleCode);

    @Query("select distinct c from Character c, CharaAddr2 ca2, Address2 a2, Address1 a1 " +
            "where c.id = ca2.id.characterId and a2.id = ca2.id.addr2Id and a2.addr1Id = ?1 " +
            "and c.roleCode = ?2 and c.enabled = true")
    List<Character> findByAddress1IdAndEnabledTrue(Integer address1Id, String roleCode);

}
