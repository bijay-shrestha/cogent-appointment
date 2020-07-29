package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.FavouriteHistory;
import com.cogent.cogentappointment.persistence.model.Favourite;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak ON 2020/06/16-11:56 AM
 */
public class FavouriteEntityListener {

    @PrePersist
    public void prePersist(Favourite target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Favourite target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Favourite target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Favourite target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new FavouriteHistory(target, action));
    }
}
