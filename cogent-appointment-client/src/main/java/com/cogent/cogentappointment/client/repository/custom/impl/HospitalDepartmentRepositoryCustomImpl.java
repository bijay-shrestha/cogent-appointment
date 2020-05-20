package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponse;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa ON 5/20/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentRepositoryCustomImpl implements HospitalDepartmentRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(HospitalDepartmentRequestDTO requestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(HospitalDepartmentUpdateRequestDTO requestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchMinHospitalDepartment(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchActiveMinHospitalDepartment(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO searchRequestDTO,
                                                       Pageable pageable) {

        HospitalDepartmentMinimalResponseDTO response = new HospitalDepartmentMinimalResponseDTO();

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_DEPARTMENT.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID, getLoggedInHospitalId());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDepartmentMinimalResponse> minimalResponseDTOS = transformNativeQueryToResultList(query,
                HospitalDepartmentMinimalResponse.class);

        if (minimalResponseDTOS.isEmpty()) {
           throw  HOSPITAL_DEPARTMENT_NOT_FOUND.get();
        } else {
            response.setResponse(minimalResponseDTOS);
            response.setTotalItems(totalItems);
            return response;
        }

    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT);
        throw new NoContentFoundException(HospitalDepartment.class);
    };

}
