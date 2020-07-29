package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorHistory;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorEntityListener {

    @PrePersist
    public void prePersist(Doctor target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Doctor target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Doctor target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Doctor target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorHistory(target, action));
    }
}

