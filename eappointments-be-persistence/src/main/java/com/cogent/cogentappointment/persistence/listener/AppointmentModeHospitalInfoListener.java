package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentModeHospitalInfoHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentModeHospitalInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-22
 */
public class AppointmentModeHospitalInfoListener {

    @PrePersist
    public void prePersist(AppointmentModeHospitalInfo target) { perform(target, INSERTED); }

    @PreUpdate
    public void preUpdate(AppointmentModeHospitalInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentModeHospitalInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentModeHospitalInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentModeHospitalInfoHistory(target, action));
    }
}
