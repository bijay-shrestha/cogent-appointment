package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminAvatarHistory;
import com.cogent.cogentappointment.persistence.history.ThirdPartyInfoHistory;
import com.cogent.cogentappointment.persistence.model.AdminAvatar;
import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.DELETED;
import static com.cogent.cogentappointment.persistence.config.Action.INSERTED;
import static com.cogent.cogentappointment.persistence.config.Action.UPDATED;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class ThirdPartyInfoEntityListener {
    @PrePersist
    public void prePersist(ThirdPartyInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ThirdPartyInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ThirdPartyInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ThirdPartyInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ThirdPartyInfoHistory (target, action));
    }
}
