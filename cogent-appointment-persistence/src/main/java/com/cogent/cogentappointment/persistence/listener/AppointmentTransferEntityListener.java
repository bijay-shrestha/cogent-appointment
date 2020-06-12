package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentTransferHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentTransfer;
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
public class AppointmentTransferEntityListener {
    @PrePersist
    public void prePersist(AppointmentTransfer target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentTransfer target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentTransfer target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentTransfer target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentTransferHistory(target, action));
    }
}
