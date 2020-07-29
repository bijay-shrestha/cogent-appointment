package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentModeHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 4/16/20
 */
public class AppointmentModeListener {

    @PrePersist
    public void prePersist(AppointmentMode target) { perform(target, INSERTED); }

    @PreUpdate
    public void preUpdate(AppointmentMode target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentMode target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentMode target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentModeHistory(target, action));
    }
}
