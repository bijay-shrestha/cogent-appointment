package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ForgotPasswordVerificationHistory;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class ForgotPasswordVerificationEntityListener {

    @PrePersist
    public void prePersist(ForgotPasswordVerification target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ForgotPasswordVerification target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ForgotPasswordVerification target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ForgotPasswordVerification target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ForgotPasswordVerificationHistory(target, action));
    }
}

