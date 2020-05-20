package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalDepartmentChargeHistory;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentCharge;
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
public class HospitalDepartmentChargeEntityListener {
    @PrePersist
    public void prePersist(HospitalDepartmentCharge target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalDepartmentCharge target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalDepartmentCharge target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalDepartmentCharge target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalDepartmentChargeHistory(target, action));
    }
}
