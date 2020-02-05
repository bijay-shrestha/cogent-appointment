package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminMetaInfoHistory;
import com.cogent.cogentappointment.persistence.model.AdminMetaInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AdminMetaInfoEntityListener {

    @PrePersist
    public void prePersist(AdminMetaInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminMetaInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminMetaInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminMetaInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminMetaInfoHistory(target, action));
    }
}

