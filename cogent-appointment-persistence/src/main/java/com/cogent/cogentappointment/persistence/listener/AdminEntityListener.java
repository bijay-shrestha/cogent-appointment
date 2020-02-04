package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminHistory;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.util.BeanUtil;


import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AdminEntityListener {

    @PrePersist
    public void prePersist(Admin target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Admin target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Admin target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Admin target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminHistory(target, action));
    }
}

