package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentRefundDetailHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti ON 06/02/2020
 */
public class AppointmentRefundDetailEntityListener {

    @PrePersist
    public void prePersist(AppointmentRefundDetail target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentRefundDetail target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentRefundDetail target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentRefundDetail target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentRefundDetailHistory(target, action));
    }
}
