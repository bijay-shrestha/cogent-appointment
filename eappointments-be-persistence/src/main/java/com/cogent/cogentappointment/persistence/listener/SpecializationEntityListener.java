
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.SpecializationHistory;
import com.cogent.cogentappointment.persistence.model.Specialization;
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
public class SpecializationEntityListener {
    @PrePersist
    public void prePersist(Specialization target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Specialization target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Specialization target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Specialization target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new SpecializationHistory(target, action));
    }
}
