package com.duofuen.repair.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CharaAddr2 {
    @EmbeddedId
    private CharaAddr2PK id;

    public CharaAddr2PK getId() {
        return id;
    }

    public CharaAddr2() {
    }

    public CharaAddr2(Integer characterId, Integer addr2Id) {
        CharaAddr2PK id = new CharaAddr2PK();
        id.setCharacterId(characterId);
        id.setAddr2Id(addr2Id);

        this.id = id;
    }

    public void setId(CharaAddr2PK id) {
        this.id = id;
    }
}
