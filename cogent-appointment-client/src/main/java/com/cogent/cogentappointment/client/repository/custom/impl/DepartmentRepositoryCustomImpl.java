package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.DepartmentRepositoryCustom;
import com.cogent.cogentappointment.client.utils.commons.PageableUtils;
import com.cogent.cogentappointment.persistence.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.DepartmentLog.DEPARTMENT;
import static com.cogent.cogentappointment.client.query.DepartmentQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author Sauravi
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(DepartmentRequestDTO requestDTO, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Long hospitalId,
                                                     Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DEPARTMENT.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DepartmentMinimalResponseDTO> minimalResponseDTOS = transformQueryToResultList(query,
                DepartmentMinimalResponseDTO.class);

        if (minimalResponseDTOS.isEmpty()){
            error();
            throw new NoContentFoundException(Department.class);
        }
        else {
            minimalResponseDTOS.get(0).setTotalItems(totalItems);
            return minimalResponseDTOS;
        }
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DETAILS)
                .setParameter(ID, id)
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, DepartmentResponseDTO.class);
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND_BY_ID,DEPARTMENT,id);
            throw new NoContentFoundException(Department.class, "id", id.toString());
        }
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchMinDepartment(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchActiveMinDepartment(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_DEPARTMENT_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    private void error() {
        log.error(CONTENT_NOT_FOUND, DEPARTMENT);
    }
}

