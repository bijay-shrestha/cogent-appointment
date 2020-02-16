package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentFollowUpLogHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpLog;
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
public class AppointmentFollowUpLogEntityListener {

    @PrePersist
    public void prePersist(AppointmentFollowUpLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentFollowUpLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentFollowUpLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentFollowUpLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentFollowUpLogHistory(target, action));
    }
}

