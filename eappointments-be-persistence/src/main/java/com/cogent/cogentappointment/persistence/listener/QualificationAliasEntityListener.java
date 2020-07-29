
package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.QualificationAliasHistory;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
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
public class QualificationAliasEntityListener {
    @PrePersist
    public void prePersist(QualificationAlias target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(QualificationAlias target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(QualificationAlias target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(QualificationAlias target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new QualificationAliasHistory(target, action));
    }
}
