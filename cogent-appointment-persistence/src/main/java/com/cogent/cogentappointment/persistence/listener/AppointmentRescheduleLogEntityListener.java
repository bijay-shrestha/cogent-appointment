package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentRescheduleLogHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentRescheduleLog;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti ON 12/02/2020
 */
public class AppointmentRescheduleLogEntityListener {

    @PrePersist
    public void prePersist(AppointmentRescheduleLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentRescheduleLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentRescheduleLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentRescheduleLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentRescheduleLogHistory(target, action));
    }
}

