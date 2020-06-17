package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE;
import static com.cogent.cogentappointment.admin.query.PatientQuery.QUERY_TO_CALCULATE_PATIENT_AGE_NATIVE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;

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
                " FROM appointment_hospital_department_info ahdi " +
                " LEFT JOIN appointment a ON a.id = ahdi.appointment_id " +
                " LEFT JOIN hospital_department hd ON hd.id=ahdi.hospital_department_id  " +
                " LEFT JOIN hospital_department_room_info hdri ON hdri.id=ahdi.hospital_department_room_info_id  " +
                " LEFT JOIN room r ON r.id=hdri.room_id  " +
                " LEFT JOIN patient p ON p.id=a.patient_id  " +
                " LEFT JOIN hospital h ON h.id=a.hospital_id  " +
                " WHERE " +
                " a.appointment_date BETWEEN :fromDate AND :toDate " +
                " AND a.status IN ('PA', 'A', 'C') ";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND ahdi.hospital_department_id =:hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            SQL += " AND  ahdi.hospital_department_room_info_id = :hospitalDepartmentRoomInfoId ";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        SQL += " GROUP BY a.id  " +
                " ORDER BY a.appointment_date";

        return SQL;
    }

    /*admin*/
    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_ROOM_WISE(
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
                " AND  ahdi.hospital_department_room_info_id = :hospitalDepartmentRoomInfoId ";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND ahdi.hospital_department_id =:hospitalDepartmentId";

        if ((!ObjectUtils.isEmpty(requestDTO.getStatus())) && (!(requestDTO.getStatus().equals(VACANT))))
            SQL += " AND a.status='" + requestDTO.getStatus() + "'";

        SQL += " GROUP BY a.id  " +
                " ORDER BY a.appointment_date";

        return SQL;
    }

    public static Function<AppointmentHospitalDepartmentPendingApprovalSearchDTO, String>
            QUERY_TO_FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENTS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " a.id as appointmentId," +                                                  //[0]
                            " a.appointmentDate as appointmentDate," +                                   //[1]
                            " a.appointmentNumber as appointmentNumber," +                               //[2]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[3]
                            " CASE WHEN" +
                            " (hpi.registrationNumber IS NULL)" +
                            " THEN 'N/A'" +
                            " ELSE" +
                            " hpi.registrationNumber" +
                            " END as registrationNumber," +                                             //[4]
                            " p.name as patientName," +                                                 //[5]
                            " p.mobileNumber as mobileNumber," +                                        //[6]
                            " a.appointmentModeId.name as appointmentMode," +                           //[7]
                            " atd.appointmentAmount as appointmentAmount," +                            //[8]
                            " hpi.hospitalNumber as hospitalNumber," +                                  //[9]
                            " p.id as patientId," +                                                     //[10]
                            " p.gender as gender," +                                                    //[11]
                            " hpi.address as address," +                                                //[12]
                            " hpi.isRegistered as isRegistered," +                                      //[13]
                            " hd.name as hospitalDepartmentName," +                                     //[14]
                            " hb.billingMode.name as billingModeName," +                                //[15]
                            " case when hr.id is null then null" +
                            " when hr.id is not null then r.roomNumber" +
                            " end as roomNumber," +                                                      //[16]
                            QUERY_TO_CALCULATE_PATIENT_AGE +                                            //[17]
                            " FROM Appointment a" +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ad.hospitalDepartment.id" +
                            " LEFT JOIN HospitalDepartmentBillingModeInfo hb ON hb.id = ad.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ad.hospitalDepartmentRoomInfo.id" +
                            " LEFT JOIN Room r ON r.id = hr.room.id" +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentHospitalDepartmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE " +
                " a.status='PA'";

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(pendingApprovalSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getFromDate()) + "' AND '"
                    + utilDateToSqlDate(pendingApprovalSearchDTO.getToDate()) + "')";

        if (!Objects.isNull(pendingApprovalSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + pendingApprovalSearchDTO.getAppointmentId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + pendingApprovalSearchDTO.getHospitalId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + pendingApprovalSearchDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + pendingApprovalSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id = " + pendingApprovalSearchDTO.getHospitalDepartmentId();

        if (!ObjectUtils.isEmpty(pendingApprovalSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + pendingApprovalSearchDTO.getAppointmentNumber() + "%'";

        whereClause += " ORDER BY a.appointmentDate DESC";

        return whereClause;
    }
}
