package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentReservationHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentReservation;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AppointmentReservationEntityListener {

    @PrePersist
    public void prePersist(AppointmentReservation target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentReservation target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentReservation target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentReservation target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentReservationHistory(target, action));
    }
}

