package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.AppointmentCountResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REJECTED;

/**
 * @author smriti on 2019-10-24
 */
public class AppointmentUtils {

    public static void parseRefundRejectDetails(AppointmentRefundRejectDTO refundRejectDTO,
                                                AppointmentRefundDetail refundDetail) {
        refundDetail.setStatus(REJECTED);
        refundDetail.setRemarks(refundRejectDTO.getRemarks());
    }
    public static AppointmentCountResponseDTO parseToAppointmentCountResponseDTO(Long overAllAppointment, Long newPatient,
                                                                                 Long registeredPatient,
                                                                                 Character pillType) {
        AppointmentCountResponseDTO countResponseDTO = new AppointmentCountResponseDTO();
        countResponseDTO.setTotalAppointment(overAllAppointment);
        countResponseDTO.setNewPatient(newPatient);
        countResponseDTO.setRegisteredPatient(registeredPatient);
        countResponseDTO.setPillType(pillType);

        return countResponseDTO;
    }

//    public static AppointmentAvailabilityResponseDTO parseToAppointmentAvailabilityResponseDTO
//            (List<AppointmentTimeResponseDTO> appointmentTimeResponseDTOS,
//             DoctorDutyRosterTimeResponseDTO doctorDutyRosterTimeResponseDTO) {
//
//        return AppointmentAvailabilityResponseDTO.builder()
//                .appointmentTimeResponseDTOS(appointmentTimeResponseDTOS)
//                .doctorDutyRosterTimeResponseDTO(doctorDutyRosterTimeResponseDTO)
//                .build();
//    }

//    public static List<AppointmentStatusResponseDTO> parseQueryResultToAppointmentStatusResponseDTOS
//            (List<Object[]> results) {
//
//        List<AppointmentStatusResponseDTO> appointmentStatusResponseDTOS = new ArrayList<>();
//
//        results.forEach(result -> {
//            final int APPOINTMENT_DATE_INDEX = 0;
//            final int TIME_WITH_STATUS_DETAILS_INDEX = 1;
//            final int DOCTOR_ID_INDEX = 2;
//            final int DOCTOR_NAME_INDEX = 3;
//            final int SPECIALIZATION_ID_INDEX = 4;
//            final int SPECIALIZATION_NAME_INDEX = 5;
//
//            Date appointmentDate = (Date) result[APPOINTMENT_DATE_INDEX];
//
//            LocalDate appointmentLocalDate = new java.sql.Date(appointmentDate.getTime()).toLocalDate();
//
//            AppointmentStatusResponseDTO appointmentStatusResponseDTO = AppointmentStatusResponseDTO.builder()
//                    .date(appointmentLocalDate)
//                    .startTimeDetails(result[TIME_WITH_STATUS_DETAILS_INDEX].toString())
//                    .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
//                    .doctorName(result[DOCTOR_NAME_INDEX].toString())
//                    .specializationId(Long.parseLong(result[SPECIALIZATION_ID_INDEX].toString()))
//                    .specializationName(result[SPECIALIZATION_NAME_INDEX].toString())
//                    .build();
//
//            appointmentStatusResponseDTOS.add(appointmentStatusResponseDTO);
//        });
//
//        return appointmentStatusResponseDTOS;
//    }
}
