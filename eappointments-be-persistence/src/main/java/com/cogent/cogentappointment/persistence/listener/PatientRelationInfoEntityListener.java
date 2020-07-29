
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.PatientRelationInfoHistory;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 26/02/20
 */
public class PatientRelationInfoEntityListener {
    @PrePersist
    public void prePersist(PatientRelationInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(PatientRelationInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(PatientRelationInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(PatientRelationInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new PatientRelationInfoHistory(target, action));
    }
}
