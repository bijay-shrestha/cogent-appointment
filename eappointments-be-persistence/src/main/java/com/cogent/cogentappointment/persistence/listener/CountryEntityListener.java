package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.CountryHistory;
import com.cogent.cogentappointment.persistence.model.Country;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class CountryEntityListener {

    @PrePersist
    public void prePersist(Country target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Country target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Country target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Country target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new CountryHistory(target, action));
    }
}

