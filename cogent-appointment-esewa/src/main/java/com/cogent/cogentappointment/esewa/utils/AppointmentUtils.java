package com.cogent.cogentappointment.esewa.utils;

import com.cogent.cogentappointment.esewa.dto.request.appointment.reschedule.AppointmentRescheduleRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.save.AppointmentRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.cancel.AppointmentCancelResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.*;
import com.cogent.cogentappointment.esewa.dto.response.appointment.save.AppointmentSuccessResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_APPOINTMENT_DATE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_APPOINTMENT_DATE_TIME;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.AppointmentStatusConstants.*;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti on 2019-10-24
 */
@Slf4j
public class AppointmentUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    /*VALIDATE IF REQUESTED DATE AND TIME IS BEFORE CURRENT DATE AND TIME*/
    public static void validateIfRequestIsBeforeCurrentDateTime(Date appointmentDate,
                                                                String appointmentTime) {

        Date requestDateTime = parseAppointmentTime(appointmentDate, appointmentTime);

        Date currentDateTime = new Date();

        boolean isRequestedBeforeCurrentDateTime = requestDateTime.before(currentDateTime);

        if (isRequestedBeforeCurrentDateTime) {
            log.error(INVALID_APPOINTMENT_DATE_TIME);
            throw new BadRequestException(INVALID_APPOINTMENT_DATE_TIME);
        }
    }

    public static void validateIfRequestIsPastDate(Date requestedDate) {

        boolean isDateBefore = removeTime(requestedDate).before(removeTime(new Date()));
        if (isDateBefore)
            throw new BadRequestException(INVALID_APPOINTMENT_DATE);
    }

    private static boolean hasTimePassed(Date date, String time) {

        Date availableDateTime = parseAppointmentTime(date, time);

        Date currentDate = new Date();

        return availableDateTime.before(currentDate);
    }

    /*VALIDATE IF REQUESTED APPOINTMENT TIME LIES BETWEEN DOCTOR DUTY ROSTER TIME SCHEDULES
     * IF IT MATCHES, THEN DO NOTHING
     * ELSE REQUESTED TIME IS INVALID AND THUS CANNOT TAKE AN APPOINTMENT*/
    public static boolean validateIfRequestedAppointmentTimeIsValid(DoctorDutyRosterTimeResponseDTO doctorDutyRosterInfo,
                                                                    String appointmentTime) {

        final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

        String doctorStartTime = getTimeFromDate(doctorDutyRosterInfo.getStartTime());
        String doctorEndTime = getTimeFromDate(doctorDutyRosterInfo.getEndTime());

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(doctorStartTime));

        do {
            String date = FORMAT.print(startDateTime);

            final Duration rosterGapDuration = Minutes.minutes(doctorDutyRosterInfo.getRosterGapDuration())
                    .toStandardDuration();

            if (date.equals(appointmentTime))
                return true;

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(doctorEndTime)) <= 0);

        return false;
    }

    public static Appointment parseToAppointment(String hyphenatedAppointmentNumber,
                                                 String appointmentNumber,
                                                 AppointmentRequestDTO requestDTO,
                                                 Date appointmentDate,
                                                 Date appointmentTime,
                                                 Character isSelf,
                                                 Patient patient,
                                                 Hospital hospital,
                                                 AppointmentMode appointmentMode,
                                                 HospitalAppointmentServiceType hospitalAppointmentServiceType) {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setAppointmentNumber(appointmentNumber);
        appointment.setHyphenatedAppointmentNumber(hyphenatedAppointmentNumber);
        appointment.setCreatedDateNepali(requestDTO.getCreatedDateNepali());
        appointment.setIsFollowUp(requestDTO.getIsFollowUp());
        appointment.setIsSelf(isSelf);
        appointment.setAppointmentModeId(appointmentMode);
        appointment.setHospitalId(hospital);
        appointment.setPatientId(patient);
        appointment.setHospitalAppointmentServiceType(hospitalAppointmentServiceType);
        appointment.setStatus(PENDING_APPROVAL);
        appointment.setSerialNumber(generateRandomNumber(6));
        return appointment;
    }

    public static AppointmentDoctorInfo parseAppointmentDoctorInfo(Appointment appointment,
                                                                   Doctor doctor,
                                                                   Specialization specialization) {

        AppointmentDoctorInfo appointmentDoctorInfo = new AppointmentDoctorInfo();
        appointmentDoctorInfo.setAppointment(appointment);
        appointmentDoctorInfo.setDoctor(doctor);
        appointmentDoctorInfo.setSpecialization(specialization);

        return appointmentDoctorInfo;
    }

    public static AppointmentHospitalDepartmentInfo parseAppointmentHospitalDepartmentInfo(
            Appointment appointment,
            HospitalDepartment hospitalDepartment,
            HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo,
            HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo) {

        AppointmentHospitalDepartmentInfo appointmentHospitalDepartmentInfo = new AppointmentHospitalDepartmentInfo();
        appointmentHospitalDepartmentInfo.setAppointment(appointment);
        appointmentHospitalDepartmentInfo.setHospitalDepartment(hospitalDepartment);
        appointmentHospitalDepartmentInfo.setHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfo);
        appointmentHospitalDepartmentInfo.setHospitalDepartmentBillingModeInfo(hospitalDepartmentBillingModeInfo);
        return appointmentHospitalDepartmentInfo;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate), Objects.requireNonNull(parseTime(appointmentTime)));
    }

    public static AppointmentSuccessResponseDTO parseToAppointmentSuccessResponseDTO(String appointmentNumber,
                                                                                     Character transactionStatus,
                                                                                     Double refundPercentage) {
        return AppointmentSuccessResponseDTO.builder()
                .appointmentNumber(appointmentNumber)
                .appointmentTransactionStatus(transactionStatus)
                .refundPercentage(refundPercentage)
                .responseStatus(CREATED)
                .responseCode(CREATED.value())
                .build();
    }

    public static void parseAppointmentCancelledDetails(Appointment appointment,
                                                        String remarks) {

        appointment.setRemarks(remarks);
        appointment.setStatus(CANCELLED);
    }

    public static AppointmentRefundDetail parseToAppointmentRefundDetail(Appointment appointment,
                                                                         Double refundAmount) {

        AppointmentRefundDetail refundDetail = new AppointmentRefundDetail();
        refundDetail.setAppointmentId(appointment);
        refundDetail.setRefundAmount(refundAmount);
        refundDetail.setStatus(PENDING_APPROVAL);
        refundDetail.setCancelledDate(new Date());
        return refundDetail;
    }

    /*startingFiscalYear = 2076-04-01
     * startingYear = 2076
     * splitStartingYear = 76
     *
     * endingFiscalYear = 2077-03-01
     * endingYear = 2077
     * splitEndingYear = 77
     *
     * APPOINTMENT NUMBER IS GENERATED IN FORMAT : 76-77-0001
     * (fiscal year start- fiscal year end – unique appointment no)
     * appointment number starts with ‘0001’ and increments by 1 & starts with ‘0001’ again in next
     * fiscal year.
     *
     * results[0] = start fiscal year
     * results[1] = end fiscal year
     * results[2] = appointment number*/
    public static String generateAppointmentNumber(List<String> results,
                                                   String startingFiscalYear,
                                                   String endingFiscalYear,
                                                   String hospitalCode) {

        System.out.println("ENTERING APPOINTMENT UTILS FOR APPOINTMENT NUMBER GENERATION--------------");
        System.out.println("hospitalCode----------->"+hospitalCode);
        System.out.println("results----------->"+results);
        System.out.println("startingFiscalYear----------->"+startingFiscalYear);
        System.out.println("endingFiscalYear----------->"+endingFiscalYear);

        String startingYear = startingFiscalYear.split(HYPHEN)[0];
        String splitStartingYear = startingYear.substring(startingYear.length() - 2);

        String endingYear = endingFiscalYear.split(HYPHEN)[0];
        String splitEndingYear = endingYear.substring(endingYear.length() - 2);

        String appointmentNumber;

        if (results.isEmpty())
            appointmentNumber = "1";
        else

            //results = CHEERS-76-77-1; CHEERS-76-77-2
            appointmentNumber = extractAppointmentNumber(results);

        appointmentNumber = hospitalCode.concat(HYPHEN)
                .concat(splitStartingYear).concat(HYPHEN)
                .concat(splitEndingYear).concat(HYPHEN)
                .concat(appointmentNumber);

        return appointmentNumber;
    }

    private static String extractAppointmentNumber(List<String> results) {

        String incrementNumber = results.get(0).split(HYPHEN)[3];
        return String.format("%01d", Integer.parseInt(incrementNumber) + 1);

    }

    public static List<String> calculateAvailableTimeSlots(String startTime,
                                                           String endTime,
                                                           Duration rosterGapDuration,
                                                           List<AppointmentBookedTimeResponseDTO> bookedAppointments) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if (!isAppointmentDateMatched(bookedAppointments, date))
                availableTimeSlots.add(date);

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
    }

    /*ADD ONLY TIME AFTER AFTER CURRENT TIME*/
    public static List<String> calculateCurrentAvailableTimeSlots(String startTime,
                                                                  String endTime,
                                                                  Duration rosterGapDuration,
                                                                  List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                                  Date requestedDate) {

        List<String> availableTimeSlots = new ArrayList<>();

        DateTime startDateTime = new DateTime(FORMAT.parseDateTime(startTime));

        do {
            String date = FORMAT.print(startDateTime);

            if ((!isAppointmentDateMatched(bookedAppointments, date)) && (!hasTimePassed(requestedDate, date)))
                availableTimeSlots.add(date);

            startDateTime = startDateTime.plus(rosterGapDuration);
        } while (startDateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return availableTimeSlots;
    }

    public static AppointmentCheckAvailabilityResponseDTO parseToAvailabilityResponse(
            String startTime,
            String endTime,
            Date queryDate,
            List<String> availableTimeSlots) {

        return AppointmentCheckAvailabilityResponseDTO.builder()
                .queryDate(queryDate)
                .doctorAvailableTime(startTime + HYPHEN + endTime)
                .availableTimeSlots(availableTimeSlots)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    private static boolean isAppointmentDateMatched(List<AppointmentBookedTimeResponseDTO> bookedAppointments,
                                                    String date) {
        return bookedAppointments.stream()
                .anyMatch(bookedAppointment -> bookedAppointment.getAppointmentTime().equals(date));
    }

    public static void updateAppointmentDetails(Appointment appointment,
                                                AppointmentRescheduleRequestDTO rescheduleRequestDTO) {

        appointment.setAppointmentDate(rescheduleRequestDTO.getRescheduleDate());

        appointment.setAppointmentTime(parseAppointmentTime(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime())
        );

        appointment.setRemarks(rescheduleRequestDTO.getRemarks());
    }

    public static AppointmentRescheduleLog parseToAppointmentRescheduleLog(
            Appointment appointment,
            AppointmentRescheduleRequestDTO rescheduleRequestDTO,
            AppointmentRescheduleLog appointmentRescheduleLog) {

        appointmentRescheduleLog.setAppointmentId(appointment);

        appointmentRescheduleLog.setPreviousAppointmentDate(appointment.getAppointmentTime());

        appointmentRescheduleLog.setRescheduleDate(parseAppointmentTime(
                rescheduleRequestDTO.getRescheduleDate(),
                rescheduleRequestDTO.getRescheduleTime())
        );

        appointmentRescheduleLog.setRemarks(rescheduleRequestDTO.getRemarks());

        appointmentRescheduleLog.setStatus(RESCHEDULED);

        return appointmentRescheduleLog;
    }

    public static StatusResponseDTO parseToStatusResponseDTO() {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setResponseCode(OK.value());
        statusResponseDTO.setResponseStatus(OK);
        return statusResponseDTO;
    }

    public static AppointmentMinResponseWithStatusDTO parseToAppointmentMinResponseWithStatusDTO(
            List<AppointmentMinResponseDTO> minResponseDTOList) {

        return AppointmentMinResponseWithStatusDTO.builder()
                .appointmentMinResponseDTOS(minResponseDTOList)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentDetailResponseWithStatusDTO parseToAppointmentDetailResponseWithStatusDTO(
            AppointmentDetailResponseDTO responseDTO) {

        return AppointmentDetailResponseWithStatusDTO.builder()
                .appointmentDetailResponseDTO(responseDTO)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentStatistics parseAppointmentStatisticsForNew(Appointment appointment) {
        AppointmentStatistics appointmentStatistics = new AppointmentStatistics();
        appointmentStatistics.setAppointmentId(appointment);
        appointmentStatistics.setAppointmentCreatedDate(new Date());
        appointmentStatistics.setIsNew(YES);

        return appointmentStatistics;
    }

    public static AppointmentStatistics parseAppointmentStatisticsForRegistered(Appointment appointment) {
        AppointmentStatistics appointmentStatistics = new AppointmentStatistics();
        appointmentStatistics.setAppointmentId(appointment);
        appointmentStatistics.setAppointmentCreatedDate(new Date());
        appointmentStatistics.setIsRegistered(YES);

        return appointmentStatistics;
    }

    public static AppointmentResponseWithStatusDTO parseToAppointmentHistory(
            List<AppointmentResponseDTO> appointmentHistory) {

        return AppointmentResponseWithStatusDTO.builder()
                .appointments(appointmentHistory)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentCancelResponseDTO parseAppointmentCancelResponse(Double appointmentAmount,
                                                                              Double refundAmount) {
        return AppointmentCancelResponseDTO.builder()
                .appointmentAmount(appointmentAmount)
                .refundAmount(refundAmount)
                .message("Your appointment has been cancelled successfully. Your refund request has been proceeded!!")
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();
    }

    public static AppointmentEsewaRequest parseToAppointmentEsewaRequest(Appointment appointment,
                                                                         String esewaId) {

        AppointmentEsewaRequest appointmentEsewaRequest = new AppointmentEsewaRequest();
        appointmentEsewaRequest.setAppointment(appointment);
        appointmentEsewaRequest.setEsewaId(esewaId);

        return appointmentEsewaRequest;
    }

}
