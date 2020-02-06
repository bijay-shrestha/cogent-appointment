package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author smriti on 2019-10-22
 */
public class AppointmentQuery {

    public static Function<AppointmentSearchRequestDTO, String> QUERY_TO_SEARCH_APPOINTMENT =
            (searchRequestDTO) -> " SELECT" +
                    " a.id as appointmentId," +                                 //[0]
                    " a.patientTypeId.name as patientTypeName," +               //[1]
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS patientName, " +                                    //[2]
                    " a.appointmentTypeId.name as appointmentTypeName," +       //[3]
                    " a.appointmentModeId.name as appointmentModeName," +       //[4]
                    " a.doctorId.name as doctorName," +                          //[5]
                    " a.billType.name as billTypeName," +                       //[6]
                    " a.appointmentDate as appointmentDate," +                  //[7]
                    " a.startTime as startTime," +                             //[8]
                    " a.endTime as endTime," +                                 //[9]
                    " a.appointmentNumber as appointmentNumber," +             //[10]
                    " a.status as status" +                                    //[11]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON p.id = pmi.patient"
                    + GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT(searchRequestDTO);

    private static String GET_WHERE_CLAUSE_TO_SEARCH_APPOINTMENT(AppointmentSearchRequestDTO searchRequestDTO) {

        String whereClause = " WHERE a.status!= 'C'";

        if (!Objects.isNull(searchRequestDTO.getPatientTypeId()))
            whereClause += " AND a.patientTypeId = " + searchRequestDTO.getPatientTypeId();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getPatientMetaInfo()))
            whereClause += " AND pmi.metaInfo LIKE '%" + searchRequestDTO.getPatientMetaInfo() + "%'";

        if (!Objects.isNull(searchRequestDTO.getAppointmentTypeId()))
            whereClause += " AND a.appointmentTypeId = " + searchRequestDTO.getAppointmentTypeId();

        if (!Objects.isNull(searchRequestDTO.getAppointmentModeId()))
            whereClause += " AND a.appointmentModeId = " + searchRequestDTO.getAppointmentModeId();

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            whereClause += " AND a.doctorId = " + searchRequestDTO.getDoctorId();

        if (!Objects.isNull(searchRequestDTO.getBillType()))
            whereClause += " AND a.billType = " + searchRequestDTO.getBillType();

        if (!ObjectUtils.isEmpty(searchRequestDTO.getAppointmentNumber()))
            whereClause += " AND a.appointmentNumber = '" + searchRequestDTO.getAppointmentNumber() + "'";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            whereClause += " AND a.status='" + searchRequestDTO.getStatus() + "'";

        return whereClause;
    }

    public static final String QUERY_TO_FETCH_APPOINTMENT_DETAILS =
            " SELECT" +
                    " a.patientTypeId.id as patientTypeId," +                //[0]
                    " a.patientTypeId.name as patientTypeName," +            //[1]
                    " p.id as patientId," +                                  //[2]
                    " CASE WHEN" +
                    " p.middleName iS NULL OR p.middleName =''" +
                    " THEN" +
                    " CONCAT" +
                    " (p.firstName, ' ', s.name) " +
                    " ELSE" +
                    " CONCAT" +
                    " (p.firstName, ' ',p.middleName,' ', s.name)" +
                    " END AS patientName, " +                                //[3]
                    " a.appointmentTypeId.id as appointmentTypeId," +        //[4]
                    " a.appointmentTypeId.name as appointmentTypeName," +    //[5]
                    " a.appointmentModeId.id as appointmentModeId," +        //[6]
                    " a.appointmentModeId.name as appointmentModeName," +    //[7]
                    " a.doctorId.id as doctorId," +                          //[8]
                    " a.doctorId.name as doctorName," +                      //[9]
                    " a.billType.id as billTypeId," +                       //[10]
                    " a.billType.name as billTypeName," +                   //[11]
                    " a.appointmentDate as appointmentDate," +             //[12]
                    " a.startTime as startTime," +                          //[13]
                    " a.endTime as endTime," +                               //[14]
                    " a.appointmentNumber as appointmentNumber," +          //[15]
                    " a.status as status," +                                //[16]
                    " a.reason as reason," +                                //[17]
                    " a.emergency as emergency," +                          //[18]
                    " a.referredBy as referredBy," +                        //[19]
                    " a.remarks as remarks," +                               //[20]
                    " a.specializationId.id as specializationId, " +        //[21]
                    " a.specializationId.name as specializationName" +      //[22]
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id = a.patientId" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN PatientMetaInfo pmi ON p.id = pmi.patient" +
                    " WHERE a.id =:id";

    public static String QUERY_TO_FETCH_LATEST_APPOINTMENT_NUMBER =
            "SELECT appointment_number" +
                    " FROM appointment" +
                    " WHERE" +
                    " str_to_date(created_date_nepali,'%Y-%m-%d')" +
                    " BETWEEN :fromDate AND :toDate" +
                    " ORDER BY id DESC LIMIT 1";

    public static String QUERY_TO_FETCH_BOOKED_APPOINTMENT =
            "SELECT DATE_FORMAT(a.startTime, '%H:%i') as startTime," +                          //[0]
                    " DATE_FORMAT(a.endTime, '%H:%i') as endTime" +                             //[1]
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.appointmentDate = :date" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId" +
                    " AND a.status != 'C'";

//    public static String QUERY_TO_FETCH_APPOINTMENT_FOR_APPOINTMENT_STATUS(AppointmentStatusRequestDTO requestDTO) {
//
//        String SQL = " SELECT" +
//                " a.appointment_date as date," +                                                        //[0]
//                " GROUP_CONCAT(DATE_FORMAT(a.start_time, '%H:%i'), '-', a.status)" +
//                " as startTimeDetails," +                                                               //[1]
//                " d.id as doctorId," +                                                                  //[2]
//                " d.name as doctorName," +                                                              //[3]
//                " s.id as specializationId," +                                                          //[4]
//                " s.name as specializationName" +                                                       //[5]
//                " FROM appointment a" +
//                " LEFT JOIN doctor d ON d.id = a.doctor_id" +
//                " LEFT JOIN specialization s ON s.id = a.specialization_id" +
//                " WHERE" +
//                " a.appointment_date BETWEEN :fromDate AND :toDate";
//
//        if (!Objects.isNull(requestDTO.getDoctorId()))
//            SQL += " AND d.id =:doctorId";
//
//        if (!Objects.isNull(requestDTO.getSpecializationId()))
//            SQL += " AND s.id = :specializationId ";
//
//        SQL += " GROUP BY a.appointment_date, a.doctor_id, a.specialization_id";
//
//        return SQL;
//    }

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_DATES =
            "SELECT" +
                    " a.appointmentDate as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    public static final String QUERY_TO_FETCH_BOOKED_APPOINTMENT_COUNT =
            "SELECT" +
                    " COUNT(a.appointmentDate) as appointmentDate" +
                    " FROM Appointment a" +
                    " WHERE" +
                    " a.status='PA'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";


    public static Function<Long, String> QUERY_TO_FETCH_APPOINTMENT_VISIT_APPROVAL_DETAILS =
            (hospitalId) ->
                    "SELECT" +
                            " h.name as hospitalName," +
                            " a.appointmentDate as appointmentDate," +
                            " a.appointmentNumber as appointmentNumber," +
                            " a.appointmentTime as appointmentTime," +
                            " p.eSewaId as esewaId," +
                            " p.registrationNumber as registrationNumber," +
                            " p.name as patientName," +
                            " p.gender as patientGender," +
                            " p.isRegistered as isRegistered," +
                            " p.isSelf as isSelf," +
                            " p.mobileNumber as mobileNumber," +
                            " sp.name as specializationName," +
                            " atd.transactionNumber as transactionNumber,atd.appointmentAmount as appointmentAmount" +
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospitalId=h.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" +
                            " WHERE p.status='Y' " +
                            " AND sp.status='Y' " +
                            " AND a.status='PA' " +
                            " AND a.hospitalId=" + hospitalId;

    public static Function<AppointmentPendingApprovalSearchDTO, String> QUERY_TO_FETCH_PENDING_APPOINTMENT_VISIT_APPROVAL_DETAILS =
            (searchRequestDTO) ->
                    "SELECT" +
                            " h.name as hospitalName," +
                            " a.appointmentDate as appointmentDate," +
                            " a.appointmentNumber as appointmentNumber," +
                            " a.appointmentTime as appointmentTime," +
                            " p.eSewaId as esewaId," +
                            " p.registrationNumber as registrationNumber," +
                            " p.name as patientName," +
                            " p.gender as patientGender," +
                            " p.dateOfBirth as patientDob," +
                            " p.isRegistered as isRegistered," +
                            " p.isSelf as isSelf," +
                            " p.mobileNumber as mobileNumber," +
                            " sp.name as specializationName," +
                            " atd.transactionNumber as transactionNumber,atd.appointmentAmount as appointmentAmount," +
                            " pi.id as patientMetaInfoId" +
                            " FROM Appointment a" +
                            " LEFT JOIN Patient p ON a.patientId=p.id" +
                            " LEFT JOIN Specialization sp ON a.specializationId=sp.id" +
                            " LEFT JOIN Hospital h ON a.hospital.id=h.id" +
                            " LEFT JOIN PatientMetaInfo pi ON pi.patient.id=p.id" +
                            " LEFT JOIN AppointmentTransactionDetail atd ON a.id = atd.appointment.id" + GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(searchRequestDTO);

    private static String ageCalculator() {

        String calculateAge = "" +
                "CASE value" +
                "WHEN TIMESTAMPDIFF(YEAR, p.dateOfBirth, now())==0 THEN empty" +
                "END" +
                "(TIMESTAMPDIFF( MONTH,  p.dateOfBirth, now()) % 12)" +
                " as patientAge, ";

//        String calculateAge = " year(CURDATE())- year(p.dateOfBirth) AS patientAge,";

//        String calculateAge = "TIMESTAMPDIFF(YEAR, p.dateOfBirth, now())" +
//                        " TIMESTAMPDIFF( MONTH, p.dateOfBirth, now() ) % 12 as _month," +
//                " FLOOR( TIMESTAMPDIFF( DAY, p.dateOfBirth, now() ) % 30.4375 ) as _day, ";

        return calculateAge;
    }


    private static String GET_WHERE_CLAUSE_TO_SEARCH_PENDING_APPOINTMENT_DETAILS(AppointmentPendingApprovalSearchDTO pendingApprovalSearchDTO) {

        String whereClause = " WHERE a.status!= 'C'" +
                " AND p.status='Y' " +
                " AND sp.status='Y' " +
                " AND a.status='PA'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate";

        if (!Objects.isNull(pendingApprovalSearchDTO.getHospitalId()))
            whereClause += " AND h.id = " + pendingApprovalSearchDTO.getHospitalId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientId()))
            whereClause += " AND p.id = " + pendingApprovalSearchDTO.getPatientId();


        if (!Objects.isNull(pendingApprovalSearchDTO.getSpecializationId()))
            whereClause += " AND sp.id = " + pendingApprovalSearchDTO.getSpecializationId();

        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientType()))
            whereClause += " AND p.isRegistered = '" + pendingApprovalSearchDTO.getPatientType() + "'";


        if (!Objects.isNull(pendingApprovalSearchDTO.getPatientCategory()))
            whereClause += " AND p.isSelf = '" + pendingApprovalSearchDTO.getPatientCategory() + "'";

        if (!Objects.isNull(pendingApprovalSearchDTO.getDoctorId()))
            whereClause += " AND a.doctorId = " + pendingApprovalSearchDTO.getDoctorId();

        return whereClause;
    }


}