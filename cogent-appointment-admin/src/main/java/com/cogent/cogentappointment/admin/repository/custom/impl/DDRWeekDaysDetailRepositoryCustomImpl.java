package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRBreakResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.DDRBreakDetailRepository;
import com.cogent.cogentappointment.admin.repository.custom.DDRWeekDaysDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DDRWeekDaysDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.DDR_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.DDRConstants.SHIFT_ID;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_WEEK_DAYS;
import static com.cogent.cogentappointment.admin.query.ddrShiftWise.DDRWeekDaysDetailQuery.QUERY_TO_FETCH_DDR_WEEK_DAYS_DETAIL;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DDRWeekDaysDetailRepositoryCustomImpl implements DDRWeekDaysDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final DDRBreakDetailRepository ddrBreakDetailRepository;

    public DDRWeekDaysDetailRepositoryCustomImpl(DDRBreakDetailRepository ddrBreakDetailRepository) {
        this.ddrBreakDetailRepository = ddrBreakDetailRepository;
    }

    @Override
    public List<DDRExistingWeekDaysResponseDTO> fetchExistingDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_DDR_WEEK_DAYS_DETAIL)
                .setParameter(DDR_ID, requestDTO.getDdrId())
                .setParameter(SHIFT_ID, requestDTO.getShiftId());

        List<DDRExistingWeekDaysResponseDTO> responseDTO =
                transformQueryToResultList(query, DDRExistingWeekDaysResponseDTO.class);

        if (responseDTO.isEmpty())
            NO_WEEK_DAYS_INFO_FOUND.get();

        return responseDTO;
    }

    @Override
    public List<DDRWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO) {
        Query query = entityManager.createQuery(QUERY_TO_FETCH_DDR_WEEK_DAYS_DETAIL)
                .setParameter(DDR_ID, requestDTO.getDdrId())
                .setParameter(SHIFT_ID, requestDTO.getShiftId());

        List<DDRWeekDaysResponseDTO> ddrWeekDetails =
                transformQueryToResultList(query, DDRWeekDaysResponseDTO.class);

        if (ddrWeekDetails.isEmpty()) {
            NO_WEEK_DAYS_INFO_FOUND.get();
        } else {

            ddrWeekDetails.forEach(weekDetail -> {
                if (weekDetail.getHasBreak().equals(YES)) {
                    List<DDRBreakResponseDTO> breakDetails =
                            ddrBreakDetailRepository.fetchWeekDaysBreakDetailForUpdateModal(weekDetail.getDdrWeekDaysId());
                    weekDetail.setBreakDetail(breakDetails);
                }
            });
        }

        return ddrWeekDetails;
    }

    private Supplier<NoContentFoundException> NO_WEEK_DAYS_INFO_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DDR_WEEK_DAYS);
        throw new NoContentFoundException(DDRWeekDaysDetail.class);
    };

}
