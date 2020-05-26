package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminModeApiFeatureIntegrationHistory;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-24
 */
public class AdminModeApiFeatureIntegrationEntityListener {

    @PrePersist
    public void prePersist(AdminModeApiFeatureIntegration target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminModeApiFeatureIntegration target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminModeApiFeatureIntegration target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminModeApiFeatureIntegration target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminModeApiFeatureIntegrationHistory(target, action));
    }
    
}
