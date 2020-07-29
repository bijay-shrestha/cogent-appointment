package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorSpecializationHistory;
import com.cogent.cogentappointment.persistence.model.DoctorSpecialization;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorSpecializationEntityListener {

    @PrePersist
    public void prePersist(DoctorSpecialization target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorSpecialization target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorSpecialization target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorSpecialization target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorSpecializationHistory(target, action));
    }
}

