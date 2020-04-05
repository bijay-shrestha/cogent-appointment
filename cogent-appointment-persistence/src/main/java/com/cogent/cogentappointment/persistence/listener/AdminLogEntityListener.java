package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminLogHistory;
import com.cogent.cogentappointment.persistence.model.AdminLog;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Rupak
 */
public class AdminLogEntityListener {

    @PrePersist
    public void prePersist(AdminLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminLogHistory(target, action));
    }
}
