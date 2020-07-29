package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminMacAddressInfoHistory;
import com.cogent.cogentappointment.persistence.model.AdminMacAddressInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AdminMacAddressInfoEntityListener {

    @PrePersist
    public void prePersist(AdminMacAddressInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminMacAddressInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminMacAddressInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminMacAddressInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminMacAddressInfoHistory(target, action));
    }
}

