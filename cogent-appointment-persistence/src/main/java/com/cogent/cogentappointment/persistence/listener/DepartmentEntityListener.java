package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminConfirmationTokenHistory;
import com.cogent.cogentappointment.persistence.history.DepartmentHistory;
import com.cogent.cogentappointment.persistence.model.AdminConfirmationToken;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class DepartmentEntityListener {

    @PrePersist
    public void prePersist(Department target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Department target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Department target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Department target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new DepartmentHistory(target, action));
    }
}

