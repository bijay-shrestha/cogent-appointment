package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorShiftHistory;
import com.cogent.cogentappointment.persistence.model.DoctorShift;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 06/05/20
 */
public class DoctorShiftEntityListener {

    @PrePersist
    public void prePersist(DoctorShift target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorShift target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorShift target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorShift target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorShiftHistory(target, action));
    }
}

