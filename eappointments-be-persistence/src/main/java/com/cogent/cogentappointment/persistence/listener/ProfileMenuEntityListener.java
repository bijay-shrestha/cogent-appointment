
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ProfileMenuHistory;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;
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
public class ProfileMenuEntityListener {
    @PrePersist
    public void prePersist(ProfileMenu target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ProfileMenu target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ProfileMenu target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ProfileMenu target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ProfileMenuHistory(target, action));
    }
}
