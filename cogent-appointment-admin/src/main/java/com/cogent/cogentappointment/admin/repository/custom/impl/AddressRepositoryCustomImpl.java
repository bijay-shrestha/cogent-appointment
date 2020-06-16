package com.cogent.cogentappointment.admin.repository.custom.impl;


import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.AddressRepositoryCustom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.query.AddressQuery.QUERY_TO_GET_LIST_OF_ZONES;
import static com.cogent.cogentappointment.admin.utils.AddressUtils.parseToZoneDropDown;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createNativeQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;


/**
 * @author Sauravi Thapa ON 6/15/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class AddressRepositoryCustomImpl implements AddressRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> getListOfZone() {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LIST_OF_ZONES);


        List<DropDownResponseDTO> response = transformQueryToResultList(query,DropDownResponseDTO.class);

        return response;
    }

    @Override
    public List<DropDownResponseDTO> getListOfProvince() {
        return null;
    }
}
