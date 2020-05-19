package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.RoomHistory;
import com.cogent.cogentappointment.persistence.model.Room;
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
 * @author Sauravi Thapa ON 5/19/20
 */
public class RoomEntityListener {
    @PrePersist
    public void prePersist(Room target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Room target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Room target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Room target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new RoomHistory(target, action));
    }
}
