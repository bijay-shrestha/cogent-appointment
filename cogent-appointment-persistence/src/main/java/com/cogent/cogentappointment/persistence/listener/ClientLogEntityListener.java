package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ClientLogHistory;
import com.cogent.cogentappointment.persistence.model.ClientLog;
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
public class ClientLogEntityListener {

    @PrePersist
    public void prePersist(ClientLog target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ClientLog target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ClientLog target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ClientLog target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ClientLogHistory(target, action));
    }
}
