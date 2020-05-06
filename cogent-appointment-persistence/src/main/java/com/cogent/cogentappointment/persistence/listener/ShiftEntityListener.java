package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ShiftHistory;
import com.cogent.cogentappointment.persistence.model.Shift;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 06/05/20
 */
public class ShiftEntityListener {

    @PrePersist
    public void prePersist(Shift target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Shift target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Shift target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Shift target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ShiftHistory(target, action));
    }
}

