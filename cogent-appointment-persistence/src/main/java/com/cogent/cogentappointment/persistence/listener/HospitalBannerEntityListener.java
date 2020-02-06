package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalBannerHistory;
import com.cogent.cogentappointment.persistence.model.HospitalBanner;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HospitalBannerEntityListener {

    @PrePersist
    public void prePersist(HospitalBanner target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalBanner target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalBanner target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalBanner target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalBannerHistory(target, action));
    }
}

