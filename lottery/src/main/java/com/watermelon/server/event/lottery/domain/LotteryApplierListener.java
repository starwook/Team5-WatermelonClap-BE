package com.watermelon.server.event.lottery.domain;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LotteryApplierListener {

    @PrePersist
    public void prePersist(LotteryApplier entity) {
        log.debug("Pre Persist: {}", entity.toString());
    }

    @PreUpdate
    public void preUpdate(LotteryApplier entity) {
        log.debug("Pre Update: {}", entity.toString());
    }

    @PostLoad
    public void postLoad(LotteryApplier entity) {
        log.debug("Post Load: {}", entity.toString());
    }

}
