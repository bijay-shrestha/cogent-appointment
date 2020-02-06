
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.NationalityHistory;
import com.cogent.cogentappointment.persistence.model.Nationality;
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
public class NationalityEntityListener {
    @PrePersist
    public void prePersist(Nationality target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Nationality target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Nationality target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Nationality target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new NationalityHistory(target, action));
    }
}
