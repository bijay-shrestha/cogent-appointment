package com.cogent.cogentappointment.commons.repository.custom.impl;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.commons.repository.custom.AddressRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

import static com.cogent.cogentappointment.commons.constants.QueryConstants.*;
import static com.cogent.cogentappointment.commons.query.AddressQuery.*;
import static com.cogent.cogentappointment.commons.utils.QueryUtils.createQuery;
import static com.cogent.cogentappointment.commons.utils.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.persistence.enums.GeographyType.ZONE;


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
//                .setParameter(GEOGRAPHY_TYPE,ZONE.ordinal());


        List<DropDownResponseDTO> response = transformQueryToResultList(query, DropDownResponseDTO.class);

        return response;
    }

    @Override
    public List<DropDownResponseDTO> getListOfProvince() {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LIST_OF_PROVINCE);
//                .setParameter(GEOGRAPHY_TYPE,ZONE.ordinal());

        List<DropDownResponseDTO> response = transformQueryToResultList(query, DropDownResponseDTO.class);

        return response;
    }

    @Override
    public List<DropDownResponseDTO> getListOfDistrictByZoneId(BigInteger zoneId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LIST_OF_DISTRICT_BY_ZONE_ID)
                .setParameter(ZONE_ID,zoneId);

        List<DropDownResponseDTO> response = transformQueryToResultList(query, DropDownResponseDTO.class);

        return response;
    }

    @Override
    public List<DropDownResponseDTO> getListOfDistrictByProvinceId(BigInteger provinceId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LIST_OF_DISTRICT_BY_PROVINCE_ID)
                .setParameter(PROVINCE_ID,provinceId);

        List<DropDownResponseDTO> response = transformQueryToResultList(query, DropDownResponseDTO.class);

        return response;
    }
}
