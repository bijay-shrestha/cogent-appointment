package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.DoctorAvatarHistory;
import com.cogent.cogentappointment.persistence.model.DoctorAvatar;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DoctorAvatarEntityListener {

    @PrePersist
    public void prePersist(DoctorAvatar target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(DoctorAvatar target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(DoctorAvatar target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(DoctorAvatar target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DoctorAvatarHistory(target, action));
    }
}

