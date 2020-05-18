package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.SpecializationDutyRosterOverrideHistory;
import com.cogent.cogentappointment.persistence.model.SpecializationDutyRosterOverride;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class SpecializationDutyRosterOverrideEntityListener {

    @PrePersist
    public void prePersist(SpecializationDutyRosterOverride target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(SpecializationDutyRosterOverride target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(SpecializationDutyRosterOverride target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(SpecializationDutyRosterOverride target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new SpecializationDutyRosterOverrideHistory(target, action));
    }
}

