
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalPatientInfoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti ON 10/02/2020
 */
public class HospitalPatientInfoEntityListener {
    @PrePersist
    public void prePersist(HospitalPatientInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalPatientInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalPatientInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalPatientInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalPatientInfoHistory(target, action));
    }
}
