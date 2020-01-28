package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.constants.QueryConstants;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.client.repository.custom.DoctorDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.client.utils.DoctorDutyRosterOverrideUtils;
import com.cogent.cogentappointment.client.utils.commons.DateUtils;
import com.cogent.cogentappointment.client.utils.commons.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.query.DoctorDutyRosterOverrideQuery.*;

@Repository
@Transactional(readOnly = true)
public class DoctorDutyRosterOverrideRepositoryCustomImpl implements DoctorDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDoctorDutyRosterOverrideCount(Long doctorId,
                                                      Long specializationId,
                                                      Date fromDate,
                                                      Date toDate) {

        Query query = QueryUtils.createQuery.apply(entityManager, VALIDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_COUNT)
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId)
                .setParameter(QueryConstants.FROM_DATE, DateUtils.utilDateToSqlDate(fromDate))
                .setParameter(QueryConstants.TO_DATE, DateUtils.utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                             Long doctorId,
                                                                             Long specializationId) {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME)
                .setParameter(QueryConstants.DATE, DateUtils.utilDateToSqlDate(date))
                .setParameter(QueryConstants.DOCTOR_ID, doctorId)
                .setParameter(QueryConstants.SPECIALIZATION_ID, specializationId);

        List<DoctorDutyRosterTimeResponseDTO> responseDTOList =
                QueryUtils.transformQueryToResultList(query, DoctorDutyRosterTimeResponseDTO.class);

        return responseDTOList.isEmpty() ? null : responseDTOList.get(0);
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus(
            DoctorDutyRosterStatusRequestDTO requestDTO) {

        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO))
                .setParameter(QueryConstants.FROM_DATE, DateUtils.utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(QueryConstants.TO_DATE, DateUtils.utilDateToSqlDate(requestDTO.getToDate()));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(QueryConstants.DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(QueryConstants.SPECIALIZATION_ID, requestDTO.getSpecializationId());

        List<Object[]> results = query.getResultList();

        return DoctorDutyRosterOverrideUtils.parseQueryResultToDoctorDutyRosterStatusResponseDTO(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }
}
