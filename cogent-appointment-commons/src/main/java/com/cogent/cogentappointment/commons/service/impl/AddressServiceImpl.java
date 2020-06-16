package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.commons.repository.AddressRepository;
import com.cogent.cogentappointment.commons.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.commons.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.commons.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.commons.log.constants.AddressLog.ZONE;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;


/**
 * @author Sauravi Thapa ON 6/16/20
 */

@Service
@Transactional(readOnly = true)
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchZoneDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ZONE);

        List<DropDownResponseDTO> response=addressRepository.getListOfZone();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ZONE, getDifferenceBetweenTwoTime(startTime));

        return response;

    }
}
