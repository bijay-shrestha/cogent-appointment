package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorWeekDaysDutyRosterHistory;
import com.cogent.cogentappointment.persistence.model.DoctorWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorWeekDaysDutyRosterEntityListener {

    @PrePersist
    public void prePersist(DoctorWeekDaysDutyRoster target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorWeekDaysDutyRoster target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorWeekDaysDutyRoster target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorWeekDaysDutyRoster target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorWeekDaysDutyRosterHistory(target, action));
    }
}

