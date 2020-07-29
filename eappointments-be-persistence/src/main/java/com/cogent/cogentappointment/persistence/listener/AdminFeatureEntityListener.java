
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminFeatureHistory;
import com.cogent.cogentappointment.persistence.model.AdminFeature;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 18/04/20
 */
public class AdminFeatureEntityListener {
    @PrePersist
    public void prePersist(AdminFeature target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminFeature target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminFeature target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminFeature target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminFeatureHistory(target, action));
    }
}
