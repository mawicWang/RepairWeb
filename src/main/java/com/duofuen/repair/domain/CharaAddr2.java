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

    public void setId(CharaAddr2PK id) {
        this.id = id;
    }
}
