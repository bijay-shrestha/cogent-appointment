package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminConfirmationTokenHistory;
import com.cogent.cogentappointment.persistence.model.AdminConfirmationToken;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AdminConfirmationTokenEntityListener {

    @PrePersist
    public void prePersist(AdminConfirmationToken target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminConfirmationToken target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminConfirmationToken target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminConfirmationToken target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminConfirmationTokenHistory(target, action));
    }
}

