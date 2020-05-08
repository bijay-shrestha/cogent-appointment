package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author smriti on 08/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDRShiftWiseRepositoryCustomImpl implements DDRShiftWiseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


}
