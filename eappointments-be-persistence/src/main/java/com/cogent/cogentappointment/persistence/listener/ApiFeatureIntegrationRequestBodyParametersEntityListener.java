package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ApiFeatureIntegrationRequestBodyParametersHistory;
import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak ON 2020/05/29-11:37 AM
 */
public class ApiFeatureIntegrationRequestBodyParametersEntityListener {

    @PrePersist
    public void prePersist(ApiFeatureIntegrationRequestBodyParameters target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ApiFeatureIntegrationRequestBodyParameters target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ApiFeatureIntegrationRequestBodyParameters target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ApiFeatureIntegrationRequestBodyParameters target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ApiFeatureIntegrationRequestBodyParametersHistory(target, action));
    }
}
