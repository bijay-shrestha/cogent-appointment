package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.PatientMetaInfoHistory;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class PatientMetaInfoEntityListener {

    @PrePersist
    public void prePersist(PatientMetaInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(PatientMetaInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(PatientMetaInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(PatientMetaInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new PatientMetaInfoHistory(target, action));
    }
}

