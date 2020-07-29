package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.CountryRepository;
import com.cogent.cogentappointment.admin.service.CountryService;
import com.cogent.cogentappointment.persistence.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.CountryLog.COUNTRY;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, COUNTRY);

        List<DropDownResponseDTO> responseDTOS = countryRepository.fetchActiveCountry();

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, COUNTRY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchCountry() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, COUNTRY);

        List<DropDownResponseDTO> responseDTOS = countryRepository.fetchCountry();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, COUNTRY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Country fetchCountryById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, COUNTRY);

        Country country = countryRepository.fetchActiveCountryById(id)
                .orElseThrow(() -> COUNTRY_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, COUNTRY, getDifferenceBetweenTwoTime(startTime));

        return country;
    }

    private Function<Long, NoContentFoundException> COUNTRY_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,COUNTRY, id);
        throw new NoContentFoundException(Country.class, "id", id.toString());
    };
}
