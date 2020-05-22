package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentTransferTransactionRequestLogHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentTransferTransactionRequestLog;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferTransactionRequestLogEntityListener {

    @PrePersist
    public void prePersist(AppointmentTransferTransactionRequestLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentTransferTransactionRequestLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentTransferTransactionRequestLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentTransferTransactionRequestLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentTransferTransactionRequestLogHistory(target, action));
    }
}

