package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorSalutationHistory;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorSalutationEntityListener {

    @PrePersist
    public void prePersist(DoctorSalutation target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorSalutation target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorSalutation target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorSalutation target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorSalutationHistory(target, action));
    }
}
