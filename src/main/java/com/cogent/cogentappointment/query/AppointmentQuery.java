package com.cogent.cogentappointment.query;

import com.cogent.cogentappointment.dto.request.appointment.AppointmentSearchRequestDTO;
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
            "SELECT startTime as startTime," +                          //[0]
                    " endTime as endTime" +                             //[1]
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
                    " a.status!='C'" +
                    " AND a.appointmentDate BETWEEN :fromDate AND :toDate" +
                    " AND a.doctorId.id = :doctorId" +
                    " AND a.specializationId.id = :specializationId";

    public static String QUERY_TO_FETCH_ACTIVE__BY_ID =
            "SELECT a," +
                    " a.patientTypeId," +
                    " a.appointmentTypeId," +
                    " a.appointmentModeId," +
                    " a.doctorId," +
                    " dt.gender," +
                    " dt.country," +
                    " a.billType," +
                    " a.patientId," +
                    " p.surname," +
                    " p.religion," +
                    " p.maritalStatus," +
                    " p.nationality," +
                    " p.municipality," +
                    " m.district," +
                    " d.provinces," +
                    " p.category," +
                    " p.title, " +
                    " s.ethnicity" +
                    " FROM Appointment a" +
                    " LEFT JOIN Patient p ON p.id=a.patientId.id" +
                    " LEFT JOIN Surname s ON s.id=p.surname.id" +
                    " LEFT JOIN Municipality m ON m.id=p.municipality.id" +
                    " LEFT JOIN District d ON d.id=m.district.id" +
                    " LEFT JOIN Doctor dt ON dt.id=a.doctorId.id" +
                    " WHERE" +
                    " a.id=:id" +
                    " AND a.status='Y'";
}
