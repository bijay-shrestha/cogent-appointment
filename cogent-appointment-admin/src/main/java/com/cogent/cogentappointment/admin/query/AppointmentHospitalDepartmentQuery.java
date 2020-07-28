package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentHospitalDepartment.AppointmentHospitalDepartmentCheckInSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.VACANT;
import static com.cogent.cogentappointment.admin.query.PatientQuery.*;
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
                " LEFT JOIN hospital_appointment_service_type hast ON hast.id=a.hospital_appointment_service_type_id" +
                " LEFT JOIN appointment_service_type ast ON ast.id=hast.appointment_service_type_id  " +
                " WHERE " +
                " a.appointment_date BETWEEN :fromDate AND :toDate " +
                " AND a.status IN ('PA', 'A', 'C') " +
                " AND ast.code=:appointmentServiceTypeCode";

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

    public static Function<AppointmentHospitalDepartmentCheckInSearchDTO, String>
            QUERY_TO_FETCH_PENDING_HOSPITAL_DEPARTMENT_APPOINTMENTS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " a.id as appointmentId," +                                                  //[0]
                            " a.appointmentDate as appointmentDate," +                                   //[1]
                            " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[2]
                            " a.appointmentNumber as appointmentNumber," +                               //[3]
                            " atd.appointmentAmount as appointmentAmount," +                            //[4]
                            " atd.transactionNumber as transactionNumber," +                             //[5]
                            " p.name as patientName," +                                                 //[6]
                            " p.mobileNumber as mobileNumber," +                                        //[7]
                            " p.gender as gender," +                                                    //[8]
                            " hpi.isRegistered as isRegistered," +                                      //[9]
                            " CASE WHEN" +
                            " (hpi.registrationNumber IS NULL)" +
                            " THEN 'N/A'" +
                            " ELSE" +
                            " hpi.registrationNumber" +
                            " END as registrationNumber," +                                             //[10]
                            " hpi.hospitalNumber as hospitalNumber," +                                  //[11]
                            " hpi.address as address," +                                                //[12]
                            " hd.name as hospitalDepartmentName," +                                     //[13]
                            " case when hr.id is null then null" +
                            " when hr.id is not null then r.roomNumber" +
                            " end as roomNumber," +                                                      //[14]
                            " CASE WHEN" +
                            " hpi.hasAddress  = 'Y'" +
                            " THEN" +
                            " CONCAT_WS(', ',COALESCE(pr.value, ' ')," +
                            " COALESCE(d.value,','),COALESCE(vm.value,','),COALESCE(hpi.wardNumber,''))" +
                            " ELSE" +
                            " hpi.address" +
                            " end as address," +
                            " h.name as hospitalName," +                                                //[15]
                            QUERY_TO_CALCULATE_PATIENT_AGE +                                            //[16]
                            " FROM Appointment a" +
                            " INNER JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ad.hospitalDepartment.id" +
                            " LEFT JOIN HospitalDepartmentBillingModeInfo hb ON hb.id = ad.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ad.hospitalDepartmentRoomInfo.id" +
                            " LEFT JOIN Room r ON r.id = hr.room.id" +
                            " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN Address pr ON pr.id = hpi.province.id" +
                            " LEFT JOIN Address d ON d.id = hpi.district.id" +
                            " LEFT JOIN Address vm ON vm.id = hpi.vdcOrMunicipality.id"
                            + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(
            AppointmentHospitalDepartmentCheckInSearchDTO pendingApprovalSearchDTO) {

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

    public static String QUERY_TO_FETCH_PENDING_APPOINTMENT_DETAIL =
            "SELECT" +
                    " a.id as appointmentId," +                                                  //[0]
                    " a.appointmentDate as appointmentDate," +                                   //[1]
                    " DATE_FORMAT(a.appointmentTime, '%h:%i %p') as appointmentTime," +          //[2]
                    " a.appointmentNumber as appointmentNumber," +                               //[3]
                    " atd.appointmentAmount as appointmentAmount," +                            //[4]
                    " atd.transactionNumber as transactionNumber," +                             //[5]
                    " p.name as patientName," +                                                 //[6]
                    " p.mobileNumber as mobileNumber," +                                        //[7]
                    " p.gender as gender," +                                                    //[8]
                    " hpi.isRegistered as isRegistered," +                                      //[9]
                    " CASE WHEN" +
                    " (hpi.registrationNumber IS NULL)" +
                    " THEN 'N/A'" +
                    " ELSE" +
                    " hpi.registrationNumber" +
                    " END as registrationNumber," +                                             //[10]
                    " hpi.hospitalNumber as hospitalNumber," +                                  //[11]
                    " hpi.address as address," +                                                //[12]
                    " hd.name as hospitalDepartmentName," +                                     //[13]
                    " case when hr.id is null then null" +
                    " when hr.id is not null then r.roomNumber" +
                    " end as roomNumber," +                                                      //[14]
                    " pr.value as province," +                                                  //[15]
                    " d.value as district," +                                                   //[16]
                    " vm.value as vdcOrMunicipality," +                                         //[17]
                    " hpi.wardNumber as wardNumber," +                                          //[18]
                    " CASE WHEN" +
                    " hpi.hasAddress  = 'Y'" +
                    " THEN" +
                    " CONCAT_WS(', ',COALESCE(pr.value, ' ')," +
                    " COALESCE(d.value,','),COALESCE(vm.value,','),COALESCE(hpi.wardNumber,''))" +
                    " ELSE" +
                    " hpi.address" +
                    " end as address," +                                                        //[19]
                    QUERY_TO_CALCULATE_PATIENT_AGE_YEAR + "," +                                 //[20]
                    QUERY_TO_CALCULATE_PATIENT_AGE_MONTH + "," +                                //[21]
                    QUERY_TO_CALCULATE_PATIENT_AGE_DAY + "," +                                  //[22]
                    " p.eSewaId as eSewaId," +                                                  //[23]
                    " a.isSelf as isSelf," +                                                    //[24]
                    " a.appointmentModeId.name as appointmentMode," +                           //[25]
                    " hb.billingMode.name as billingModeName," +                                //[26]
                    " h.name as hospitalName," +                                                  //[27]
                    " h.id as hospitalId," +                                                      //[28]
                    " hpi.email as emailAddress"+                                                 //[29]
                    " FROM Appointment a" +
                    " INNER JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ad.hospitalDepartment.id" +
                    " LEFT JOIN HospitalDepartmentBillingModeInfo hb ON hb.id = ad.hospitalDepartmentBillingModeInfo.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ad.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Room r ON r.id = hr.room.id" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                    " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                    " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                    " LEFT JOIN Address pr ON pr.id = hpi.province.id" +
                    " LEFT JOIN Address d ON d.id = hpi.district.id" +
                    " LEFT JOIN Address vm ON vm.id = hpi.vdcOrMunicipality.id" +
                    " WHERE a.id =:appointmentId" +
                    " AND a.status='PA'";

    public static String QUERY_TO_FETCH_APPOINTMENT_DETAIL_FOR_HOSPITAL_DEPT_CHECK_IN =
            "SELECT" +
                    " p.name as name," +                                                 //[0]
                    QUERY_TO_CALCULATE_PATIENT_AGE_YEAR + "," +                          //[1]
                    QUERY_TO_CALCULATE_PATIENT_AGE_MONTH + "," +                        //[2]
                    QUERY_TO_CALCULATE_PATIENT_AGE_DAY + "," +                          //[3]
                    " p.gender as gender," +                                             //[4]
                    " COALESCE(d.value, '') as district," +                              //[5]
                    " COALESCE(vm.value, '') as vdc," +                                 //[6]
                    " COALESCE(hpi.wardNumber, '') as wardNo," +                        //[7]
                    " hpi.address AS address," +                                        //[8]
                    " p.mobileNumber as mobileNo," +                                    //[9]
                    " COALESCE(hpi.email, '') as emailAddress," +                       //[10]
                    " hd.name as section," +                                            //[11]
                    " case when hr.id is null then '0'" +
                    " when hr.id is not null then SUBSTRING_INDEX(r.roomNumber ,'-',-1)" +
                    " end as roomNo," +                                                 //[12]
                    " a.appointmentNumber as appointmentNo," +                          //[13]
                    " hpi.hospitalNumber as patientId" +                                //[14]
                    " FROM Appointment a" +
                    " INNER JOIN AppointmentHospitalDepartmentInfo ad ON a.id = ad.appointment.id" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ad.hospitalDepartment.id" +
                    " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ad.hospitalDepartmentRoomInfo.id" +
                    " LEFT JOIN Room r ON r.id = hr.room.id" +
                    " LEFT JOIN Patient p ON p.id = a.patientId.id" +
                    " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                    " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                    " LEFT JOIN Address d ON d.id = hpi.district.id" +
                    " LEFT JOIN Address vm ON vm.id = hpi.vdcOrMunicipality.id" +
                    " WHERE a.id =:appointmentId" +
                    " AND a.status='PA'";

    public static Function<AppointmentLogSearchDTO, String> QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOGS =
            (appointmentLogSearchDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +                                                         //[0]
                            " a.appointmentDate as appointmentDate," +                                           //[1]
                            " a.appointmentNumber as appointmentNumber," +                                       //[2]
                            " DATE_FORMAT(a.appointmentTime , '%h:%i %p') as appointmentTime," +                 //[3]
                            " p.eSewaId as esewaId," +                                                           //[4]
                            " CASE WHEN hpi.registrationNumber IS NULL " +
                            " THEN 'N/A'" +
                            " ELSE hpi.registrationNumber END as registrationNumber," +                          //[5]
                            " p.name as patientName," +                                                          //[6]
                            " p.gender as patientGender," +                                                      //[7]
                            " hpi.isRegistered as isRegistered," +                                              //[8]
                            " p.mobileNumber as mobileNumber," +                                                //[9]
                            " atd.transactionNumber as transactionNumber," +                                    //[10]
                            " COALESCE(atd.appointmentAmount,0) as appointmentAmount," +                        //[11]
                            " a.status as status, " +                                                           //[12]
                            " CASE WHEN" +
                            " a.status = 'RE'" +
                            " THEN " +
                            " (COALESCE(ard.refundAmount,0))" +                                                 //[12]
                            " ELSE" +
                            " 0" +
                            " END AS refundAmount," +
                            " hpi.address as patientAddress," +                                                 //[14]
                            " atd.transactionDate as transactionDate," +                                       //[15]
                            " a.appointmentModeId.name as appointmentMode," +                                   //[16]
                            " a.isFollowUp as isFollowUp," +                                                    //[17]
                            " CASE WHEN" +
                            " a.status!= 'RE'" +
                            " THEN" +
                            " atd.appointmentAmount" +
                            " ELSE" +
                            " (atd.appointmentAmount - COALESCE(ard.refundAmount ,0)) " +        //[20]
                            " END AS revenueAmount," +
                            QUERY_TO_CALCULATE_PATIENT_AGE + "," +                                                //[19]
                            " hd.name as hospitalDepartmentName," +                                             //[20]
                            " hb.billingMode.name as billingModeName," +                                        //[21]
                            " case when hr.id is null then null" +
                            " when hr.id is not null then r.roomNumber" +
                            " end as roomNumber" +                                                             //[22]
                            " FROM Appointment a" +
                            " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                            " LEFT JOIN Patient p ON a.patientId.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " LEFT JOIN AppointmentRefundDetail ard ON a.id=ard.appointmentId" +
                            " LEFT JOIN HospitalBillingModeInfo hb ON hb.id = ahd.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                            " LEFT JOIN Room r ON r.id = hr.room.id" +
                            " WHERE has.appointmentServiceType.code = :appointmentServiceTypeCode"
                            + GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(appointmentLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_HOSPITAL_DEPARTMENT_APPOINTMENT_LOG_DETAILS(
            AppointmentLogSearchDTO appointmentLogSearchDTO) {

        String whereClause = " AND hd.status!='D'";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getFromDate())
                && !ObjectUtils.isEmpty(appointmentLogSearchDTO.getToDate()))
            whereClause += " AND (a.appointmentDate BETWEEN '" + utilDateToSqlDate(appointmentLogSearchDTO.getFromDate())
                    + "' AND '" + utilDateToSqlDate(appointmentLogSearchDTO.getToDate()) + "')";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentLogSearchDTO.getStatus()) && !appointmentLogSearchDTO.getStatus().equals(""))
            whereClause += " AND a.status = '" + appointmentLogSearchDTO.getStatus() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + appointmentLogSearchDTO.getHospitalId();

        if (!Objects.isNull(appointmentLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pi.id = " + appointmentLogSearchDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentLogSearchDTO.getPatientType() + "'";

        if (!ObjectUtils.isEmpty(appointmentLogSearchDTO.getAppointmentCategory()))
            whereClause += " AND a.isSelf = '" + appointmentLogSearchDTO.getAppointmentCategory() + "'";

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id = " + appointmentLogSearchDTO.getHospitalDepartmentId();

        if (!Objects.isNull(appointmentLogSearchDTO.getHospitalDepartmentRoomInfoId()))
            whereClause += " AND hr.id = " + appointmentLogSearchDTO.getHospitalDepartmentRoomInfoId();

        whereClause += " ORDER BY a.appointmentDate DESC ";

        return whereClause;
    }

    public static Function<AppointmentRescheduleLogSearchDTO, String>
            QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_RESCHEDULE_APPOINTMENT_LOGS =
            (appointmentRescheduleLogSearchDTO) ->
                    " SELECT" +
                            " p.eSewaId as esewaId," +                                                               //[0]
                            " arl.previousAppointmentDate as previousAppointmentDate," +                            //[1]
                            " DATE_FORMAT(arl.previousAppointmentDate, '%h:%i %p') as previousAppointmentTime," +   //[2]
                            " arl.rescheduleDate as rescheduleAppointmentDate," +                                   //[3]
                            " DATE_FORMAT(arl.rescheduleDate, '%h:%i %p') as rescheduleAppointmentTime," +          //[4]
                            " a.appointmentNumber as appointmentNumber," +                                          //[5]
                            " hpi.registrationNumber as registrationNumber," +                                     //[6]
                            " p.name as patientName," +                                                            //[7]
                            QUERY_TO_CALCULATE_PATIENT_AGE + "," +                                      //[8]
                            " p.gender as patientGender," +                                               //[9]
                            " p.mobileNumber as mobileNumber," +                                         //[10]
                            " atd.transactionNumber as transactionNumber," +                            //[11]
                            " atd.appointmentAmount as appointmentAmount," +                            //[12]
                            " arl.remarks as remarks," +                                                //[13]
                            " a.isFollowUp as isFollowUp," +                                            //[14]
                            " hd.name as hospitalDepartmentName," +                                     //[15]
                            " hb.billingMode.name as billingModeName," +                                //[16]
                            " case when hr.id is null then null" +
                            " when hr.id is not null then r.roomNumber" +
                            " end as roomNumber," +                                                      //[17]
                            " h.name as hospitalName" +                                                  //[18]
                            " FROM AppointmentRescheduleLog arl" +
                            " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                            " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                            " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                            " LEFT JOIN Patient p ON p.id=a.patientId" +
                            " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                            " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                            " LEFT JOIN Hospital h ON h.id=a.hospitalId" +
                            " LEFT JOIN HospitalBillingModeInfo hb ON hb.id = ahd.hospitalDepartmentBillingModeInfo.id" +
                            " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                            " LEFT JOIN Room r ON r.id = hr.room.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                            " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                            GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(appointmentRescheduleLogSearchDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(
            AppointmentRescheduleLogSearchDTO appointmentRescheduleLogSearchDTO) {

        String fromDate = utilDateToSqlDate(appointmentRescheduleLogSearchDTO.getFromDate()) + " 00:00:00";
        String toDate = utilDateToSqlDate(appointmentRescheduleLogSearchDTO.getToDate()) + " 23:59:59";

        String whereClause = " WHERE " +
                " hpi.status='Y'" +
                " AND arl.status='RES'" +
                " AND hd.status!='D'" +
                " AND arl.rescheduleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'" +
                " AND has.appointmentServiceType.code = :appointmentServiceTypeCode";

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber LIKE '%" + appointmentRescheduleLogSearchDTO.getAppointmentNumber() + "%'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getEsewaId()))
            whereClause += " AND p.eSewaId = '" + appointmentRescheduleLogSearchDTO.getEsewaId() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getAppointmentId()))
            whereClause += " AND a.id = " + appointmentRescheduleLogSearchDTO.getAppointmentId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getPatientMetaInfoId()))
            whereClause += " AND pmi.id = " + appointmentRescheduleLogSearchDTO.getPatientMetaInfoId();

        if (!ObjectUtils.isEmpty(appointmentRescheduleLogSearchDTO.getPatientType()))
            whereClause += " AND hpi.isRegistered = '" + appointmentRescheduleLogSearchDTO.getPatientType() + "'";

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getHospitalDepartmentId()))
            whereClause += " AND hd.id = " + appointmentRescheduleLogSearchDTO.getHospitalDepartmentId();

        if (!Objects.isNull(appointmentRescheduleLogSearchDTO.getHospitalDepartmentRoomInfoId()))
            whereClause += " AND hr.id = " + appointmentRescheduleLogSearchDTO.getHospitalDepartmentRoomInfoId();

        whereClause += " ORDER BY arl.rescheduleDate";

        return whereClause;
    }

    public static String QUERY_TO_CALCULATE_TOTAL_HOSPITAL_DEPT_RESCHEDULE_AMOUNT(AppointmentRescheduleLogSearchDTO searchDTO) {
        return
                "SELECT" +
                        " COALESCE(SUM(atd.appointmentAmount),0)" +
                        " FROM AppointmentRescheduleLog arl" +
                        " LEFT JOIN Appointment a ON a.id=arl.appointmentId.id" +
                        " LEFT JOIN HospitalAppointmentServiceType has ON has.id=a.hospitalAppointmentServiceType.id " +
                        " LEFT JOIN AppointmentHospitalDepartmentInfo ahd ON ahd.appointment.id = a.id" +
                        " LEFT JOIN HospitalDepartment hd ON hd.id = ahd.hospitalDepartment.id" +
                        " LEFT OUTER JOIN HospitalDepartmentRoomInfo hr ON hr.id = ahd.hospitalDepartmentRoomInfo.id" +
                        " LEFT JOIN Patient p ON p.id=a.patientId" +
                        " LEFT JOIN PatientMetaInfo pmi ON pmi.patient.id=p.id" +
                        " LEFT JOIN HospitalPatientInfo hpi ON hpi.patient.id =p.id AND hpi.hospital.id = a.hospitalId.id" +
                        " LEFT JOIN Hospital h ON h.id = a.hospitalId" +
                        " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id" +
                        GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT_RESCHEDULE_LOG_DETAILS(searchDTO);
    }


}
