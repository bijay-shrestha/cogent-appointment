package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalBillingModeInfoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalBillingModeInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.DELETED;
import static com.cogent.cogentappointment.persistence.config.Action.INSERTED;
import static com.cogent.cogentappointment.persistence.config.Action.UPDATED;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
public class HospitalBillingModeInfoEntityListener {
    @PrePersist
    public void prePersist(HospitalBillingModeInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalBillingModeInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalBillingModeInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalBillingModeInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalBillingModeInfoHistory(target, action));
    }
}
