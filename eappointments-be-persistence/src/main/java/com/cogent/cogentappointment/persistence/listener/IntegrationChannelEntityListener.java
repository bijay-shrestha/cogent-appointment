package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.IntegrationChannelHistory;
import com.cogent.cogentappointment.persistence.model.IntegrationChannel;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-27
 */
public class IntegrationChannelEntityListener {

    @PrePersist
    public void prePersist(IntegrationChannel target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(IntegrationChannel target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(IntegrationChannel target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(IntegrationChannel target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new IntegrationChannelHistory(target, action));
    }


}
