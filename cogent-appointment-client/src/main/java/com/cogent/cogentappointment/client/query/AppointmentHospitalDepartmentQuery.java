package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.client.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE;

/**
 * @author Sauravi Thapa on 06/07/20
 */
public class AppointmentHospitalDepartmentQuery {

    /*admin*/
    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT " +
                "  a.appointment_date as appointmentDate, " +                                                      //[0]
                "  GROUP_CONCAT(DATE_FORMAT(a.appointment_time, '%H:%i'), '-', a.status) as startTimeDetails, " +  //[1]
                "  hd.id as hospitalDepartmentId, " +                                                              //[2]
                "  hdri.id as hosptalDeaprtmentRoomInfoId, " +                                                     //[3]
                "  a.appointment_number  as appointmentNumber, " +                                                 //[4]
                "  p.name as patientName, " +                                                                      //[5]
                "  p.gender  as gender, " +                                                                        //[6]
                "  p.mobile_number as mobileNumber, " +                                                            //[7]
                QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE + "," +                                                      //[8]
                "  a.id as appointmentId, " +                                                                      //[9]
                "  a.is_follow_up as isFollowUp, " +                                                              //[10]
                "  a.has_transferred as hasTransferred " +                                                        //[1]
                "FROM appointment_hospital_department_info ahdi " +
                "LEFT JOIN appointment a ON a.id = ahdi.appointment_id " +
                "LEFT JOIN hospital_department hd ON hd.id=ahdi.hospital_department_id  " +
                "LEFT JOIN hospital_department_room_info hdri ON hdri.id=ahdi.hospital_department_room_info_id  " +
                "LEFT JOIN room r ON r.id=hdri.room_id  " +
                "LEFT JOIN patient p ON p.id=a.patient_id  " +
                "LEFT JOIN hospital h ON h.id=a.hospital_id  " +
                " WHERE " +
                " a.appointment_date BETWEEN :fromDate AND :toDate " +
                " AND a.status IN ('PA', 'A', 'C') " +
                " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND ahdi.hospital_department_id =:hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            SQL += " AND  ahdi.hospital_department_room_info_id = :hospitalDepartmentRoomInfoId ";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        SQL += " GROUP BY a.appointment_date, ahdi.hospital_department_id , ahdi.hospital_department_room_info_id  " +
                " ORDER BY a.appointment_date";

        return SQL;
    }

}
