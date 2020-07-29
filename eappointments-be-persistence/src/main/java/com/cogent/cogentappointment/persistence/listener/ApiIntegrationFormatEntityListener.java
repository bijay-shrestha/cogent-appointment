package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ApiIntegrationFormatHistory;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
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
public class ApiIntegrationFormatEntityListener {

    @PrePersist
    public void prePersist(ApiIntegrationFormat target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ApiIntegrationFormat target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ApiIntegrationFormat target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ApiIntegrationFormat target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ApiIntegrationFormatHistory(target, action));
    }

}
