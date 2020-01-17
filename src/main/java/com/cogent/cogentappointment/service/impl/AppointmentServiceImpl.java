package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.request.appointment.*;
import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentAvailabilityResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.appointment.AppointmentResponseDTO;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Appointment;
import com.cogent.cogentappointment.model.Doctor;
import com.cogent.cogentappointment.model.Patient;
import com.cogent.cogentappointment.model.Specialization;
import com.cogent.cogentappointment.repository.AppointmentRepository;
import com.cogent.cogentappointment.service.AppointmentService;
import com.cogent.cogentappointment.service.DoctorService;
import com.cogent.cogentappointment.service.PatientService;
import com.cogent.cogentappointment.service.SpecializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.utils.AppointmentUtils.*;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final SpecializationService specializationService;

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(PatientService patientService,
                                  DoctorService doctorService,
                                  SpecializationService specializationService,
                                  AppointmentRepository appointmentRepository) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.specializationService = specializationService;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentAvailabilityResponseDTO checkAvailability(AppointmentRequestDTO appointmentRequestDTO) {

//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(CHECK_AVAILABILITY_PROCESS_STARTED);
//
//        List<AppointmentTimeResponseDTO> responseDTO = appointmentRepository.checkAvailability(
//                appointmentRequestDTO.getAppointmentDate(),
//                appointmentRequestDTO.getDoctorId(),
//                appointmentRequestDTO.getSpecializationId());
//
//        DoctorDutyRosterTimeResponseDTO doctorDutyRosterTimeResponseDTO =
//                adminService.fetchDoctorDutyRoster(parseToDoctorDutyRosterRequestDTO(appointmentRequestDTO));
//
//        log.info(CHECK_AVAILABILITY_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
//
//        return parseToAppointmentAvailabilityResponseDTO(responseDTO, doctorDutyRosterTimeResponseDTO);
        return null;
    }

    @Override
    public String save(AppointmentRequestDTO appointmentRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT);

        Patient patient = fetchPatient(appointmentRequestDTO.getIsNewRegistration(),
                appointmentRequestDTO.getPatientRequestDTO());

        Doctor doctor = fetchDoctor(appointmentRequestDTO.getDoctorId());

        Specialization specialization = fetchSpecialization(appointmentRequestDTO.getSpecializationId());

        String appointmentNumber = appointmentRepository.generateAppointmentNumber(
                appointmentRequestDTO.getCreatedDateNepali());

        Appointment appointment = parseToAppointment(appointmentRequestDTO, appointmentNumber,
                patient, specialization, doctor);

        save(appointment);

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return appointmentNumber;
    }

    @Override
    public void update(AppointmentUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT);

        Appointment appointment = findById(updateRequestDTO.getAppointmentId());

//        Patient patient = fetchPatient(updateRequestDTO.getPatientId());

//        AdminAppointmentResponseDTO responseDTO = fetchAdminDetails(updateRequestDTO);
//
//        parseToUpdatedAppointment(appointment, updateRequestDTO, responseDTO, patient);

        //todo; appointment status in follow up tracker to be updated as per appointment

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void cancel(AppointmentCancelRequestDTO cancelRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CANCELLING_PROCESS_STARTED);

        Appointment appointment = findById(cancelRequestDTO.getId());

        convertToCancelledAppointment(appointment, cancelRequestDTO);

        log.info(CANCELLING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentMinimalResponseDTO> search(AppointmentSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentMinimalResponseDTO> responseDTOS =
                appointmentRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);

        AppointmentResponseDTO responseDTO = appointmentRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void rescheduleAppointment(AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(RESCHEDULE_PROCESS_STARTED);

        Appointment appointment = findIncompleteAppointmentById(rescheduleRequestDTO.getAppointmentId());

        Date originalAppointmentDate = appointment.getAppointmentDate();
        Date originalAppointmentTime = appointment.getStartTime();

        parseToRescheduleAppointment(appointment, rescheduleRequestDTO);

        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
    }

//    @Override
//    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
//            AppointmentStatusRequestDTO requestDTO) {
//
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);
//
//        List<AppointmentStatusResponseDTO> responseDTOS =
//                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO);
//
//        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
//        return responseDTOS;
//    }

//    @Override
//    public List<AppointmentDateResponseDTO> fetchBookedAppointmentDates(AppointmentDateRequestDTO requestDTO) {
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_PROCESS_STARTED);
//
//        List<AppointmentDateResponseDTO> appointmentDateResponseDTOS =
//                appointmentRepository.fetchBookedAppointmentDates(requestDTO);
//
//        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
//
//        return appointmentDateResponseDTOS;
//    }

    private Doctor fetchDoctor(Long doctorId) {
        return doctorService.fetchDoctorById(doctorId);
    }

    private Specialization fetchSpecialization(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }

    private Patient fetchPatient(Boolean isNewRegistration,
                                 PatientRequestDTO patientRequestDTO) {

        return isNewRegistration ? patientService.save(patientRequestDTO)
                : patientService.fetchPatient(patientRequestDTO.getPatientId());
    }

    private Appointment findById(Long appointmentId) {
        return appointmentRepository.findAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Appointment findIncompleteAppointmentById(Long appointmentId) {
        return appointmentRepository.fetchIncompleteAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class, "id", id.toString());
    };

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

//    private void saveFollowUpTracker(AppointmentRequestDTO appointmentRequestDTO,
//                                     String appointmentNumber,
//                                     AdminAppointmentResponseDTO responseDTO,
//                                     Patient patient) {
//
//        String parentAppointmentNumber;
//
//        if (Objects.isNull(appointmentRequestDTO.getParentAppointmentNumber())) {
//            parentAppointmentNumber = appointmentNumber;
//            followUpTrackerService.saveFollowUpTracker(
//                    appointmentRequestDTO.getAppointmentDate(),
//                    parentAppointmentNumber,
//                    responseDTO.getPatientType(),
//                    responseDTO.getDoctor(),
//                    patient);
//        } else {
//            parentAppointmentNumber = appointmentRequestDTO.getParentAppointmentNumber();
//            followUpTrackerService.updateNumberOfFollowupsInFollowUpTracker(
//                    parentAppointmentNumber,
//                    responseDTO.getDoctor().getId(),
//                    patient.getId());
//        }
//    }

}

