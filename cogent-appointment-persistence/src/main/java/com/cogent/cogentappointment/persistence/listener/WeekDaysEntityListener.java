package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminAvatarHistory;
import com.cogent.cogentappointment.persistence.history.WeekDaysHistory;
import com.cogent.cogentappointment.persistence.model.AdminAvatar;
import com.cogent.cogentappointment.persistence.model.WeekDays;
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
public class WeekDaysEntityListener {

    @PrePersist
    public void prePersist(WeekDays target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(WeekDays target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(WeekDays target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(WeekDays target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new WeekDaysHistory(target, action));
    }
}
