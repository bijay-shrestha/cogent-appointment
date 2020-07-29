package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HmacApiInfoHistory;
import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class HmacApiInfoEntityListener {

    @PrePersist
    public void prePersist(HmacApiInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HmacApiInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HmacApiInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HmacApiInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HmacApiInfoHistory(target, action));
    }
}

