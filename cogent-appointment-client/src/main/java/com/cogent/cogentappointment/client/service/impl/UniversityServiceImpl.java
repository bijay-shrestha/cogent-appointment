package com.cogent.cogentappointment.client.service.impl;


import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.UniversityRepository;
import com.cogent.cogentappointment.client.service.CountryService;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.client.service.UniversityService;
import com.cogent.cogentappointment.persistence.model.Country;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.UniversityLog.UNIVERSITY;
import static com.cogent.cogentappointment.client.utils.UniversityUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;


/**
 * @author smriti on 08/11/2019
 */
@Service
@Transactional
@Slf4j
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    private final CountryService countryService;

    private final HospitalService hospitalService;

    public UniversityServiceImpl(UniversityRepository universityRepository,
                                 CountryService countryService,
                                 HospitalService hospitalService) {
        this.universityRepository = universityRepository;
        this.countryService = countryService;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(UniversityRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, UNIVERSITY);

        Long hospitalId = getLoggedInHospitalId();

        Country country = fetchCountry(requestDTO.getCountryId());

        Hospital hospital = fetchHospital(hospitalId);

        Long university = universityRepository.validateDuplicity(
                requestDTO.getName(),
                hospitalId);

        validateName(university, requestDTO.getName());

        save(parseToUniversity(requestDTO, country, hospital));

        log.info(SAVING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(UniversityUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, UNIVERSITY);

        Long hospitalId = getLoggedInHospitalId();

        University university = fetchByIdAndHospitalId(requestDTO.getId(), hospitalId);

        Country country = fetchCountry(requestDTO.getCountryId());

        Hospital hospital = fetchHospital(hospitalId);

        Long count = universityRepository.validateDuplicity(
                requestDTO.getId(), requestDTO.getName(), hospitalId
        );

        validateName(count, requestDTO.getName());

        parseToUpdatedUniversity(requestDTO, country, hospital, university);

        log.info(UPDATING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, UNIVERSITY);

        University university = fetchByIdAndHospitalId(deleteRequestDTO.getId(), getLoggedInHospitalId());

        parseToDeletedUniversity(university, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, UNIVERSITY);

        Long hospitalId = getLoggedInHospitalId();

        List<UniversityMinimalResponseDTO> responseDTOS =
                universityRepository.search(searchRequestDTO, hospitalId, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UniversityResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, UNIVERSITY);

        UniversityResponseDTO responseDTO = universityRepository.fetchDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinUniversity() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, UNIVERSITY);

        List<DropDownResponseDTO> responseDTOS =
                universityRepository.fetchActiveMinUniversity(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public University findActiveUniversityById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, UNIVERSITY);

        University university = universityRepository.fetchActiveUniversityById(id)
                .orElseThrow(() -> UNIVERSITY_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return university;
    }

    private void validateName(Long universityCount, String name) {
        if (universityCount.intValue() > 0) {
            log.error(NAME_DUPLICATION_ERROR, UNIVERSITY, name);
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, University.class.getSimpleName(), name));
        }
    }

    private Country fetchCountry(Long countryId) {
        return countryService.fetchCountryById(countryId);
    }

    private void save(University university) {
        universityRepository.save(university);
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private University fetchByIdAndHospitalId(Long id, Long hospitalId) {
        return universityRepository.fetchUniversityByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> UNIVERSITY_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> UNIVERSITY_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,UNIVERSITY,id);
        throw new NoContentFoundException(University.class, "id", id.toString());
    };

}
