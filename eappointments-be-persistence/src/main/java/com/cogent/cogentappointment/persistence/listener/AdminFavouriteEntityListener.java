package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.AdminFavouriteHistory;
import com.cogent.cogentappointment.persistence.model.AdminFavourite;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak ON 2020/06/16-12:02 PM
 */
public class AdminFavouriteEntityListener {

    @PrePersist
    public void prePersist(AdminFavourite target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(AdminFavourite target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(AdminFavourite target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(AdminFavourite target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new AdminFavouriteHistory(target, action));
    }

}
