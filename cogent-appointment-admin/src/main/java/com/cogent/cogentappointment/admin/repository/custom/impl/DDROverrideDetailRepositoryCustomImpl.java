package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDROverrideDetailResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.DDROverrideDetailRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDROverrideDetailRepositoryCustomImpl implements DDROverrideDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<DDROverrideDetailResponseDTO> fetchDDROverrideDetail(Long ddrId) {
//        Query query = entityManager.createQuery()
        return null;
    }
}
