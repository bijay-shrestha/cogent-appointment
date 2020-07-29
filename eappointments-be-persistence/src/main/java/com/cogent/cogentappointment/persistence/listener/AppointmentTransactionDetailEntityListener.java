package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentTransactionDetailHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AppointmentTransactionDetailEntityListener {

    @PrePersist
    public void prePersist(AppointmentTransactionDetail target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentTransactionDetail target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentTransactionDetail target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentTransactionDetail target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentTransactionDetailHistory(target, action));
    }
}

