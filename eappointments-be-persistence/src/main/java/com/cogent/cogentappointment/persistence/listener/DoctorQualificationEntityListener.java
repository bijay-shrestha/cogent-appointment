package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorQualificationHistory;
import com.cogent.cogentappointment.persistence.model.DoctorQualification;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorQualificationEntityListener {

    @PrePersist
    public void prePersist(DoctorQualification target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorQualification target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorQualification target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorQualification target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorQualificationHistory(target, action));
    }
}

