package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HttpRequestMethodHistory;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
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
public class HttpRequestMethodEntityListener {

    @PrePersist
    public void prePersist(HttpRequestMethod target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HttpRequestMethod target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HttpRequestMethod target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HttpRequestMethod target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HttpRequestMethodHistory(target, action));
    }
}
