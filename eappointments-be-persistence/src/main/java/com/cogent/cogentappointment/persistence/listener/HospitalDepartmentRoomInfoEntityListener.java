package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalDepartmentRoomInfoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public class HospitalDepartmentRoomInfoEntityListener {
    @PrePersist
    public void prePersist(HospitalDepartmentRoomInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalDepartmentRoomInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalDepartmentRoomInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalDepartmentRoomInfo target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalDepartmentRoomInfoHistory(target, action));
    }
}
