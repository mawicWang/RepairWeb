package com.duofuen.repair.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CharaAddr2PK implements Serializable {
    private Integer characterId;
    private Integer addr2Id;

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public Integer getAddr2Id() {
        return addr2Id;
    }

    public void setAddr2Id(Integer addr2Id) {
        this.addr2Id = addr2Id;
    }
}
