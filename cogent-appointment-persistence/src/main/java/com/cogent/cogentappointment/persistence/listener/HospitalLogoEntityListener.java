package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalLogoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HospitalLogoEntityListener {

    @PrePersist
    public void prePersist(HospitalLogo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalLogo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalLogo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalLogo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalLogoHistory(target, action));
    }
}

