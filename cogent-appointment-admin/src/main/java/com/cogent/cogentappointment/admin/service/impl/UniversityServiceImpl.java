package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.university.UniversityMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.university.UniversityResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.UniversityRepository;
import com.cogent.cogentappointment.admin.service.CountryService;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.service.UniversityService;
import com.cogent.cogentappointment.persistence.model.Country;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.University;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.UniversityLog.UNIVERSITY;
import static com.cogent.cogentappointment.admin.utils.UniversityUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        Country country = fetchCountry(requestDTO.getCountryId());

        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        Long university = universityRepository.validateDuplicity(
                requestDTO.getName(), requestDTO.getHospitalId()
        );

        validateName(university, requestDTO.getName());

        save(parseToUniversity(requestDTO, country, hospital));

        log.info(SAVING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(UniversityUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, UNIVERSITY);

        University university = findUniversityById(requestDTO.getId());

        Long count = universityRepository.validateDuplicity(
                requestDTO.getId(), requestDTO.getName(), requestDTO.getHospitalId()
        );

        validateName(count, requestDTO.getName());

        Country country = fetchCountry(requestDTO.getCountryId());

        Hospital hospital = fetchHospital(requestDTO.getHospitalId());

        parseToUpdatedUniversity(requestDTO, country, hospital, university);

        log.info(UPDATING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, UNIVERSITY);

        University university = findUniversityById(deleteRequestDTO.getId());

        parseToDeletedUniversity(university, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
                                                     Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, UNIVERSITY);

        List<UniversityMinimalResponseDTO> responseDTOS =
                universityRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public UniversityResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, UNIVERSITY);

        UniversityResponseDTO responseDTO = universityRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveUniversity() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, UNIVERSITY);

        List<DropDownResponseDTO> responseDTOS = universityRepository.fetchActiveUniversity();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public University fetchUniversityById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, UNIVERSITY);

        University university = universityRepository.fetchActiveUniversityById(id)
                .orElseThrow(() -> new NoContentFoundException(University.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return university;
    }

    private void validateName(Long universityCount, String name) {
        if (universityCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, University.class.getSimpleName(), name));
    }

    private Country fetchCountry(Long countryId) {
        return countryService.fetchCountryById(countryId);
    }

    private void save(University university) {
        universityRepository.save(university);
    }

    private University findUniversityById(Long id) {
        return universityRepository.findUniversityById(id)
                .orElseThrow(() -> new NoContentFoundException(University.class, "id", id.toString()));
    }

    private Hospital fetchHospital(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }


}
