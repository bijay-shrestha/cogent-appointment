package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.BillingModeChargeResponse;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponse;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
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
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.admin.query.HospitalDepartmentQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

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
    public Optional<List<DropDownResponseDTO>> fetchAvailableHospitalDepartment(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_AVAILABLE_ROOM_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> dropDownDTOS = transformQueryToResultList(query, DropDownResponseDTO.class);

        return dropDownDTOS.isEmpty() ? Optional.empty() : Optional.of(dropDownDTOS);
    }

    @Override
    public HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO searchRequestDTO,
                                                       Pageable pageable) {

        HospitalDepartmentMinimalResponseDTO response = new HospitalDepartmentMinimalResponseDTO();

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDepartmentMinimalResponse> minimalResponseDTOS = transformNativeQueryToResultList(query,
                HospitalDepartmentMinimalResponse.class);

        minimalResponseDTOS.forEach(minimalResponseDTO -> {
            Query fetchBillingModeWithCharge = createQuery.apply(entityManager,
                    QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_BILLING_MODE_WITH_CHARGE)
                    .setParameter(HOSPITAL_DEPARTMENT_ID, Long.parseLong(minimalResponseDTO.getId().toString()));

            minimalResponseDTO.setBillingModeChargeResponseList(transformQueryToResultList(
                    fetchBillingModeWithCharge, BillingModeChargeResponse.class));
        });

        if (minimalResponseDTOS.isEmpty()) {
            throw HOSPITAL_DEPARTMENT_NOT_FOUND.get();
        } else {
            response.setHospitalDepartmentList(minimalResponseDTOS);
            response.setTotalItems(totalItems);
            return response;
        }

    }

    @Override
    public HospitalDepartmentResponseDTO fetchHospitalDepartmentDetails(Long hospitalDepartmentId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DETAILS)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        Query doctorListQuery = createQuery.apply(entityManager, QUERY_TO_GET_DOCTOR_LIST_BY_HOSPITAL_DEPARTMENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        Query roomListQuery = createQuery.apply(entityManager, QUERY_TO_GET_ROOM_LIST_BY_HOSPITAL_DEPARTMENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        Query billingModeWithChargeQuery = createQuery.apply(entityManager,
                QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_BILLING_MODE_WITH_CHARGE)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        try {
            HospitalDepartmentResponseDTO responseDTO = transformQueryToSingleResult(query,
                    HospitalDepartmentResponseDTO.class);
            responseDTO.setDoctorList(transformQueryToResultList(doctorListQuery, DoctorDropdownDTO.class));
            responseDTO.setRoomList(transformQueryToResultList(roomListQuery, DropDownResponseDTO.class));
            responseDTO.setBillingModeChargeResponseList(transformQueryToResultList(billingModeWithChargeQuery,
                    BillingModeChargeResponse.class));
            return responseDTO;
        } catch (NoResultException e) {
            log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, hospitalDepartmentId);
            throw new NoContentFoundException(HospitalDepartment.class, "id", hospitalDepartmentId.toString());
        }
    }

    //    Hospital Department = Department in frontend
    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT);
        throw new NoContentFoundException("No Department(s) Found");
    };

}
