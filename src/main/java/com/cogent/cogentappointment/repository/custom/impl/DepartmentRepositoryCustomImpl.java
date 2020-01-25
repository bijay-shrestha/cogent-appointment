package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Department;
import com.cogent.cogentappointment.repository.custom.DepartmentRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.cogent.cogentappointment.constants.QueryConstants.*;
import static com.cogent.cogentappointment.query.DepartmentQuery.*;
import static com.cogent.cogentappointment.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.*;

/**
 * @author Sauravi
 */
@Repository
@Transactional(readOnly = true)
public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(DepartmentRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_CODE, requestDTO.getHospitalCode());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getDepartmentCode())
                .setParameter(HOSPITAL_CODE, requestDTO.getHospitalCode());

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

        if (minimalResponseDTOS.isEmpty()) throw new NoContentFoundException(Department.class);
        else {
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
}

