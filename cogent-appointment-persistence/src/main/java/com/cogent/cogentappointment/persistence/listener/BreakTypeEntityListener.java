package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.BreakTypeHistory;
import com.cogent.cogentappointment.persistence.model.BreakType;
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
public class BreakTypeEntityListener {

    @PrePersist
    public void prePersist(BreakType target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(BreakType target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(BreakType target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(BreakType target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new BreakTypeHistory(target, action));
    }
}

