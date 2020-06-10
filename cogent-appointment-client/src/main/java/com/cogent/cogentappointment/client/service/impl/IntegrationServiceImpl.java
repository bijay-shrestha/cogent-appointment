package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.integrationClient.ApiIntegrationCheckInRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.repository.IntegrationRepository;
import com.cogent.cogentappointment.client.service.IntegrationService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;
    private final AppointmentRepository appointmentRepository;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository, HospitalPatientInfoRepository hospitalPatientInfoRepository, AppointmentRepository appointmentRepository) {
        this.integrationRepository = integrationRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void approveAppointmentCheckIn(ApiIntegrationCheckInRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.fetchPendingAppointmentById(requestDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_INFO_NOT_FOUND.apply(requestDTO.getAppointmentId()));


        if (requestDTO.isStatus()) {

            HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository.
                    findByPatientAndHospitalId(appointment.getPatientId().getId(), appointment.getHospitalId().getId())
                    .orElseThrow(() -> HOSPITAL_PATIENT_INFO_NOT_FOUND.apply(appointment.getPatientId().getId()));

            hospitalPatientInfo.setHospitalNumber(requestDTO.getHospitalNumber());
        }

        appointment.setStatus(APPROVED);


    }

    private Function<Long, NoContentFoundException> APPOINTMENT_INFO_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class);
    };

    private Function<Long, NoContentFoundException> HOSPITAL_PATIENT_INFO_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HospitalPatientInfo.class);
    };

}
