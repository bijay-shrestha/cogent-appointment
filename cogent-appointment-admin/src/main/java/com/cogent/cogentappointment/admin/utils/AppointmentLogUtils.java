package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.HospitalDepartmentTransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;

/**
 * @author smriti on 16/04/20
 */
public class AppointmentLogUtils {

    public static BookedAppointmentResponseDTO parseBookedAppointmentDetails(Object[] bookedResult,
                                                                             Object[] bookedWithFollowUpResult) {

        return BookedAppointmentResponseDTO.builder()
                .bookedCount(Long.parseLong(bookedResult[0].toString()))
                .bookedAmount(Double.parseDouble(bookedResult[1].toString()))
                .followUpCount(Long.parseLong(bookedWithFollowUpResult[0].toString()))
                .followUpAmount(Double.parseDouble(bookedWithFollowUpResult[1].toString()))
                .build();
    }

    public static CheckedInAppointmentResponseDTO parseCheckedInAppointmentDetails(Object[] checkInResult,
                                                                                   Object[] checkInWithFollowUpResult) {

        return CheckedInAppointmentResponseDTO.builder()
                .checkedInCount(Long.parseLong(checkInResult[0].toString()))
                .checkedInAmount(Double.parseDouble(checkInResult[1].toString()))
                .followUpCount(Long.parseLong(checkInWithFollowUpResult[0].toString()))
                .followUpAmount(Double.parseDouble(checkInWithFollowUpResult[1].toString()))
                .build();
    }

    public static CancelledAppointmentResponseDTO parseCancelledAppointmentDetails(Object[] cancelledResult,
                                                                                   Object[] cancelledWithFollowUpResult) {

        return CancelledAppointmentResponseDTO.builder()
                .cancelledCount(Long.parseLong(cancelledResult[0].toString()))
                .cancelAmount(Double.parseDouble(cancelledResult[1].toString()))
                .followUpCount(Long.parseLong(cancelledWithFollowUpResult[0].toString()))
                .followUpAmount(Double.parseDouble(cancelledWithFollowUpResult[1].toString()))
                .build();
    }

    public static RefundAppointmentResponseDTO parseRefundedAppointmentDetails(Object[] refundResult,
                                                                               Object[] refundWithFollowUpResult) {

        return RefundAppointmentResponseDTO.builder()
                .refundedCount(Long.parseLong(refundResult[0].toString()))
                .refundedAmount(Double.parseDouble(refundResult[1].toString()))
                .followUpCount(Long.parseLong(refundWithFollowUpResult[0].toString()))
                .followUpAmount(Double.parseDouble(refundWithFollowUpResult[1].toString()))
                .build();
    }

    public static RevenueFromRefundAppointmentResponseDTO parseRevenueFromRefundAppointmentDetails
            (Object[] refundResult,
             Object[] refundWithFollowUpResult) {

        return RevenueFromRefundAppointmentResponseDTO.builder()
                .revenueFromRefundCount(Long.parseLong(refundResult[0].toString()))
                .revenueFromRefundAmount(Double.parseDouble(refundResult[1].toString()))
                .followUpCount(Long.parseLong(refundWithFollowUpResult[0].toString()))
                .followUpAmount(Double.parseDouble(refundWithFollowUpResult[1].toString()))
                .build();
    }

    public static void parseToAppointmentLogResponseDTO(AppointmentLogResponseDTO responseDTO,
                                                        BookedAppointmentResponseDTO bookedAppointmentResponseDTO,
                                                        CheckedInAppointmentResponseDTO checkedInAppointmentResponseDTO,
                                                        CancelledAppointmentResponseDTO cancelledAppointmentResponseDTO,
                                                        RefundAppointmentResponseDTO refundAppointmentResponseDTO,
                                                        RevenueFromRefundAppointmentResponseDTO revenueFromRefundAppointmentResponseDTO) {

        responseDTO.setBookedInfo(bookedAppointmentResponseDTO);
        responseDTO.setCheckedInInfo(checkedInAppointmentResponseDTO);
        responseDTO.setCancelledInfo(cancelledAppointmentResponseDTO);
        responseDTO.setRefundInfo(refundAppointmentResponseDTO);
        responseDTO.setRevenueFromRefundInfo(revenueFromRefundAppointmentResponseDTO);
    }

    public static void parseToTxnLogResponseDTO(
            TransactionLogResponseDTO responseDTO,
            BookedAppointmentResponseDTO bookedAppointmentResponseDTO,
            CheckedInAppointmentResponseDTO checkedInAppointmentResponseDTO,
            CancelledAppointmentResponseDTO cancelledAppointmentResponseDTO,
            RefundAppointmentResponseDTO refundAppointmentResponseDTO,
            RevenueFromRefundAppointmentResponseDTO revenueFromRefundAppointmentResponseDTO) {

        responseDTO.setBookedInfo(bookedAppointmentResponseDTO);
        responseDTO.setCheckedInInfo(checkedInAppointmentResponseDTO);
        responseDTO.setCancelledInfo(cancelledAppointmentResponseDTO);
        responseDTO.setRefundInfo(refundAppointmentResponseDTO);
        responseDTO.setRevenueFromRefundInfo(revenueFromRefundAppointmentResponseDTO);
    }

    public static void parseToHospitalDeptTxnLogResponseDTO(HospitalDepartmentTransactionLogResponseDTO responseDTO,
                                                            BookedAppointmentResponseDTO bookedAppointmentResponseDTO,
                                                            CheckedInAppointmentResponseDTO checkedInAppointmentResponseDTO,
                                                            CancelledAppointmentResponseDTO cancelledAppointmentResponseDTO,
                                                            RefundAppointmentResponseDTO refundAppointmentResponseDTO,
                                                            RevenueFromRefundAppointmentResponseDTO revenueFromRefundAppointmentResponseDTO) {

        responseDTO.setBookedInfo(bookedAppointmentResponseDTO);
        responseDTO.setCheckedInInfo(checkedInAppointmentResponseDTO);
        responseDTO.setCancelledInfo(cancelledAppointmentResponseDTO);
        responseDTO.setRefundInfo(refundAppointmentResponseDTO);
        responseDTO.setRevenueFromRefundInfo(revenueFromRefundAppointmentResponseDTO);
    }
}
