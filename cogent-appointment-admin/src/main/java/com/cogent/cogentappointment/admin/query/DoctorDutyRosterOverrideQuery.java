package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;

public class DoctorDutyRosterOverrideQuery {

    public static final String VALIDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_COUNT =
            " SELECT COUNT(d.id)" +
                    " FROM DoctorDutyRosterOverride d" +
                    " LEFT JOIN DoctorDutyRoster dr ON dr.id = d.doctorDutyRosterId.id" +
                    " WHERE dr.status != 'D'" +
                    " AND d.status = 'Y'" +
                    " AND dr.doctorId.id=:doctorId" +
                    " AND dr.specializationId.id= :specializationId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static final String VALIDATE_DOCTOR_DUTY_ROSTER_OVERRIDE_COUNT_FOR_UPDATE =
            " SELECT COUNT(d.id)" +
                    " FROM DoctorDutyRosterOverride d" +
                    " LEFT JOIN DoctorDutyRoster dr ON dr.id = d.doctorDutyRosterId.id" +
                    " WHERE dr.status != 'D'" +
                    " AND d.status = 'Y'" +
                    " AND d.id!=:id" +
                    " AND dr.doctorId.id=:doctorId" +
                    " AND dr.specializationId.id= :specializationId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS =
            "SELECT" +
                    " d.id as doctorDutyRosterOverrideId," +            //[0]
                    " d.fromDate as fromDate," +                        //[1]
                    " d.toDate as toDate," +                            //[2]
                    " d.startTime as startTime," +                      //[3]
                    " d.endTime as endTime," +                          //[4]
                    " d.dayOffStatus as dayOffStatus," +                 //[5]
                    " d.remarks as remarks" +                            //[6]
                    " FROM DoctorDutyRosterOverride d" +
                    " WHERE" +
                    " d.doctorDutyRosterId.status!= 'D'" +
                    " AND d.status = 'Y'" +
                    " AND d.doctorDutyRosterId.id = :id";

    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_STATUS(AppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT" +
                " d.fromDate," +                                            //[0]
                " d.toDate," +                                              //[1]
                " DATE_FORMAT(d.startTime, '%H:%i') as startTime," +        //[2]
                " DATE_FORMAT(d.endTime, '%H:%i') as endTime," +            //[3]
                " d.dayOffStatus," +                                        //[4]
                " dr.id as doctorId," +                                      //[5]
                " dr.name as doctorName," +                                  //[6]
                " dd.specializationId.id as specializationId," +            //[7]
                " dd.specializationId.name as specializationName," +        //[8]
                " dd.rosterGapDuration as rosterGapDuration," +             //[9]
                " dr.salutation as doctorSalutation"+
                " FROM DoctorDutyRosterOverride d" +
                " LEFT JOIN DoctorDutyRoster dd ON dd.id = d.doctorDutyRosterId.id" +
                " LEFT JOIN Hospital h ON h.id = dd.hospitalId.id" +
                " LEFT JOIN Doctor dr ON dr.id = dd.doctorId.id" +
                " WHERE" +
                " d.status = 'Y'" +
                " AND dd.status = 'Y'" +
                " AND dd.toDate >=:fromDate" +
                " AND dd.fromDate <=:toDate";

        if (!Objects.isNull(requestDTO.getDoctorId()))
            SQL += " AND dr.id = :doctorId";

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            SQL += " AND dd.specializationId.id = :specializationId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id = :hospitalId";

        return SQL;
    }

    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> overrideUpdateRequestDTOS) {

        String overrideIds = overrideUpdateRequestDTOS.stream()
                .map(request -> request.getDoctorDutyRosterOverrideId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        return " SELECT d FROM DoctorDutyRosterOverride d" +
                " WHERE d.id IN (" + overrideIds + ")";
    }


    public static String QUERY_TO_GET_OVERRIDE_TIME_BY_ROSTER_ID=
            "SELECT " +
                    " DATE_FORMAT(hddro.start_time,'%H:%i') as startTime , " +
                    " DATE_FORMAT(hddro.end_time ,'%H:%i') as endTime " +
                    " FROM " +
                    " doctor_duty_roster_override hddro " +
                    " WHERE " +
                    " doctor_duty_roster_id = :doctorDutyRosterId " +
                    " AND (hddro.from_date <= :date " +
                    " AND hddro.to_date >=:date)";



}
