package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalHistory;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HospitalEntityListener {

    @PrePersist
    public void prePersist(Hospital target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Hospital target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Hospital target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Hospital target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalHistory(target, action));
    }
}

