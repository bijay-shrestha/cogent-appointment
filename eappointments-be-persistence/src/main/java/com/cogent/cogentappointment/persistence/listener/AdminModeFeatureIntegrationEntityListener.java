package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminModeFeatureIntegrationHistory;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-21
 */
public class AdminModeFeatureIntegrationEntityListener {

    @PrePersist
    public void prePersist(AdminModeFeatureIntegration target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminModeFeatureIntegration target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminModeFeatureIntegration target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminModeFeatureIntegration target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminModeFeatureIntegrationHistory(target, action));
    }
    
}
