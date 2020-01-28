package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.log.CommonLogConstant;
import com.cogent.cogentappointment.client.log.constants.CountryLog;
import com.cogent.cogentappointment.client.model.Country;
import com.cogent.cogentappointment.client.repository.CountryRepository;
import com.cogent.cogentappointment.client.service.CountryService;
import com.cogent.cogentappointment.client.utils.commons.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
@Service
@Transactional
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveCountry() {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, CountryLog.COUNTRY);

        List<DropDownResponseDTO> responseDTOS = countryRepository.fetchActiveCountry();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, CountryLog.COUNTRY, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Country fetchCountryById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, CountryLog.COUNTRY);

        Country country = countryRepository.fetchActiveCountryById(id)
                .orElseThrow(() -> new NoContentFoundException(Country.class, "id", id.toString()));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, CountryLog.COUNTRY, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return country;
    }
}
