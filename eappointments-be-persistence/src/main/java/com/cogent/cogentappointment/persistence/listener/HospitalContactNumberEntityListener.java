package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalContactNumberHistory;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HospitalContactNumberEntityListener {

    @PrePersist
    public void prePersist(HospitalContactNumber target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalContactNumber target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalContactNumber target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalContactNumber target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalContactNumberHistory(target, action));
    }
}

