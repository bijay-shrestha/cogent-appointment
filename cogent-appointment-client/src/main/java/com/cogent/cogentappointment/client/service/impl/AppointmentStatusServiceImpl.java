package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.count.HospitalDeptAppointmentStatusCountRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAndWeekdaysDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentDetailsForStatus;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.DoctorTimeSlotResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.count.AppointmentCountWithStatusDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.count.HospitalDepartmentRosterDetailsDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.count.HospitalDeptAppointmentStatusCountResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.*;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.AppointmentService;
import com.cogent.cogentappointment.client.service.AppointmentStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.ALL;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.utils.AppointmentStatusUtils.*;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.getTotalAppointmentTimeSlotCount;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.getVacantAppointmentSlotsCount;
import static com.cogent.cogentappointment.client.utils.DoctorDutyRosterUtils.mergeOverrideAndActualDoctorDutyRoster;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.mergeOverrideAndActualHospitalDeptDutyRoster;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.mergeOverrideAndActualHospitalDeptDutyRosterForCount;
import static com.cogent.cogentappointment.commons.utils.MinIOUtils.fileUrlCheckPoint;

/**
 * @author smriti ON 16/12/2019
 */
@Service
@Transactional
@Slf4j
public class AppointmentStatusServiceImpl implements AppointmentStatusService {

    private final DoctorDutyRosterRepository doctorDutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository;

    private final HospitalDeptDutyRosterOverrideRepository deptDutyRosterOverrideRepository;

    private final HospitalDeptDutyRosterRepository deptDutyRosterRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentService appointmentService;

    private final AppointmentRepository appointmentRepository;

    private final HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository
            hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository;

    private final RoomRepository roomRepository;

    public AppointmentStatusServiceImpl(DoctorDutyRosterRepository doctorDutyRosterRepository,
                                        DoctorDutyRosterOverrideRepository doctorDutyRosterOverrideRepository,
                                        HospitalDeptDutyRosterOverrideRepository deptDutyRosterOverrideRepository,
                                        HospitalDeptDutyRosterRepository deptDutyRosterRepository,
                                        DoctorRepository doctorRepository,
                                        AppointmentService appointmentService,
                                        AppointmentRepository appointmentRepository,
                                        HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository
                                                hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository,
                                        RoomRepository roomRepository) {
        this.doctorDutyRosterRepository = doctorDutyRosterRepository;
        this.doctorDutyRosterOverrideRepository = doctorDutyRosterOverrideRepository;
        this.deptDutyRosterOverrideRepository = deptDutyRosterOverrideRepository;
        this.deptDutyRosterRepository = deptDutyRosterRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository = hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository;
        this.roomRepository = roomRepository;
    }


    @Override
    public AppointmentStatusDTO fetchAppointmentStatusResponseDTO(AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_STATUS);

        if (requestDTO.getHasAppointmentNumber().equals(YES))
            return searchAppointmentByApptNumber(requestDTO.getAppointmentNumber());

        Long hospitalId = getLoggedInHospitalId();

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus = fetchDoctorStatus(requestDTO, hospitalId);

        doctorDutyRosterStatus.forEach(response -> {
            if(response.getFileUri()!=null) {
                response.setFileUri(fileUrlCheckPoint(response.getFileUri()));
            }
        });

        List<AppointmentStatusResponseDTO> appointments = fetchAppointmentStatus(requestDTO, hospitalId);

        doctorDutyRosterStatus = setDoctorTimeSlot(requestDTO.getStatus(), doctorDutyRosterStatus, appointments);

        List<DoctorDropdownDTO> doctorInfo =
                doctorRepository.fetchDoctorAvatarInfo(hospitalId, requestDTO.getDoctorId());

        AppointmentStatusDTO appointmentStatusDTO = parseToAppointmentStatusDTO(doctorDutyRosterStatus, doctorInfo);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return appointmentStatusDTO;
    }

    @Override
    public HospitalDeptAppointmentStatusDTO fetchHospitalDeptAppointmentStatusResponseDTO(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS);


        if (requestDTO.getHasAppointmentNumber().equals(YES))
            return searchByAppointmentNumber(requestDTO.getAppointmentNumber());

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus = fetchHospitalDepartmentStatus
                (requestDTO);

        fetchRoomByDepartmentId(hospitalDeptDutyRosterStatus);

        List<HospitalDeptAppointmentStatusResponseDTO> appointments = fetchAppointmentStatus(requestDTO);

        hospitalDeptDutyRosterStatus = setDepartmentAppointmentTimeSlot(requestDTO.getStatus(),
                hospitalDeptDutyRosterStatus, appointments);

        List<HospitalDeptAndWeekdaysDTO> hospitalDepartmentIdsAndWeekDaysList = getHospitalDepartmentIdsAndWeekDays
                (hospitalDeptDutyRosterStatus);

        List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorDTOS = fetchHospitalDeptAndDoctorInfo
                (hospitalDepartmentIdsAndWeekDaysList);

        HospitalDeptAppointmentStatusDTO appointmentStatusDTO = parseToHospitalDeptAppointmentStatusDTO
                (hospitalDeptDutyRosterStatus, hospitalDeptAndDoctorDTOS);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS, getDifferenceBetweenTwoTime(startTime));

        return appointmentStatusDTO;
    }

    @Override
    public List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptAppointmentStatusRoomwise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE);

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus = fetchHospitalDepartmentStatusRoomWise
                (requestDTO);

        List<HospitalDeptAppointmentStatusResponseDTO> appointments = fetchAppointmentStatusRoomWise(requestDTO);

        hospitalDeptDutyRosterStatus = setDepartmentAppointmentTimeSlot(requestDTO.getStatus(),
                hospitalDeptDutyRosterStatus, appointments);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS_ROOM_WISE, getDifferenceBetweenTwoTime(startTime));

        return hospitalDeptDutyRosterStatus;
    }

    @Override
    public HospitalDeptAppointmentStatusCountResponseDTO fetchHospitalDeptAppointmentStatusCount(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS_COUNT);

        List<HospitalDepartmentRosterDetailsDTO> rosterDetailsDTOS = getRosterDetailsForAppointmentStatusCount(requestDTO);

        Long totalAppointmentSlotCount = getTotalAppointmentTimeSlotCount(rosterDetailsDTOS);

        List<AppointmentCountWithStatusDTO> appointmentCountWithStatus = getAppointmentCountWithStatus(requestDTO);

        Long appointmentFollowUpCount = getAppointmentFollowUpCount(requestDTO);

        HospitalDeptAppointmentStatusCountResponseDTO responseDTO =
                getVacantAppointmentSlotsCount(appointmentCountWithStatus,
                        totalAppointmentSlotCount,
                        appointmentFollowUpCount);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_APPOINTMENT_STATUS_COUNT,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private List<HospitalDepartmentRosterDetailsDTO> getRosterDetailsForAppointmentStatusCount(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

        List<HospitalDepartmentRosterDetailsDTO> rosterDetailsDTOS =
                deptDutyRosterRepository.fetchHospitalDepartmentRosterDetails(requestDTO);

        List<HospitalDepartmentRosterDetailsDTO> rosterOverrideDetailsDTOS =
                deptDutyRosterOverrideRepository.fetchHospitalDepartmentRosterOverrideDetails(requestDTO,
                        getRosterIdListForCount(rosterDetailsDTOS));

        List<HospitalDepartmentRosterDetailsDTO> mergedList =
                mergeOverrideAndActualHospitalDeptDutyRosterForCount(rosterOverrideDetailsDTOS, rosterDetailsDTOS);

        return mergedList;
    }

    private AppointmentStatusDTO searchAppointmentByApptNumber(String appointmentNumber) {
        AppointmentDetailsForStatus appointmentDetailsForStatus =
                fetchAppointmentByApptNumber(appointmentNumber, "DOC");

        RosterDetailsForStatus rosterDetailsForStatus = fetchDoctorDutyRosterDetails
                (appointmentDetailsForStatus);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                parseDoctorDutyRosterStatusResponseDTOS(
                        parseToDoctorTimeSlotResponseDTOS(appointmentDetailsForStatus),
                        rosterDetailsForStatus,
                        appointmentDetailsForStatus);

        List<DoctorDropdownDTO> doctorInfo =
                doctorRepository.fetchDoctorAvatarInfo(appointmentDetailsForStatus.getHospitalId(),
                        appointmentDetailsForStatus.getDoctorId());

        return parseToAppointmentStatusDTO(doctorDutyRosterStatus, doctorInfo);
    }


    private RosterDetailsForStatus fetchDoctorDutyRosterDetails(AppointmentDetailsForStatus appointmentDetailsForStatus) {

        RosterDetailsForStatus rosterDetailsForStatus = doctorDutyRosterRepository
                .fetchRosterDetailsToSearchByApptNumber(appointmentDetailsForStatus.getDoctorId(),
                        appointmentDetailsForStatus.getSpecializationId(),
                        appointmentDetailsForStatus.getAppointmentDate());

        if (rosterDetailsForStatus.getHasRosterOverRide().equals(YES))
            doctorDutyRosterOverrideRepository.fetchOverrideRosterDetails(rosterDetailsForStatus,
                    appointmentDetailsForStatus.getAppointmentDate());

        return rosterDetailsForStatus;
    }

    private HospitalDeptAppointmentStatusDTO searchByAppointmentNumber(String appointmentNumber) {

        HospitalDeptAppointmentDetailsForStatus hospitalDeptAppointmentDetailsForStatus = fetchHospitalDeptAppointmentByApptNumber(appointmentNumber);

        RosterDetailsForStatus rosterDetailsForStatus = deptDutyRosterRepository
                .fetchHospitalDepartmentDutyRosterDetailsByDeptId(hospitalDeptAppointmentDetailsForStatus.getHospitalDepartmentId(),
                        hospitalDeptAppointmentDetailsForStatus.getHospitalDepartmentRoomInfoId(),
                        hospitalDeptAppointmentDetailsForStatus.getAppointmentDate());

        if (rosterDetailsForStatus.getHasRosterOverRide().equals(YES))
            deptDutyRosterOverrideRepository.fetchOverrideRosterDetails(rosterDetailsForStatus,
                    hospitalDeptAppointmentDetailsForStatus.getAppointmentDate());

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus =
                parseHospitalDeptDutyRosterStatusResponseDTOS(
                        parseToAppointmentTimeSlotResponseDTOS(hospitalDeptAppointmentDetailsForStatus),
                        rosterDetailsForStatus,
                        hospitalDeptAppointmentDetailsForStatus);

        fetchRoomByDepartmentId(hospitalDeptDutyRosterStatus);

        HospitalDeptAndWeekdaysDTO deptAndWeekdaysDTO = getHospitalDepartmentIdsAndWeekDays(
                hospitalDeptDutyRosterStatus.get(0));

        List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorDTOS = fetchHospitalDeptAndDoctorInfo
                (deptAndWeekdaysDTO);

        HospitalDeptAppointmentStatusDTO appointmentStatusDTO =
                parseToHospitalDeptAppointmentStatusDTO(hospitalDeptDutyRosterStatus,
                        hospitalDeptAndDoctorDTOS);

        return appointmentStatusDTO;
    }

    private HospitalDeptAppointmentDetailsForStatus fetchHospitalDeptAppointmentByApptNumber(String appointmentNumber) {

        return appointmentRepository.fetchHospitalDeptAppointmentByApptNumber(appointmentNumber);

    }

    private AppointmentDetailsForStatus fetchAppointmentByApptNumber(String appointmentNumber,
                                                                     String appointmentServiceTypeCode) {

        return appointmentRepository.fetchAppointmentByApptNumber(appointmentNumber, appointmentServiceTypeCode);

    }

    private void fetchRoomByDepartmentId(List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus) {

        hospitalDeptDutyRosterStatus.forEach(object -> {
            object.setRoomList(roomRepository.fetchActiveMinRoomForAppointmentStatus(
                    object.getHospitalDepartmentId()));
        });

    }

    private List<HospitalDeptAndDoctorDTO> fetchHospitalDeptAndDoctorInfo
            (List<HospitalDeptAndWeekdaysDTO> deptAndWeekdaysDTOS) {

        List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorDTOS = new ArrayList<>();

        deptAndWeekdaysDTOS.forEach(hospitalDeptAndWeekdaysDTO -> {
            HospitalDeptAndDoctorDTO hospitalDeptAndDoctorDTO = hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository
                    .fetchHospitalDeptAndDoctorInfo(hospitalDeptAndWeekdaysDTO);



            hospitalDeptAndDoctorDTOS.add(hospitalDeptAndDoctorDTO);
        });

        return hospitalDeptAndDoctorDTOS;
    }

    private List<HospitalDeptAndDoctorDTO> fetchHospitalDeptAndDoctorInfo
            (HospitalDeptAndWeekdaysDTO deptAndWeekdaysDTO) {

        List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorDTOS = new ArrayList<>();

        HospitalDeptAndDoctorDTO hospitalDeptAndDoctorDTO = hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository
                .fetchHospitalDeptAndDoctorInfo(deptAndWeekdaysDTO);
        hospitalDeptAndDoctorDTOS.add(hospitalDeptAndDoctorDTO);

        return hospitalDeptAndDoctorDTOS;
    }

    private List<HospitalDeptAndWeekdaysDTO> getHospitalDepartmentIdsAndWeekDays
            (List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus) {
        List<HospitalDeptAndWeekdaysDTO> hospitalDeptAndWeekdaysDTOS = new ArrayList<>();

        hospitalDeptDutyRosterStatus.forEach(rosterStatusResponseDTO -> {
            HospitalDeptAndWeekdaysDTO hospitalDeptAndWeekdaysDTO = new HospitalDeptAndWeekdaysDTO();
            hospitalDeptAndWeekdaysDTO.setHospitalDepartmentId(rosterStatusResponseDTO.getHospitalDepartmentId());
            hospitalDeptAndWeekdaysDTO.setWeekDay(rosterStatusResponseDTO.getDate().getDayOfWeek().toString());
            hospitalDeptAndWeekdaysDTOS.add(hospitalDeptAndWeekdaysDTO);
        });

        return hospitalDeptAndWeekdaysDTOS;
    }

    private HospitalDeptAndWeekdaysDTO getHospitalDepartmentIdsAndWeekDays
            (HospitalDeptDutyRosterStatusResponseDTO hospitalDeptDutyRosterStatus) {

        HospitalDeptAndWeekdaysDTO hospitalDeptAndWeekdaysDTO = new HospitalDeptAndWeekdaysDTO();
        hospitalDeptAndWeekdaysDTO.setHospitalDepartmentId(hospitalDeptDutyRosterStatus.getHospitalDepartmentId());
        hospitalDeptAndWeekdaysDTO.setWeekDay(hospitalDeptDutyRosterStatus.getDate().getDayOfWeek().toString());


        return hospitalDeptAndWeekdaysDTO;
    }

    /*IN CASE OF PAST DATE ->
       * IF THERE ANY APPOINTMENTS, THEN SHOW AVAILABLE TIME SLOTS IRRESPECTIVE OF DAY OFF STATUS
       * BUT IF THERE ARE NO APPOINTMENT, THEN SHOW AVAILABLE TIME SLOTS ONLY IF DAY OFF STATUS = 'N'
       *
       * IN CASE OF FUTURE DATE ->
       * SHOW AVAILABLE TIME SLOTS ONLY IF DAY OFF STATUS = 'N'*/
    private List<DoctorDutyRosterStatusResponseDTO> setDoctorTimeSlot(
            String status,
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus,
            List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS) {

        switch (status) {
            case ALL:
                setDoctorTimeSlotForAllAppointmentStatus(doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
            case VACANT:
                setDoctorTimeSlotForVacantAppointmentStatus(doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
            default:
                doctorDutyRosterStatus = setDoctorTimeSlotForSelectedAppointmentStatus
                        (doctorDutyRosterStatus, appointmentStatusResponseDTOS);
                break;
        }

        return doctorDutyRosterStatus;
    }

    /*FETCH DOCTOR DUTY ROSTER FROM DOCTOR_DUTY_ROSTER_OVERRIDE FIRST
      AND THEN DOCTOR_DUTY ROSTER. THEN MERGE BOTH ROSTERS BASED ON THE REQUESTED SEARCH DATE, DOCTOR AND SPECIALIZATION*/
    private List<DoctorDutyRosterStatusResponseDTO> fetchDoctorStatus(AppointmentStatusRequestDTO requestDTO,
                                                                      Long hospitalId) {

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterOverrideStatus =
                doctorDutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO, hospitalId);

        List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus =
                doctorDutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO, hospitalId);

        if (doctorDutyRosterOverrideStatus.isEmpty() && doctorDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(DoctorDutyRoster.class);

        return mergeOverrideAndActualDoctorDutyRoster(doctorDutyRosterOverrideStatus, doctorDutyRosterStatus);
    }

    /*FETCH APPOINTMENT DETAILS WITHIN SELECTED DATE RANGE*/
    private List<AppointmentStatusResponseDTO> fetchAppointmentStatus(AppointmentStatusRequestDTO requestDTO,
                                                                      Long hospitalId) {
        return appointmentService.fetchAppointmentForAppointmentStatus(requestDTO, hospitalId);
    }

    /*IF DOCTOR DAY OFF STATUS= 'Y', THEN THERE ARE NO ANY TIME SLOTS
     * IF DAY OFF STATUS = 'N' :
     * IF APPOINTMENT EXISTS WITHIN SELECTED DOCTOR DUTY ROSTER RANGE, THEN FILTER APPOINTMENT STATUS ACCORDINGLY
     * ELSE APPOINTMENT STATUS IS VACANT FOR ALL TIME SLOTS*/
    private void setDoctorTimeSlotForAllAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
            List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<DoctorTimeSlotResponseDTO> doctorTimeSlots = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(doctorDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots, appointments);
            } else {
                if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForAllAppointmentStatus(doctorDutyRosterStatusResponseDTO, doctorTimeSlots, appointments);
            }
        }
    }

    private void setTimeSlotForAllAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots,
             List<AppointmentStatusResponseDTO> appointments) {

        List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasAppointment(appointment, doctorDutyRosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            setTimeSlotHavingAppointments(appointmentMatchedWithRoster, doctorTimeSlots);

            doctorTimeSlots = calculateTimeSlotsForAll(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);
        } else {
            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                doctorTimeSlots = calculateTimeSlotsForAll(doctorDutyRosterStatusResponseDTO, doctorTimeSlots);
        }

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }

    /*FOR SIMPLICITY, ADD ALL MATCHED APPOINTMENT LIST INTO FINAL LIST*/
    private void setTimeSlotHavingAppointments(List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster,
                                               List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        for (AppointmentStatusResponseDTO appointment : appointmentMatchedWithRoster) {
            parseAppointmentDetails(appointment, doctorTimeSlots);
        }
    }

    private List<DoctorTimeSlotResponseDTO> calculateTimeSlotsForAll(
            DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
            List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

        return calculateTimeSlotsForAllAppointmentStatus(
                doctorDutyRosterStatusResponseDTO.getDate(),
                doctorDutyRosterStatusResponseDTO.getStartTime(),
                doctorDutyRosterStatusResponseDTO.getEndTime(),
                doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                doctorTimeSlots);
    }

    private void setDoctorTimeSlotForVacantAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatusResponseDTOS,
            List<AppointmentStatusResponseDTO> appointments) {

        for (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO : doctorDutyRosterStatusResponseDTOS) {

            doctorDutyRosterStatusResponseDTO.setWeekDayName(
                    doctorDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<DoctorTimeSlotResponseDTO> doctorTimeSlots = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(doctorDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForVacantAppointmentStatus
                        (doctorDutyRosterStatusResponseDTO, appointments, doctorTimeSlots);

            } else {
                if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForVacantAppointmentStatus
                            (doctorDutyRosterStatusResponseDTO, appointments, doctorTimeSlots);
            }
        }
    }

    private void setTimeSlotForVacantAppointmentStatus
            (DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
             List<AppointmentStatusResponseDTO> appointments,
             List<DoctorTimeSlotResponseDTO> doctorTimeSlots) {

         /*FILTER APPOINTMENT THAT MATCHES WITH DOCTOR DUTY ROSTER INFO*/
        List<AppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasAppointment(appointment, doctorDutyRosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            /*JOIN MATCHED APPOINTMENTS INTO COMMA SEPARATED STRING eg. 10:00-PA, 10:20-PA*/
            String matchedAppointmentWithStatus =
                    appointmentMatchedWithRoster.stream()
                            .map(AppointmentStatusResponseDTO::getAppointmentTimeDetails)
                            .collect(Collectors.joining(COMMA_SEPARATED));

            doctorTimeSlots = calculateTimeSlotsForVacantAppointmentStatus(
                    doctorDutyRosterStatusResponseDTO.getDate(),
                    doctorDutyRosterStatusResponseDTO.getStartTime(),
                    doctorDutyRosterStatusResponseDTO.getEndTime(),
                    doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                    matchedAppointmentWithStatus,
                    doctorTimeSlots
            );
        } else {

            if (doctorDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {

                doctorTimeSlots = calculateTimeSlotsForVacantAppointmentStatus(
                        doctorDutyRosterStatusResponseDTO.getDate(),
                        doctorDutyRosterStatusResponseDTO.getStartTime(),
                        doctorDutyRosterStatusResponseDTO.getEndTime(),
                        doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                        null,
                        doctorTimeSlots
                );
            }
        }

        doctorDutyRosterStatusResponseDTO.setDoctorTimeSlots(doctorTimeSlots);
    }


   /*IF STATUS IN SEARCH DTO IS NOT EMPTY AND IS NOT VACANT (i.e,PA/A/C),
     THEN RETURN ONLY APPOINTMENT DETAILS WITH RESPECTIVE STATUS.
     NO NEED TO FILTER WITH DOCTOR DUTY ROSTER RANGE*/

    /* FIRST VALIDATE IF DUTY ROSTER DATE IS BEFORE CURRENT DATE THEN SHOW ALL APPOINTMENTS IRRESPECTIVE OF DAYOFF
    * ELSE SHOW APPOINTMENT ONLY IF DAYOFF IS 'N'
    *
    * eg. DDR DATE = 2020-01-20, CURRENT DATE = 2020-01-22, DAYOFF = 'Y'/'N', APPOINTMENT = 2020-01-20
     * SEARCH = 2020-01-20 to 2020-01-20 -> SHOW ALL APPOINTMENT AS PER STATUS
    *   DDR DATE = 2020-01-23, CURRENT DATE = 2020-01-22, DAYOFF = 'Y', APPOINTMENT = 2020-01-22
     * SEARCH = 2020-01-22 to 2020-01-23 -> NO RECORDS BECAUSE DAYOFF ='Y'
    * */
    private List<DoctorDutyRosterStatusResponseDTO> setDoctorTimeSlotForSelectedAppointmentStatus(
            List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterStatus,
            List<AppointmentStatusResponseDTO> appointments) {

        /*THROW EXCEPTION IF NO APPOINTMENT EXISTS FOR THE SELECTED STATUS*/
        if (appointments.isEmpty()) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT);
            throw new NoContentFoundException(Appointment.class);
        }

        /*FILTER OUT FROM DOCTOR DUTY ROSTERS SUCH THAT IT CONTAINS ONLY THOSE ROSTERS HAVING
        * APPOINTMENT*/
        List<DoctorDutyRosterStatusResponseDTO> rostersWithAppointment = doctorDutyRosterStatus.stream()
                .filter(doctorDutyRoster -> (appointments.stream()
                        .anyMatch(appointment -> hasAppointment(appointment, doctorDutyRoster)))
                )
                .collect(Collectors.toList());

         /*ADD TO LIST ONLY IF DOCTOR DAY OFF STATUS IS 'N'
         * AND APPOINTMENT CONDITION MATCHES*/
        rostersWithAppointment.forEach(doctorDutyRoster -> {
            List<DoctorTimeSlotResponseDTO> doctorTimeSlotResponseDTOS = new ArrayList<>();
            appointments
                    .stream()
                    .filter(appointment -> hasAppointment(appointment, doctorDutyRoster))
                    .forEach(appointment -> {

                        doctorDutyRoster.setWeekDayName(appointment.getDate().getDayOfWeek().toString());

                        boolean isDateBefore = convertLocalDateToDate(appointment.getDate()).before(new Date());

                        if (isDateBefore) {
                            parseAppointmentDetails(appointment, doctorTimeSlotResponseDTOS);
                        } else {
                            if (doctorDutyRoster.getDayOffStatus().equals(NO))
                                parseAppointmentDetails(appointment, doctorTimeSlotResponseDTOS);
                        }

                        doctorDutyRoster.setDoctorTimeSlots(doctorTimeSlotResponseDTOS);
                    });
        });

        return rostersWithAppointment;
    }

    private boolean hasAppointment(AppointmentStatusResponseDTO appointment,
                                   DoctorDutyRosterStatusResponseDTO doctorDutyRosterStatus) {

        return appointment.getDate().equals(doctorDutyRosterStatus.getDate())
                && (appointment.getDoctorId().equals(doctorDutyRosterStatus.getDoctorId()))
                && (appointment.getSpecializationId().equals(doctorDutyRosterStatus.getSpecializationId()));
    }

    private List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDepartmentStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus =
                deptDutyRosterRepository.fetchHospitalDeptDutyRosterStatus(requestDTO);

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterOverrideStatus =
                deptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideStatus(
                        requestDTO, getRosterIdList(hospitalDeptDutyRosterStatus));

        if (hospitalDeptDutyRosterOverrideStatus.isEmpty() && hospitalDeptDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(HospitalDepartmentDutyRoster.class);

        return mergeOverrideAndActualHospitalDeptDutyRoster(hospitalDeptDutyRosterOverrideStatus, hospitalDeptDutyRosterStatus);
    }

    private List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDepartmentStatusRoomWise
            (HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterStatus =
                deptDutyRosterRepository.fetchHospitalDeptDutyRosterStatusRoomWise(requestDTO);

        List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterOverrideStatus =
                deptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideStatus(
                        requestDTO, getRosterIdList(hospitalDeptDutyRosterStatus));

        if (hospitalDeptDutyRosterOverrideStatus.isEmpty() && hospitalDeptDutyRosterStatus.isEmpty())
            throw new NoContentFoundException(DoctorDutyRoster.class);

        return mergeOverrideAndActualHospitalDeptDutyRoster(hospitalDeptDutyRosterOverrideStatus, hospitalDeptDutyRosterStatus);
    }

    private List<Long> getRosterIdList(List<HospitalDeptDutyRosterStatusResponseDTO> rosterStatusResponseDTOS) {
        return rosterStatusResponseDTOS
                .stream()
                .map(HospitalDeptDutyRosterStatusResponseDTO::getHospitalDepartmentDutyRosterId)
                .collect(Collectors.toList());
    }

    private List<HospitalDeptDutyRosterStatusResponseDTO> setDepartmentAppointmentTimeSlot(
            String status,
            List<HospitalDeptDutyRosterStatusResponseDTO> dutyRosterStatusResponseDTOS,
            List<HospitalDeptAppointmentStatusResponseDTO> deaprtmentAppointmentStatusResponseDTOS) {

        switch (status) {

            case ALL:
                setAppointmentTimeSlotForAllDepartmentAppointmentStatus(dutyRosterStatusResponseDTOS,
                        deaprtmentAppointmentStatusResponseDTOS);
                break;

            case VACANT:
                setAppointmentTimeSlotForVacantDepartmentAppointmentStatus(dutyRosterStatusResponseDTOS,
                        deaprtmentAppointmentStatusResponseDTOS);
                break;

            default:
                dutyRosterStatusResponseDTOS = setAppointmentTimeSlotForSelectedDepartmentAppointmentStatus(
                        dutyRosterStatusResponseDTOS,
                        deaprtmentAppointmentStatusResponseDTOS);
                break;
        }

        return dutyRosterStatusResponseDTOS;
    }

    private List<HospitalDeptAppointmentStatusResponseDTO> fetchAppointmentStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        return appointmentRepository.fetchHospitalDeptAppointmentForAppointmentStatus(requestDTO);

    }

    private List<HospitalDeptAppointmentStatusResponseDTO> fetchAppointmentStatusRoomWise(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        return appointmentRepository.fetchHospitalDeptAppointmentForAppointmentStatusRoomWise(requestDTO);

    }

    private List<AppointmentCountWithStatusDTO> getAppointmentCountWithStatus(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

        return appointmentRepository.getAppointmentCountWithStatus(requestDTO);

    }

    private Long getAppointmentFollowUpCount(
            HospitalDeptAppointmentStatusCountRequestDTO requestDTO) {

        return appointmentRepository.getAppointmentFollowUpCount(requestDTO);

    }

    private void setAppointmentTimeSlotForAllDepartmentAppointmentStatus(
            List<HospitalDeptDutyRosterStatusResponseDTO> rosterStatusResponseDTOS,
            List<HospitalDeptAppointmentStatusResponseDTO> appointments) {

        for (HospitalDeptDutyRosterStatusResponseDTO hospitalDeptDutyRosterStatusResponseDTO : rosterStatusResponseDTOS) {

            hospitalDeptDutyRosterStatusResponseDTO.setWeekDayName(
                    hospitalDeptDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<AppointmentTimeSlotResponseDTO> timeSlotResponseDTOS = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(hospitalDeptDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForAllDepartmentAppointmentStatus(hospitalDeptDutyRosterStatusResponseDTO,
                        timeSlotResponseDTOS,
                        appointments);
            } else {
                if (hospitalDeptDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForAllDepartmentAppointmentStatus(hospitalDeptDutyRosterStatusResponseDTO,
                            timeSlotResponseDTOS,
                            appointments);
            }
        }
    }

    private void setTimeSlotForAllDepartmentAppointmentStatus
            (HospitalDeptDutyRosterStatusResponseDTO rosterStatusResponseDTO,
             List<AppointmentTimeSlotResponseDTO> appointmentTimeSlots,
             List<HospitalDeptAppointmentStatusResponseDTO> appointments) {

        List<HospitalDeptAppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasDepartmentAppointment(appointment, rosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            setTimeSlotHavingDepartmentAppointments(appointmentMatchedWithRoster, appointmentTimeSlots);

            appointmentTimeSlots = calculateTimeSlotsForAll(rosterStatusResponseDTO, appointmentTimeSlots);
        } else {
            if (rosterStatusResponseDTO.getDayOffStatus().equals(NO))
                appointmentTimeSlots = calculateTimeSlotsForAll(rosterStatusResponseDTO, appointmentTimeSlots);
        }

        rosterStatusResponseDTO.setAppointmentTimeSlots(appointmentTimeSlots);
    }

    private boolean hasDepartmentAppointment(HospitalDeptAppointmentStatusResponseDTO appointment,
                                             HospitalDeptDutyRosterStatusResponseDTO rosterStatusResponseDTO) {

        if (Objects.isNull(rosterStatusResponseDTO.getHospitalDepartmentRoomInfoId())
                && Objects.isNull(appointment.getHospitalDepartmentRoomInfoId())) {
            return appointment.getDate().equals(rosterStatusResponseDTO.getDate())
                    && (appointment.getDepartmentId().equals(rosterStatusResponseDTO.getHospitalDepartmentId()));
        } else {
            return appointment.getDate().equals(rosterStatusResponseDTO.getDate())
                    && (appointment.getDepartmentId().equals(rosterStatusResponseDTO.getHospitalDepartmentId())
                    && appointment.getHospitalDepartmentRoomInfoId().equals(rosterStatusResponseDTO.getHospitalDepartmentRoomInfoId()));
        }
    }

    private void setTimeSlotHavingDepartmentAppointments(
            List<HospitalDeptAppointmentStatusResponseDTO> appointmentMatchedWithRoster,
            List<AppointmentTimeSlotResponseDTO> doctorTimeSlots) {

        for (HospitalDeptAppointmentStatusResponseDTO appointment : appointmentMatchedWithRoster) {
            parseDepartmentAppointmentDetails(appointment, doctorTimeSlots);
        }
    }

    private List<AppointmentTimeSlotResponseDTO> calculateTimeSlotsForAll(
            HospitalDeptDutyRosterStatusResponseDTO doctorDutyRosterStatusResponseDTO,
            List<AppointmentTimeSlotResponseDTO> doctorTimeSlots) {

        return calculateTimeSlotsForAllDepartmentAppointmentStatus(
                doctorDutyRosterStatusResponseDTO.getDate(),
                doctorDutyRosterStatusResponseDTO.getStartTime(),
                doctorDutyRosterStatusResponseDTO.getEndTime(),
                doctorDutyRosterStatusResponseDTO.getRosterGapDuration(),
                doctorTimeSlots);
    }

    private void setAppointmentTimeSlotForVacantDepartmentAppointmentStatus(
            List<HospitalDeptDutyRosterStatusResponseDTO> departmentDutyRosterStatusResponseDTOS,
            List<HospitalDeptAppointmentStatusResponseDTO> appointments) {

        for (HospitalDeptDutyRosterStatusResponseDTO departmentDutyRosterStatusResponseDTO :
                departmentDutyRosterStatusResponseDTOS) {

            departmentDutyRosterStatusResponseDTO.setWeekDayName(
                    departmentDutyRosterStatusResponseDTO.getDate().getDayOfWeek().toString());

            List<AppointmentTimeSlotResponseDTO> appointmentTimeSlots = new ArrayList<>();

            boolean isDateBefore = convertLocalDateToDate(departmentDutyRosterStatusResponseDTO.getDate())
                    .before(new Date());

            if (isDateBefore) {
                setTimeSlotForVacantDepartmentAppointmentStatus
                        (departmentDutyRosterStatusResponseDTO, appointments, appointmentTimeSlots);

            } else {
                if (departmentDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO))
                    setTimeSlotForVacantDepartmentAppointmentStatus
                            (departmentDutyRosterStatusResponseDTO, appointments, appointmentTimeSlots);
            }
        }
    }

    private void setTimeSlotForVacantDepartmentAppointmentStatus
            (HospitalDeptDutyRosterStatusResponseDTO hospitalDeptDutyRosterStatusResponseDTO,
             List<HospitalDeptAppointmentStatusResponseDTO> appointments,
             List<AppointmentTimeSlotResponseDTO> appointmentTimeSlots) {

        List<HospitalDeptAppointmentStatusResponseDTO> appointmentMatchedWithRoster =
                appointments.stream()
                        .filter(appointment -> hasDepartmentAppointment(appointment, hospitalDeptDutyRosterStatusResponseDTO))
                        .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(appointmentMatchedWithRoster)) {

            String matchedAppointmentWithStatus =
                    appointmentMatchedWithRoster.stream()
                            .map(HospitalDeptAppointmentStatusResponseDTO::getAppointmentTimeDetails)
                            .collect(Collectors.joining(COMMA_SEPARATED));

            appointmentTimeSlots = calculateTimeSlotsForVacantDepartmentAppointmentStatus(
                    hospitalDeptDutyRosterStatusResponseDTO.getDate(),
                    hospitalDeptDutyRosterStatusResponseDTO.getStartTime(),
                    hospitalDeptDutyRosterStatusResponseDTO.getEndTime(),
                    hospitalDeptDutyRosterStatusResponseDTO.getRosterGapDuration(),
                    matchedAppointmentWithStatus,
                    appointmentTimeSlots
            );
        } else {

            if (hospitalDeptDutyRosterStatusResponseDTO.getDayOffStatus().equals(NO)) {

                appointmentTimeSlots = calculateTimeSlotsForVacantDepartmentAppointmentStatus(
                        hospitalDeptDutyRosterStatusResponseDTO.getDate(),
                        hospitalDeptDutyRosterStatusResponseDTO.getStartTime(),
                        hospitalDeptDutyRosterStatusResponseDTO.getEndTime(),
                        hospitalDeptDutyRosterStatusResponseDTO.getRosterGapDuration(),
                        null,
                        appointmentTimeSlots
                );
            }
        }

        hospitalDeptDutyRosterStatusResponseDTO.setAppointmentTimeSlots(appointmentTimeSlots);
    }

    private List<HospitalDeptDutyRosterStatusResponseDTO> setAppointmentTimeSlotForSelectedDepartmentAppointmentStatus(
            List<HospitalDeptDutyRosterStatusResponseDTO> departmentDutyRosterStatus,
            List<HospitalDeptAppointmentStatusResponseDTO> appointments) {

        if (appointments.isEmpty()) {
            log.error(CONTENT_NOT_FOUND, APPOINTMENT);
            throw new NoContentFoundException(Appointment.class);
        }

        List<HospitalDeptDutyRosterStatusResponseDTO> rostersWithAppointment = departmentDutyRosterStatus.stream()
                .filter(doctorDutyRoster -> (appointments.stream()
                        .anyMatch(appointment -> hasDepartmentAppointment(appointment, doctorDutyRoster)))
                )
                .collect(Collectors.toList());

        rostersWithAppointment.forEach(doctorDutyRoster -> {
            List<AppointmentTimeSlotResponseDTO> appointmentTimeSlotResponseDTOS = new ArrayList<>();
            appointments
                    .stream()
                    .filter(appointment -> hasDepartmentAppointment(appointment, doctorDutyRoster))
                    .forEach(appointment -> {

                        doctorDutyRoster.setWeekDayName(appointment.getDate().getDayOfWeek().toString());

                        boolean isDateBefore = convertLocalDateToDate(appointment.getDate()).before(new Date());

                        if (isDateBefore) {
                            parseDepartmentAppointmentDetails(appointment, appointmentTimeSlotResponseDTOS);
                        } else {
                            if (doctorDutyRoster.getDayOffStatus().equals(NO))
                                parseDepartmentAppointmentDetails(appointment, appointmentTimeSlotResponseDTOS);
                        }

                        doctorDutyRoster.setAppointmentTimeSlots(appointmentTimeSlotResponseDTOS);
                    });
        });

        return rostersWithAppointment;
    }

    private List<Long> getRosterIdListForCount(List<HospitalDepartmentRosterDetailsDTO> rosterStatusResponseDTOS) {
        return rosterStatusResponseDTOS
                .stream()
                .map(HospitalDepartmentRosterDetailsDTO::getRosterId)
                .collect(Collectors.toList());
    }

}
