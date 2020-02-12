package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentRescheduleHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentReschedule;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Rupak
 */
public class AppointmentRescheduleEntityListener {
    @PrePersist
    public void prePersist(AppointmentReschedule target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentReschedule target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentReschedule target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentReschedule target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentRescheduleHistory(target, action));
    }


}
