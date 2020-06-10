package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalDepartmentWeekDaysDutyRosterHistory;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HospitalDepartmentWeekDaysDutyRosterEntityListener {

    @PrePersist
    public void prePersist(HospitalDepartmentWeekDaysDutyRoster target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalDepartmentWeekDaysDutyRoster target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalDepartmentWeekDaysDutyRoster target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalDepartmentWeekDaysDutyRoster target,
                        Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalDepartmentWeekDaysDutyRosterHistory(target, action));
    }
}

