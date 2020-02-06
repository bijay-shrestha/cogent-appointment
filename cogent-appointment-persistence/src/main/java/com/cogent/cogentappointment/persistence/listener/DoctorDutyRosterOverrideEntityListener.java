package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorDutyRosterOverrideHistory;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorDutyRosterOverrideEntityListener {

    @PrePersist
    public void prePersist(DoctorDutyRosterOverride target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorDutyRosterOverride target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorDutyRosterOverride target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorDutyRosterOverride target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorDutyRosterOverrideHistory(target, action));
    }
}

