package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.DoctorRepository;
import com.cogent.cogentappointment.admin.repository.DoctorSalutationRepository;
import com.cogent.cogentappointment.admin.repository.SalutationRepository;
import com.cogent.cogentappointment.admin.service.SalutationService;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import com.cogent.cogentappointment.persistence.model.Salutation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.SalutationLog.SALUTATION;
import static com.cogent.cogentappointment.admin.utils.SalutationUtils.parseDoctorSalutationToDeleted;
import static com.cogent.cogentappointment.admin.utils.SalutationUtils.parseToSalutaionToDeleted;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

@Service
@Transactional
@Slf4j
public class SalutationServiceImpl implements SalutationService {

    private final SalutationRepository salutationRepository;

    private final DoctorSalutationRepository doctorSalutationRepository;

    private final DoctorRepository doctorRepository;

    public SalutationServiceImpl(SalutationRepository salutationRepository, DoctorSalutationRepository doctorSalutationRepository, DoctorRepository doctorRepository) {
        this.salutationRepository = salutationRepository;
        this.doctorSalutationRepository = doctorSalutationRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinSalutation() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SALUTATION);

        List<DropDownResponseDTO> responseDTOS = salutationRepository.fetchActiveMinSalutation();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SALUTATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, SALUTATION);

        Salutation salutation = findSalutationById(deleteRequestDTO.getId());

        salutationRepository.save(parseToSalutaionToDeleted(salutation, deleteRequestDTO));

        updateDoctorSalutation(salutation);


        log.info(DELETING_PROCESS_COMPLETED, SALUTATION, getDifferenceBetweenTwoTime(startTime));

    }

    private void updateDoctorSalutation(Salutation salutation) {

        List<DoctorSalutation> doctorSalutationList = doctorSalutationRepository.findDoctorSalutationBySalutationId(salutation.getId())
                .orElseThrow(() -> SALUTATION_WITH_GIVEN_ID_NOT_FOUND.apply(salutation.getId()));

        doctorSalutationRepository.saveAll(parseDoctorSalutationToDeleted(doctorSalutationList));

        doctorSalutationList.forEach(doctorSalutation -> {

            Doctor doctor = doctorRepository.findActiveDoctorById(doctorSalutation.getDoctorId())
                    .orElseThrow(() -> SALUTATION_WITH_GIVEN_ID_NOT_FOUND.apply(doctorSalutation.getId()));

            if (doctor.getSalutation() != null) {
                List<String> salutationList = new ArrayList<>();
                salutationList.addAll(Arrays.asList(doctor.getSalutation().split("\\s+")));

                salutationList.remove(salutation.getCode());

                String salutationToUpdate = "";
                if (salutationList.size() == 0) {
                    salutationToUpdate = null;
                }

                if (salutationList.size() == 1) {
                    salutationToUpdate = salutationList.stream()
                            .collect(Collectors.joining());
                }

                if (salutationList.size() > 1) {
                    salutationToUpdate = salutationList.stream()
                            .collect(Collectors.joining(" "));
                }
                doctor.setSalutation(salutationToUpdate);
            }

        });


    }

    private Salutation findSalutationById(Long id) {
        return salutationRepository.fetchActiveSalutationById(id).orElseThrow(() -> SALUTATION_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> SALUTATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SALUTATION, id);
        throw new NoContentFoundException(Salutation.class, "id", id.toString());
    };
}
