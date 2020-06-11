package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ApiIntegrationRequestBodyParametersHistory;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationRequestBodyParameters;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak ON 5/28/20
 */
public class ApiIntegrationRequestBodyParametersEntityListener {

    @PrePersist
    public void prePersist(ApiIntegrationRequestBodyParameters target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ApiIntegrationRequestBodyParameters target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ApiIntegrationRequestBodyParameters target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ApiIntegrationRequestBodyParameters target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ApiIntegrationRequestBodyParametersHistory(target, action));
    }

}
