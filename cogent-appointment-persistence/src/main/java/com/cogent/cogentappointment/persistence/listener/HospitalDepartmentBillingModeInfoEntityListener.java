package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalDepartmentBillingModeInfoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public class HospitalDepartmentBillingModeInfoEntityListener {
    @PrePersist
    public void prePersist(HospitalDepartmentBillingModeInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalDepartmentBillingModeInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalDepartmentBillingModeInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalDepartmentBillingModeInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalDepartmentBillingModeInfoHistory(target, action));
    }
}
