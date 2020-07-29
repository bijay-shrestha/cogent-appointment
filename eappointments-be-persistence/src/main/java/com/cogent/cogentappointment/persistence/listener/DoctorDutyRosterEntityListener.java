package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorDutyRosterHistory;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorDutyRosterEntityListener {

    @PrePersist
    public void prePersist(DoctorDutyRoster target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorDutyRoster target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorDutyRoster target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorDutyRoster target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorDutyRosterHistory(target, action));
    }
}

