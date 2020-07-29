package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentHistory;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AppointmentEntityListener {

    @PrePersist
    public void prePersist(Appointment target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Appointment target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Appointment target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Appointment target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentHistory(target, action));
    }
}

