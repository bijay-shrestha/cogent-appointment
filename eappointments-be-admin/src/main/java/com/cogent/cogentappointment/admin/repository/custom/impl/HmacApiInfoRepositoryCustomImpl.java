package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.repository.custom.HmacApiInfoRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * @author Sauravi Thapa २०/२/२
 */
public class HmacApiInfoRepositoryCustomImpl implements HmacApiInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


}
