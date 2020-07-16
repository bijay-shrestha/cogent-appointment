package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.LastModifiedAppointmentIdAndStatus;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.OverrideDatesResponseDTO;
import com.cogent.cogentappointment.persistence.model.*;
import org.apache.commons.collections4.ListUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferUtils {
    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static List<Date> mergeOverrideAndActualDateList(
            List<Date> overrideAvailableList,
            List<Date> actualList,
            List<Date> overrideDayOffList) {

        List<Date> unmatchedList = actualList.stream()
                .filter(actual -> (overrideAvailableList.stream()
                        .filter(override -> (override.equals(actual)))
                        .count()) < 1)
                .collect(Collectors.toList());


        List<Date> matchedList = actualList.stream()
                .filter(actual -> (overrideDayOffList.stream()
                        .filter(override -> (override.equals(actual)))
                        .anyMatch(override -> override.equals(actual))))
                .collect(Collectors.toList());

        List<Date> dates = ListUtils.union(unmatchedList, overrideAvailableList);

        dates.removeAll(matchedList);

//        overrideList.removeIf(override -> override.getDayOffStatus().equals(YES));
        Collections.sort(dates);

        return dates;
    }

    public static List<Date> getActualdate(List<String> dayOffDay, List<Date> dates) {
        List<Date> unmatched = dates.stream()
                .filter(actualDate -> dayOffDay.stream()
                        .filter(weekDay -> weekDay.equals(actualDate.toString().substring(0, 3).toUpperCase()))
                        .count() < 1)
                .collect(Collectors.toList());
        return unmatched;
    }

    public static List<String> getGapDuration(String startTime, String endTime, Integer gapDuration, Date requestedDate) {
        final Duration duration = Minutes.minutes(gapDuration).toStandardDuration();
        DateTime appointmentStartTime = new DateTime(FORMAT.parseDateTime(startTime));
        if (utilDateToSqlDate(new Date()).equals(utilDateToSqlDate(requestedDate))) {
            return filterAppointmentTimeByCurrentDate(appointmentStartTime, endTime, duration);
        } else {
            return getAppointmentTimeExcludingCurrentDate(appointmentStartTime, endTime, duration);
        }
    }

    public static List<String> filterAppointmentTimeByCurrentDate(DateTime appointmentStartTime,
                                                                  String appointmentEndTime,
                                                                  Duration duration) {
        List<String> response = new ArrayList<>();
        do {
            String dateFormat = new SimpleDateFormat("HH:mm").format(new java.util.Date(System.currentTimeMillis()));
            DateTime currentTime = new DateTime(FORMAT.parseDateTime(dateFormat));
            if (!appointmentStartTime.isBefore(currentTime) && !appointmentStartTime.equals(currentTime)) {
                response.add(convertTo12HourFormat(FORMAT.print(appointmentStartTime)));
            }
            appointmentStartTime = appointmentStartTime.plus(duration);
        } while (appointmentStartTime.compareTo(FORMAT.parseDateTime(appointmentEndTime)) <= 0);
        return response;
    }

    public static List<String> getAppointmentTimeExcludingCurrentDate(DateTime appointmentStartTime,
                                                                      String appointmentEndTime,
                                                                      Duration duration) {
        List<String> response = new ArrayList<>();
        do {
            response.add(convertTo12HourFormat(FORMAT.print(appointmentStartTime)));
            appointmentStartTime = appointmentStartTime.plus(duration);
        } while (appointmentStartTime.compareTo(FORMAT.parseDateTime(appointmentEndTime)) <= 0);

        return response;
    }


    public static List<String> getVacantTime(List<String> allTimeSlot,
                                             List<String> unavailableTimeSlot) {

        List<String> unmatchedList = allTimeSlot.stream()
                .filter(actual -> (unavailableTimeSlot.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        return unmatchedList;
    }

    public static Boolean compareIfRequestedDateExists(
            List<Date> overrideList,
            Date requestedDate) {

        return overrideList.contains(requestedDate) ? true : false;
    }

    public static Appointment parseAppointmentTransferDetail(Appointment appointment,
                                                             AppointmentTransferRequestDTO requestDTO) {
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setAppointmentTime(parseAppointmentTime(appointment.getAppointmentDate(), requestDTO.getAppointmentTime()));
        appointment.setRemarks(requestDTO.getRemarks());
        appointment.setHasTransferred(YES);
        return appointment;
    }

    public static AppointmentDoctorInfo parseAppointmentDoctorInfo(Doctor doctor,Specialization specialization,
                                                                   AppointmentDoctorInfo appointmentDoctorInfo) {

        appointmentDoctorInfo.setDoctor(doctor);
        appointmentDoctorInfo.setSpecialization(specialization);

        return appointmentDoctorInfo;
    }

    public static AppointmentTransfer parseToAppointmentTransfer(Appointment appointment,
                                                                 AppointmentTransferRequestDTO requestDTO,
                                                                 Doctor currentDoctor,
                                                                 Specialization currentSpecialization,
                                                                 Doctor previousDoctor,
                                                                 Specialization previousSpecialization) {
        AppointmentTransfer appointmentTransfer = new AppointmentTransfer();
        appointmentTransfer.setAppointment(appointment);
        appointmentTransfer.setPreviousAppointmentDateAndTime(appointment.getAppointmentTime());
        appointmentTransfer.setRemarks(requestDTO.getRemarks());
        appointmentTransfer.setCurrentAppointmentDateAndTime(parseAppointmentTime(requestDTO.getAppointmentDate(),
                requestDTO.getAppointmentTime()));
        appointmentTransfer.setPreviousDoctor(previousDoctor);
        appointmentTransfer.setPreviousSpecialization(previousSpecialization);
        appointmentTransfer.setCurrentDoctor(currentDoctor);
        appointmentTransfer.setCurrentSpecialization(currentSpecialization);

        return appointmentTransfer;
    }

    public static AppointmentTransferTransactionDetail parseToAppointmentTransferTransactionDetail(
            AppointmentTransactionDetail transactionDetail,
            Double currentAppointmentCharge,
            String remarks,
            AppointmentTransfer appointmentTransfer) {
        AppointmentTransferTransactionDetail transferTransactionDetail = new AppointmentTransferTransactionDetail();
        transferTransactionDetail.setAppointmentTransactionDetail(transactionDetail);
        transferTransactionDetail.setPreviousAppointmentAmount(transactionDetail.getAppointmentAmount());
        transferTransactionDetail.setPreviousDiscountAmount(transactionDetail.getDiscountAmount());
        transferTransactionDetail.setPreviousServiceChargeAmount(transactionDetail.getServiceChargeAmount());
        transferTransactionDetail.setPreviousTaxAmount(transactionDetail.getTaxAmount());
        transferTransactionDetail.setPreviousTransactionDate(transactionDetail.getTransactionDate());
        transferTransactionDetail.setRemarks(remarks);
        transferTransactionDetail.setAppointmentTransfer(appointmentTransfer);
        transferTransactionDetail.setCurrentAppointmentAmount(currentAppointmentCharge);

        return transferTransactionDetail;
    }

    public static AppointmentTransactionDetail parseToAppointmentTransactionDetail(
            AppointmentTransactionDetail transactionDetail,
            AppointmentTransferRequestDTO requestDTO) {
        transactionDetail.setAppointmentAmount(requestDTO.getAppointmentCharge());
        transactionDetail.setTransactionDate(new Date());

        return transactionDetail;
    }

    public static AppointmentTransferTransactionRequestLog parseToAppointmentTransferTransactionRequestLog(
            AppointmentTransactionRequestLog transactionRequestLog,
            String remarks,
            AppointmentTransferTransactionDetail transferTransactionDetail) {
        AppointmentTransferTransactionRequestLog requestLog = new AppointmentTransferTransactionRequestLog();
        requestLog.setAppointmentTransactionRequestLog(transactionRequestLog);
        requestLog.setPreviousTransactionDate(transactionRequestLog.getTransactionDate());
        requestLog.setPreviousTransactionStatus(transactionRequestLog.getTransactionStatus());
        requestLog.setRemarks(remarks);
        requestLog.setAppointmentTransferTransactionDetail(transferTransactionDetail);

        return requestLog;
    }

    public static AppointmentTransactionRequestLog parseToAppointmentTransactionRequestLog(
            AppointmentTransactionRequestLog transactionRequestLog) {
        transactionRequestLog.setTransactionDate(new Date());
        transactionRequestLog.setTransactionStatus(YES);
        transactionRequestLog.setHas_transferred(YES);

        return transactionRequestLog;
    }

    public static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate),
                Objects.requireNonNull(parseTime(convert12HourTo24HourFormat(appointmentTime))));
    }


    public static List<AppointmentTransferLogDTO> mergeCurrentAppointmentStatus(
            List<LastModifiedAppointmentIdAndStatus> appointmentDetailsDTOS,
            List<AppointmentTransferLogDTO> transferredList) {

//        transferredList.forEach(appointmentTransferLogDTO -> {
//            appointmentDetailsDTOS.forEach(lastModifiedAppointmentIdAndStatus -> {
//                if (appointmentTransferLogDTO.getAppointmentTransferId() ==
//                        lastModifiedAppointmentIdAndStatus.getAppointmentTransferredId()) {
//                    appointmentTransferLogDTO.setStatus(lastModifiedAppointmentIdAndStatus.getStatus());
//                }
//            });
//        });

        transferredList.forEach(appointmentTransferLogDTO -> {
            appointmentDetailsDTOS.forEach(lastModifiedAppointmentIdAndStatus -> {
                if (appointmentTransferLogDTO.getAppointmentTransferId() ==
                        lastModifiedAppointmentIdAndStatus.getAppointmentTransferredId()) {
                    appointmentTransferLogDTO.setStatus(lastModifiedAppointmentIdAndStatus.getStatus());
                }
            });
        });

        return transferredList;
    }

    public static List<Date> filterOverrideDayOffDates(List<OverrideDatesResponseDTO> overrideDatesResponseDTOS) {
        List<Date> dayOffDateRange = new ArrayList<>();

        List<OverrideDatesResponseDTO> dayOffDates = overrideDatesResponseDTOS.stream()
                .filter(date -> date.getDayOffStatus().equals('Y'))
                .collect(Collectors.toList());

        dayOffDates.forEach(dates -> {
            List<Date> dateList = getDates(dates.getFromDate(), dates.getToDate());
            dayOffDateRange.addAll(dateList);
        });

        return dayOffDateRange;
    }

    public static List<Date> filterOverrideAvaliableDates(List<OverrideDatesResponseDTO> overrideDatesResponseDTOS) {
        List<Date> dayOffDateRange = new ArrayList<>();

        List<OverrideDatesResponseDTO> dayOffDates = overrideDatesResponseDTOS.stream()
                .filter(date -> date.getDayOffStatus().equals('N'))
                .collect(Collectors.toList());

        dayOffDates.forEach(dates -> {
            List<Date> dateList = getDates(dates.getFromDate(), dates.getToDate());
            dayOffDateRange.addAll(dateList);
        });

        return dayOffDateRange;
    }

    public static String convertTo12HourFormat(String timeIn24HrFormat) {
        try {
            SimpleDateFormat dateParser = new SimpleDateFormat("HH:mm");
            Date date = dateParser.parse(timeIn24HrFormat);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm a");
            return dateFormatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
