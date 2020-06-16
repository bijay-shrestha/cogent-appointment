package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.commons.repository.AddressRepository;
import com.cogent.cogentappointment.commons.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static com.cogent.cogentappointment.commons.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.commons.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.commons.log.constants.AddressLog.*;
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

    @Override
    public List<DropDownResponseDTO> fetchProvinceDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROVINCE);

        List<DropDownResponseDTO> response=addressRepository.getListOfProvince();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROVINCE, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public List<DropDownResponseDTO> fetchDistrictDropDownByZoneId(BigInteger zoneId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DISTRICT_BY_ZONE_ID);

        List<DropDownResponseDTO> response=addressRepository.getListOfDistrictByZoneId(zoneId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DISTRICT_BY_ZONE_ID, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public List<DropDownResponseDTO> fetchDistrictDropDownByProvinceId(BigInteger provinceId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DISTRICT_BY_PROVINCE_ID);

        List<DropDownResponseDTO> response=addressRepository.getListOfDistrictByProvinceId(provinceId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DISTRICT_BY_PROVINCE_ID, getDifferenceBetweenTwoTime(startTime));

        return response;

    }
}
