
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.QualificationHistory;
import com.cogent.cogentappointment.persistence.model.Qualification;
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
public class QualificationEntityListener {
    @PrePersist
    public void prePersist(Qualification target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Qualification target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Qualification target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Qualification target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new QualificationHistory(target, action));
    }
}
