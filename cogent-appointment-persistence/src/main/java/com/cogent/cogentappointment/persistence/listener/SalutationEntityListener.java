package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.SalutationHistory;
import com.cogent.cogentappointment.persistence.model.Salutation;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class SalutationEntityListener {
    @PrePersist
    public void prePersist(Salutation target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Salutation target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Salutation target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Salutation target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new SalutationHistory(target, action));
    }
}
