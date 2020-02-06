
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ProfileHistory;
import com.cogent.cogentappointment.persistence.model.Profile;
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
public class ProfileEntityListener {
    @PrePersist
    public void prePersist(Profile target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Profile target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Profile target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Profile target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ProfileHistory(target, action));
    }
}
