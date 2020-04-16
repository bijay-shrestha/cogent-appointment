package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;

/**
 * @author smriti on 16/04/20
 */
public class AppointmentRevenueStatisticsUtils {

    public static void parseBookedAppointmentDetails(Object[] result,
                                                     AppointmentRevenueStatisticsResponseDTO responseDTO) {

        final int COUNT_INDEX = 0;
        final int AMOUNT_INDEX = 1;

        responseDTO.setBookedAppointmentsCount(Long.parseLong(result[COUNT_INDEX].toString()));
        responseDTO.setBookedAmount(Double.parseDouble(result[AMOUNT_INDEX].toString()));
    }

    public static void parseCheckedInAppointmentDetails(Object[] result,
                                                        AppointmentRevenueStatisticsResponseDTO responseDTO) {

        final int COUNT_INDEX = 0;
        final int AMOUNT_INDEX = 1;

        responseDTO.setCheckedInAppointmentsCount(Long.parseLong(result[COUNT_INDEX].toString()));
        responseDTO.setCheckedInAmount(Double.parseDouble(result[AMOUNT_INDEX].toString()));
    }

    public static void parseCancelledAppointmentDetails(Object[] result,
                                                        AppointmentRevenueStatisticsResponseDTO responseDTO) {

        final int COUNT_INDEX = 0;
        final int AMOUNT_INDEX = 1;

        responseDTO.setCancelAppointmentsCount(Long.parseLong(result[COUNT_INDEX].toString()));
        responseDTO.setCancelAmount(Double.parseDouble(result[AMOUNT_INDEX].toString()));
    }

    public static void parseRefundedAppointmentDetails(Object[] result,
                                                       AppointmentRevenueStatisticsResponseDTO responseDTO) {

        final int COUNT_INDEX = 0;
        final int AMOUNT_INDEX = 1;

        responseDTO.setRefundedAppointmentsCount(Long.parseLong(result[COUNT_INDEX].toString()));
        responseDTO.setRefundedAmount(Double.parseDouble(result[AMOUNT_INDEX].toString()));
    }

    public static void parseRevenueFromRefundedAppointmentDetails(Object[] result,
                                                                  AppointmentRevenueStatisticsResponseDTO responseDTO) {

        final int COUNT_INDEX = 0;
        final int AMOUNT_INDEX = 1;

        responseDTO.setRevenueFromRefundedAppointmentsCount(Long.parseLong(result[COUNT_INDEX].toString()));
        responseDTO.setRevenueFromRefundedAmount(Double.parseDouble(result[AMOUNT_INDEX].toString()));
    }

}
