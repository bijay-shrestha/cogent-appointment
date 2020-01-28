package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.Department;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.admin.repository.custom.DepartmentRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.cogent.cogentappointment.admin.query.DepartmentQuery.*;

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

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(QueryConstants.NAME, requestDTO.getName())
                .setParameter(QueryConstants.CODE, requestDTO.getDepartmentCode())
                .setParameter(QueryConstants.HOSPITAL_ID, requestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO) {
        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(QueryConstants.ID, requestDTO.getId())
                .setParameter(QueryConstants.NAME, requestDTO.getName())
                .setParameter(QueryConstants.CODE, requestDTO.getDepartmentCode())
                .setParameter(QueryConstants.HOSPITAL_ID, requestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_SEARCH_DEPARTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<DepartmentMinimalResponseDTO> minimalResponseDTOS = QueryUtils.transformQueryToResultList(query,
                DepartmentMinimalResponseDTO.class);

        if (minimalResponseDTOS.isEmpty()) throw new NoContentFoundException(Department.class);
        else {
            minimalResponseDTOS.get(0).setTotalItems(totalItems);
            return minimalResponseDTOS;
        }
    }

    @Override
    public DepartmentResponseDTO fetchDetails(Long id) {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_DETAILS)
                .setParameter(QueryConstants.ID, id);

        try {
            return QueryUtils.transformQueryToSingleResult(query, DepartmentResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(Department.class, "id", id.toString());
        }
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchDepartmentForDropdown() {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_FOR_DROPDOWN);

        List<DropDownResponseDTO> dropDownDTOS = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchActiveDropDownList() {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_DEPARTMENT_FOR_DROPDOWN);

        List<DropDownResponseDTO> dropDownDTOS = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public Optional<List<DropDownResponseDTO>> fetchDepartmentByHospitalId(Long hospitalId) {
        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_DEPARTMENT_BY_HOSPITAL_ID)
                .setParameter(QueryConstants.HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }
}

