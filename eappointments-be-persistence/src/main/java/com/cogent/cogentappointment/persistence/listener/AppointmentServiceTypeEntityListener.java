
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminServiceTypeHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 26/05/20
 */
public class AppointmentServiceTypeEntityListener {
    @PrePersist
    public void prePersist(AppointmentServiceType target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentServiceType target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentServiceType target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentServiceType target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminServiceTypeHistory(target, action));
    }
}
