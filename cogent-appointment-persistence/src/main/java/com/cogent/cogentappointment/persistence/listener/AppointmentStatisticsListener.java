package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AppointmentStatisticsHistory;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpLog;
import com.cogent.cogentappointment.persistence.model.AppointmentStatistics;
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

/**
 * @author Sauravi Thapa ON 4/16/20
 */
public class AppointmentStatisticsListener {

    @PrePersist
    public void prePersist(AppointmentStatistics target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AppointmentStatistics target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AppointmentStatistics target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AppointmentStatistics target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AppointmentStatisticsHistory(target, action));
    }
}
