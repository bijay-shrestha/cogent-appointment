package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ApiQueryParametersHistory;
import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-19
 */
public class ApiQueryParametersEntityListener {

    @PrePersist
    public void prePersist(ApiQueryParameters target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ApiQueryParameters target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ApiQueryParameters target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ApiQueryParameters target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ApiQueryParametersHistory(target, action));
    }
}
