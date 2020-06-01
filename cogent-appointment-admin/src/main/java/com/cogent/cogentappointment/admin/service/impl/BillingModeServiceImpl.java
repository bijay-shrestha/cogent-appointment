package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.BillingModeRepository;
import com.cogent.cogentappointment.admin.service.BillingModeService;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.BillingModeLog.BILLING_MODE;
import static com.cogent.cogentappointment.admin.utils.BillingModeUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

/**
 * @author Sauravi Thapa ON 4/17/20
 */

@Service
@Transactional
@Slf4j
public class BillingModeServiceImpl implements BillingModeService {

    private final BillingModeRepository billingModeRepository;

    public BillingModeServiceImpl(BillingModeRepository billingModeRepository) {
        this.billingModeRepository = billingModeRepository;
    }

    @Override
    public void save(BillingModeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, BILLING_MODE);

        List<Object[]> billingMode = billingModeRepository.validateDuplicity(requestDTO);

        validateDuplicity(billingMode, requestDTO.getName(), requestDTO.getCode(), BillingMode.class.getSimpleName());

        save(parseToBillingMode.apply(requestDTO));

        log.info(SAVING_PROCESS_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(BillingModeUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, BILLING_MODE);

        BillingMode billingMode = fetchBillingModeById(requestDTO.getId());

        List<Object[]> billingModes = billingModeRepository.validateDuplicity(requestDTO);

        validateDuplicity(billingModes,
                requestDTO.getName(),
                requestDTO.getCode(),
                BillingMode.class.getSimpleName());

        save(parseToUpdateBillingMode.apply(requestDTO, billingMode));

        log.info(UPDATING_PROCESS_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, BILLING_MODE);

        BillingMode billingMode = fetchBillingModeById(deleteRequestDTO.getId());

        save(parseToDeletedBillingMode.apply(deleteRequestDTO,billingMode));

        log.info(DELETING_PROCESS_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public BillingModeMinimalResponseDTO search(BillingModeSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, BILLING_MODE);

        BillingModeMinimalResponseDTO responseDTO = billingModeRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public BillingModeResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, BILLING_MODE);

        BillingModeResponseDTO details = billingModeRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return details;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinBillingMode() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository.fetchActiveMinBillingMode();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinBillingModeByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository.fetchActiveMinBillingModeByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinBillingMode() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository.fetchMinBillingMode();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinBillingModeByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository.fetchMinBillingModeByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    private void save(BillingMode billingMode){
        billingModeRepository.save(billingMode);
    }

    private BillingMode fetchBillingModeById(Long id) {
        return billingModeRepository.fetchBillingModeById(id)
                .orElseThrow(() -> BILLING_MODE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> BILLING_MODE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, BILLING_MODE, id);
        throw new NoContentFoundException(BillingMode.class, "id", id.toString());
    };
}
