
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminAvatarHistory;
import com.cogent.cogentappointment.persistence.model.AdminAvatar;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.*;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class AdminAvatarEntityListener {
    @PrePersist
    public void prePersist(AdminAvatar target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminAvatar target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminAvatar target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminAvatar target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminAvatarHistory(target, action));
    }
}
