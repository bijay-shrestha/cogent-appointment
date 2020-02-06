
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.PatientHistory;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa २०/२/५
 */
public class PatientEntityListener {
    @PrePersist
    public void prePersist(Patient target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Patient target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Patient target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Patient target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new PatientHistory(target, action));
    }
}
