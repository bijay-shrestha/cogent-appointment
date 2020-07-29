package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminDashboardFeatureHistory;
import com.cogent.cogentappointment.persistence.history.AdminHistory;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
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
public class AdminDashboardFeatureEntityListener {

    @PrePersist
    public void prePersist(AdminDashboardFeature target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminDashboardFeature target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminDashboardFeature target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminDashboardFeature target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminDashboardFeatureHistory(target, action));
    }
}
