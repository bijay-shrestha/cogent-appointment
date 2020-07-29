package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.BillingModeHistory;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
public class BillingModeListener {

    @PrePersist
    public void prePersist(BillingMode target) { perform(target, INSERTED); }

    @PreUpdate
    public void preUpdate(BillingMode target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(BillingMode target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(BillingMode target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new BillingModeHistory(target, action));
    }
}
