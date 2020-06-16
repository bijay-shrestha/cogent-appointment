package com.cogent.cogentappointment.commons.repository.custom.impl;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.commons.repository.custom.AddressRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.commons.query.AddressQuery.QUERY_TO_GET_LIST_OF_ZONES;
import static com.cogent.cogentappointment.commons.utils.AddressUtils.parseToZoneDropDown;
import static com.cogent.cogentappointment.commons.utils.QueryUtils.createNativeQuery;

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
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_GET_LIST_OF_ZONES);

        List<Object[]> result = query.getResultList();

        List<DropDownResponseDTO> response = parseToZoneDropDown.apply(result);

        return response;
    }

    @Override
    public List<DropDownResponseDTO> getListOfProvince() {
        return null;
    }
}
