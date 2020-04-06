package com.cogent.cogentappointment.esewa.query;

import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.esewa.constants.StringConstant.COMMA_SEPARATED;

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

    public static final String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_TIME =
            "SELECT d.startTime as startTime," +                             //[0]
                    " d.endTime as endTime," +                              //[1]
                    " d.dayOffStatus as dayOffStatus," +                    //[2]
                    " dd.rosterGapDuration as rosterGapDuration" +          //[3]
                    " FROM DoctorDutyRosterOverride d" +
                    " LEFT JOIN DoctorDutyRoster dd ON dd.id = d.doctorDutyRosterId.id" +
                    " WHERE" +
                    " d.status = 'Y'" +
                    " AND dd.status = 'Y'" +
                    " AND :date BETWEEN d.fromDate AND d.toDate" +
                    " AND dd.doctorId.id = :doctorId" +
                    " AND dd.specializationId.id = :specializationId";

    public static String QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> overrideUpdateRequestDTOS) {

        String overrideIds = overrideUpdateRequestDTOS.stream()
                .map(request -> request.getDoctorDutyRosterOverrideId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        return " SELECT d FROM DoctorDutyRosterOverride d" +
                " WHERE d.id IN (" + overrideIds + ")";
    }

}
