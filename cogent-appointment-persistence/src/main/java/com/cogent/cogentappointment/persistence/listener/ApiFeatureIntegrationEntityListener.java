package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ApiFeatureIntegrationHistory;
import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegration;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-18
 */
public class ApiFeatureIntegrationEntityListener {

    @PrePersist
    public void prePersist(ApiFeatureIntegration target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ApiFeatureIntegration target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ApiFeatureIntegration target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ApiFeatureIntegration target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ApiFeatureIntegrationHistory(target, action));
    }
}
