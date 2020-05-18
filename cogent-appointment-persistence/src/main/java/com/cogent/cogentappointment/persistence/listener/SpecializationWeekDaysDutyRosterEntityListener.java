package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.SpecializationWeekDaysDutyRosterHistory;
import com.cogent.cogentappointment.persistence.model.SpecializationWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class SpecializationWeekDaysDutyRosterEntityListener {

    @PrePersist
    public void prePersist(SpecializationWeekDaysDutyRoster target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(SpecializationWeekDaysDutyRoster target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(SpecializationWeekDaysDutyRoster target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(SpecializationWeekDaysDutyRoster target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new SpecializationWeekDaysDutyRosterHistory(target, action));
    }
}

