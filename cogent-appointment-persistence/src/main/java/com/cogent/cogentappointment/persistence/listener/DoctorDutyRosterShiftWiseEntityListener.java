package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorDutyRosterShiftWiseHistory;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 08/05/20
 */
public class DoctorDutyRosterShiftWiseEntityListener {

    @PrePersist
    public void prePersist(DoctorDutyRosterShiftWise target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorDutyRosterShiftWise target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorDutyRosterShiftWise target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorDutyRosterShiftWise target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorDutyRosterShiftWiseHistory(target, action));
    }
}

