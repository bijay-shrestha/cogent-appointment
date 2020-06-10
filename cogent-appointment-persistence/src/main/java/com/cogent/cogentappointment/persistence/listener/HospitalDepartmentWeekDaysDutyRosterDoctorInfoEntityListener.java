package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalDepartmentWeekDaysDutyRosterDoctorInfoHistory;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRosterDoctorInfo;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 05/06/20
 */
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoEntityListener {

    @PrePersist
    public void prePersist(HospitalDepartmentWeekDaysDutyRosterDoctorInfo target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalDepartmentWeekDaysDutyRosterDoctorInfo target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalDepartmentWeekDaysDutyRosterDoctorInfo target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalDepartmentWeekDaysDutyRosterDoctorInfo target,
                        Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalDepartmentWeekDaysDutyRosterDoctorInfoHistory(target, action));
    }
}
