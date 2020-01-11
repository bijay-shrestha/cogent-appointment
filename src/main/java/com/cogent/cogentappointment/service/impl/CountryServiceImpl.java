package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Country;
import com.cogent.cogentappointment.repository.CountryRepository;
import com.cogent.cogentappointment.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.CountryLog.COUNTRY;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, COUNTRY);

        List<DropDownResponseDTO> responseDTOS = countryRepository.fetchActiveCountry();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, COUNTRY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Country fetchCountryById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, COUNTRY);

        Country country = countryRepository.fetchActiveCountryById(id)
                .orElseThrow(() -> new NoContentFoundException(Country.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, COUNTRY, getDifferenceBetweenTwoTime(startTime));

        return country;
    }
}
