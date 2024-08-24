package com.watermelon.server;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseEntityListener {

    @PrePersist
    public void prePersist(BaseEntity entity) {
        log.debug("Pre Persist: {}", entity.toString());
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        log.debug("Pre Update: {}", entity.toString());
    }

    @PostLoad
    public void postLoad(BaseEntity entity) {
        log.debug("Post Load: {}", entity.toString());
    }

}
