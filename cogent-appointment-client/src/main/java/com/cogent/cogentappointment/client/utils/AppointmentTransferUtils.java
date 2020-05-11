package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
public class AppointmentTransferUtils {
    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("HH:mm");

    public static List<Date> mergeOverrideAndActualDateList(
            List<Date> overrideList,
            List<Date> actualList) {

        List<Date> unmatchedList = actualList.stream()
                .filter(actual -> (overrideList.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        overrideList.addAll(unmatchedList);

//        overrideList.removeIf(override -> override.getDayOffStatus().equals(YES));
        Collections.sort(overrideList);

        return overrideList;
    }

    public static List<Date> getActualdate(List<String> dayOffDay, List<Date> dates) {
        List<Date> unmatched = dates.stream()
                .filter(actualDate -> dayOffDay.stream()
                        .filter(weekDay -> weekDay.equals(actualDate.toString().substring(0, 3).toUpperCase()))
                        .count() < 1)
                .collect(Collectors.toList());
        return unmatched;
    }

    public static List<String> getGapDuration(String startTime, String endTime, Integer gapDuration) {
        final Duration duration = Minutes.minutes(gapDuration).toStandardDuration();
        DateTime dateTime = new DateTime(FORMAT.parseDateTime(startTime));
        List<String> response = new ArrayList<>();

        do {
            response.add(convert24HourTo12HourFormat(FORMAT.print(dateTime)));
            dateTime = dateTime.plus(duration);
        } while (dateTime.compareTo(FORMAT.parseDateTime(endTime)) <= 0);

        return response;
    }

    public static List<String> getVacantTime(List<String> allTimeSlot,
                                             List<String> unavailableTimeSlot,
                                             Date requestedDate) {

        Date time = new java.util.Date(System.currentTimeMillis());
        String dateFormat = new SimpleDateFormat("HH:mm:ss").format(time);
        LocalTime localTime = LocalTime.parse(dateFormat);

        List<String> unmatchedList = allTimeSlot.stream()
                .filter(actual -> (unavailableTimeSlot.stream()
                        .filter(override -> (override.equals(actual))
                                && (override.equals(actual))
                        )
                        .count()) < 1)
                .collect(Collectors.toList());

        return unmatchedList;
    }

    public static Date compareAndGetDate(
            List<Date> overrideList,
            Date requestedDate) {

//        List<Date> matchedList = overrideList.stream()
//                .filter(actual -> (overrideList.stream()
//                        .filter(override -> (override.equals(actual)))
//                        .anyMatch(override -> override.equals(actual))))
//                .collect(Collectors.toList());
        List<Date> matchedList = overrideList.stream()
                .filter(overrideDate -> overrideDate.equals(requestedDate))
                .collect(Collectors.toList());

        for (Date date : matchedList) {
            if (date.equals(requestedDate)) {
                return date;
            }
        }
        return null;
    }

    public static Appointment parseAppointmentTransferDetail(Appointment appointment,
                                                             AppointmentTransferRequestDTO requestDTO,
                                                             Doctor doctor) {
        appointment.setAppointmentDate(requestDTO.getAppointmentDate());
        appointment.setAppointmentTime(parseAppointmentTime(appointment.getAppointmentDate(), requestDTO.getAppointmentTime()));
        appointment.setDoctorId(doctor);
        appointment.setRemarks(requestDTO.getRemarks());
        appointment.setHasTransferred(YES);
        return appointment;
    }

    public static Appointment parseAppointmentForSpecialization(Appointment appointment,
                                                                AppointmentTransferRequestDTO requestDTO,
                                                                Doctor doctor,
                                                                Specialization specialization) {
        parseAppointmentTransferDetail(appointment,
                requestDTO,
                doctor);
        appointment.setSpecializationId(specialization);
        return appointment;
    }

    public static AppointmentTransfer parseToAppointmentTransfer(Appointment appointment, String remarks) {
        AppointmentTransfer appointmentTransfer = new AppointmentTransfer();
        appointmentTransfer.setAppointment(appointment);
        appointmentTransfer.setPreviousAppointmentDate(appointment.getAppointmentDate());
        appointmentTransfer.setPreviousAppointmentTime(appointment.getAppointmentTime());
        appointmentTransfer.setPreviousDoctorId(appointment.getDoctorId().getId());
        appointmentTransfer.setPreviousSpecializationId(appointment.getSpecializationId().getId());
        appointmentTransfer.setRemarks(remarks);

        return appointmentTransfer;
    }

    public static AppointmentTransferTransactionDetail parseToAppointmentTransferTransactionDetail(
            AppointmentTransactionDetail transactionDetail,
            String remarks) {
        AppointmentTransferTransactionDetail transferTransactionDetail = new AppointmentTransferTransactionDetail();
        transferTransactionDetail.setAppointmentTransactionDetail(transactionDetail);
        transferTransactionDetail.setPreviousAppointmentAmount(transactionDetail.getAppointmentAmount());
        transferTransactionDetail.setPreviousDiscountAmount(transactionDetail.getDiscountAmount());
        transferTransactionDetail.setPreviousServiceChargeAmount(transactionDetail.getServiceChargeAmount());
        transferTransactionDetail.setPreviousTaxAmount(transactionDetail.getTaxAmount());
        transferTransactionDetail.setPreviousTransactionDate(transactionDetail.getTransactionDate());
        transferTransactionDetail.setPreviousTransactionDateTime(transactionDetail.getTransactionDateTime());
        transferTransactionDetail.setRemarks(remarks);

        return transferTransactionDetail;
    }
    public static AppointmentTransactionDetail parseToAppointmentTransactionDetail(
            AppointmentTransactionDetail transactionDetail,
            AppointmentTransferRequestDTO requestDTO) {
        transactionDetail.setAppointmentAmount(requestDTO.getAppointmentCharge());
        transactionDetail.setTransactionDate(new Date());
        transactionDetail.setTransactionDateTime(new Date());

        return transactionDetail;
    }

    public static AppointmentTransferTransactionRequestLog parseToAppointmentTransferTransactionRequestLog(
            AppointmentTransactionRequestLog transactionRequestLog,
            String remarks) {
        AppointmentTransferTransactionRequestLog requestLog = new AppointmentTransferTransactionRequestLog();
        requestLog.setAppointmentTransactionRequestLog(transactionRequestLog);
        requestLog.setPreviousTransactionDate(transactionRequestLog.getTransactionDate());
        requestLog.setPreviousTransactionStatus(transactionRequestLog.getTransactionStatus());
        requestLog.setRemarks(remarks);

        return requestLog;
    }

    public static AppointmentTransactionRequestLog parseToAppointmentTransactionRequestLog(
            AppointmentTransactionRequestLog transactionRequestLog) {
        transactionRequestLog.setTransactionDate(new Date());
        transactionRequestLog.setTransactionStatus(YES);

        return transactionRequestLog;
    }

    private static Date parseAppointmentTime(Date appointmentDate, String appointmentTime) {
        return datePlusTime(utilDateToSqlDate(appointmentDate),
                Objects.requireNonNull(parseTime(convert12HourTo24HourFormat(appointmentTime))));
    }


}
