package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorAppointmentChargeHistory;
import com.cogent.cogentappointment.persistence.model.DoctorAppointmentCharge;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorAppointmentChargeEntityListener {

    @PrePersist
    public void prePersist(DoctorAppointmentCharge target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorAppointmentCharge target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorAppointmentCharge target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorAppointmentCharge target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorAppointmentChargeHistory(target, action));
    }
}

