package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentTransactionRequestLogHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionRequestLog;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 29/03/20
 */
public class AppointmentTransactionRequestLogEntityListener {

    @PrePersist
    public void prePersist(AppointmentTransactionRequestLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentTransactionRequestLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentTransactionRequestLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentTransactionRequestLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentTransactionRequestLogHistory(target, action));
    }
}

