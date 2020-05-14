package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DDRShiftWiseRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_SHIFT_WISE;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRShiftWiseQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 08/05/20
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class DDRShiftWiseRepositoryCustomImpl implements DDRShiftWiseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDoctorDutyRosterCount(Long doctorId,
                                              Long specializationId,
                                              Date fromDate,
                                              Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DDR_SHIFT_WISE_COUNT)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DDRExistingMinDTO> fetchExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_DDR)
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId())
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        return transformQueryToResultList(query, DDRExistingMinDTO.class);
    }

    @Override
    public List<DDRMinResponseDTO> search(DDRSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(searchRequestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(searchRequestDTO.getToDate()));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DDRMinResponseDTO> results = transformQueryToResultList(query, DDRMinResponseDTO.class);

        if (results.isEmpty())
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);

        return results;
    }

    @Override
    public DDRResponseDTO fetchDDRDetail(Long ddrId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_SHIFT_WISE_DETAILS)
                .setParameter(DDR_ID, ddrId);
        try {
            return transformQueryToSingleResult(query, DDRResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND(ddrId);
        }
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DDR_SHIFT_WISE);
        throw new NoContentFoundException(DoctorDutyRosterShiftWise.class);
    };

    private NoContentFoundException DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND(Long ddrId) {
        log.error(CONTENT_NOT_FOUND_BY_ID, DDR_SHIFT_WISE, ddrId);
        throw new NoContentFoundException(DoctorDutyRosterShiftWise.class, "ddrId", ddrId.toString());
    }
}
