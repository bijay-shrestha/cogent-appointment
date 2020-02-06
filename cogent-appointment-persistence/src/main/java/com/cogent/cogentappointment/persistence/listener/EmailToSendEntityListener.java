package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.EmailToSendHistory;
import com.cogent.cogentappointment.persistence.model.EmailToSend;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class EmailToSendEntityListener {

    @PrePersist
    public void prePersist(EmailToSend target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(EmailToSend target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(EmailToSend target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(EmailToSend target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new EmailToSendHistory(target, action));
    }
}

