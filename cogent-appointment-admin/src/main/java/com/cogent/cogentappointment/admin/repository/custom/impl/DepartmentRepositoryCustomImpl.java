package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DepartmentRepositoryCustom;
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

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ERROR_LOG;
import static com.cogent.cogentappointment.admin.log.constants.DepartmentLog.DEPARTMENT;
import static com.cogent.cogentappointment.admin.query.DepartmentQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 25/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(DepartmentRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DEPARTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DepartmentMinimalResponseDTO> minimalResponseDTOS = transformQueryToResultList(query,
                DepartmentMinimalResponseDTO.class);

        if (minimalResponseDTOS.isEmpty()) {
            error();
            throw new NoContentFoundException(Department.class);
        } else {
            minimalResponseDTOS.get(0).setTotalItems(totalItems);
            return minimalResponseDTOS;
        }
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DETAILS)
                .setParameter(ID, id);

        try {
            return transformQueryToSingleResult(query, DepartmentResponseDTO.class);
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND_BY_ID,DEPARTMENT,id);
            throw new NoContentFoundException(Department.class, "id", id.toString());
        }
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchDepartmentForDropdown() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_FOR_DROPDOWN);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchActiveDropDownList() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_DEPARTMENT_FOR_DROPDOWN);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchDepartmentByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    private void error() {
        log.error(ERROR_LOG, DEPARTMENT);
    }
}

