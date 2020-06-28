package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;

import java.util.List;
import java.util.Objects;

/**
 * @author Sauravi Thapa ON 6/28/20
 */
public class AppointmentStatusCountQuery {

//    public static String QUERY_TO_FETCH_ROSTER_DETAILS_FOR_STATUS_COUNT=
//            "SELECT" +
//                    " hddr.id as rosterId, " +
//                    " hddr.rosterGapDuration as rosterGapDuration," +
//                    " DATE_FORMAT(wddr.startTime,'%H:%i') as startTime," +
//                    " DATE_FORMAT(wddr.endTime,'%H:%i') as endTime," +
//                    " hd.id as hospitalDepartmentId," +
//                    " hd.name as hospitalDepartmentName" +
//                    " FROM HospitalDepartmentDutyRoster hddr" +
//                    " LEFT JOIN HospitalDepartment hd ON hd.id=hddr.hospitalDepartment.id " +
//                    " LEFT JOIN HospitalDepartmentDutyRosterRoomInfo ri ON ri.hospitalDepartmentDutyRoster.id = hddr.id" +
//                    " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster wddr ON wddr.hospitalDepartmentDutyRoster.id = hddr.id" +
//                    " LEFT JOIN WeekDays wd ON wd.id = wddr.weekDays.id" +
//                    " WHERE" +
//                    " hddr.fromDate <=:toDate" +
//                    " AND hddr.toDate >= :fromDate" +
//                    " AND hddr.hospital.id =:hospitalId" +
//                    " AND wd.name IN (:weekDayName)";

    public static String QUERY_TO_FETCH_ROSTER_DETAILS_FOR_STATUS_COUNT(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL =
                "SELECT" +
                        " hddr.from_date as fromDate," +                                                           //[0]
                        " hddr.to_date as toDate," +                                                               //[1]
                        " hddr.roster_gap_duration as rosterGapDuration," +                                        //[2]
                        " hd.id as hospitalDepartmentId," +                                                        //[3]
                        " hd.name as hospitalDepartmentName," +                                                    //[4]
                        " GROUP_CONCAT((CONCAT( DATE_FORMAT(dw.start_time, '%H:%i'), " +
                        "'-', DATE_FORMAT(dw.end_time, '%H:%i'), '-', dw.day_off_status, '-', w.name)))" +
                        " as timeDetails," +                                                                      //[5]
                        " hddr.id as rosterId" +                                                                  //[6]
                        " FROM" +
                        " hospital_department_duty_roster hddr" +
                        " LEFT JOIN hospital_department_week_days_duty_roster dw ON dw.hospital_department_duty_roster_id = hddr.id" +
                        " LEFT JOIN week_days w ON w.id = dw.week_days_id" +
                        " LEFT JOIN hospital h ON h.id = hddr.hospital_id" +
                        " LEFT JOIN hospital_department hd ON hd.id = hddr.hospital_department_id" +
                        " LEFT JOIN hospital_department_duty_roster_room_info hdrri ON hdrri.hospital_department_duty_roster_id=hddr.id" +
                        " LEFT JOIN hospital_department_room_info hdri ON hdri.id=hdrri.hospital_department_room_info_id " +
                        " LEFT JOIN room r ON r.id=hdri.room_id " +
                        " WHERE" +
                        " hddr.status = 'Y'" +
                        " AND hd.status = 'Y'" +
                        " AND hddr.to_date >=:fromDate" +
                        " AND hddr.from_date <=:toDate" +
                        " AND hddr.hospital_id=:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hd.id = :hospitalDepartmentId";

        SQL +=" GROUP BY hddr.id";

        return SQL;
    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_STATUS_COUNT(
            HospitalDeptAppointmentStatusRequestDTO requestDTO, List<Long> rosterIdList) {

        String SQL =
                "SELECT" +
                        " hddro.fromDate as fromDate," +
                        " hddro.toDate as toDate," +
                        " DATE_FORMAT(hddro.startTime, '%H:%i') as startTime," +
                        " DATE_FORMAT(hddro.endTime, '%H:%i') as endTime," +
                        " hddr.rosterGapDuration as rosterGapDuration," +
                        " hd.id as hospitalDepartmentId," +
                        " hd.name as hospitatDepartmentName," +
                        " hddr.id as rosterId" +
                        " FROM" +
                        " HospitalDepartmentDutyRosterOverride hddro" +
                        " LEFT JOIN HospitalDepartmentDutyRoster hddr ON hddr.id = hddro .hospitalDepartmentDutyRoster.id" +
                        " LEFT JOIN HospitalDepartment hd ON hddr.hospitalDepartment.id=hd.id" +
                        " LEFT JOIN HospitalDepartmentDutyRosterRoomInfo ri ON ri.hospitalDepartmentDutyRoster.id=hddr.id" +
                        " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.id=ri.hospitalDepartmentRoomInfo.id" +
                        " LEFT JOIN Room r ON r.id=hdri.room.id " +
                        " WHERE" +
                        " hddro.status = 'Y'" +
                        " AND hddr.status = 'Y'" +
                        " AND hddro.toDate >=:fromDate" +
                        " AND hddro.fromDate <=:toDate";

        if (rosterIdList.size() > 0)
            SQL += " AND hddr.id IN (:hospitalDepartmentDutyRosterId) ";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hd.id = :hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND hddr.hospital.id = :hospitalId";

        return SQL;
    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_COUNT(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT " +
                " COUNT(a.id) as count," +
                " a.status as status" +
                " FROM AppointmentHospitalDepartmentInfo ahdi " +
                " LEFT JOIN Appointment a ON a.id = ahdi.appointment.id " +
                " LEFT JOIN HospitalDepartment hd ON hd.id=ahdi.hospitalDepartment.id  " +
                " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.id=ahdi.hospitalDepartmentRoomInfo.id  " +
                " LEFT JOIN Room r ON r.id=hdri.room.id  " +
                " LEFT JOIN Patient p ON p.id=a.patientId.id  " +
                " LEFT JOIN Hospital h ON h.id=a.hospitalId.id  " +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id" +
                " LEFT JOIN AppointmentServiceType ast ON ast.id=hast.appointmentServiceType.id  " +
                " WHERE " +
                " a.appointmentDate BETWEEN :fromDate AND :toDate " +
                " AND ast.code=:appointmentServiceTypeCode";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND ahdi.hospitalDepartment.id =:hospitalDepartmentId";

        SQL += " GROUP BY a.status ";

        return SQL;
    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_APPOINTMENT_FOR_APPOINTMENT_STATUS_FOLLOW_UP_COUNT(
            HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT " +
                " COUNT(a.id) as count" +
                " FROM AppointmentHospitalDepartmentInfo ahdi " +
                " LEFT JOIN Appointment a ON a.id = ahdi.appointment.id " +
                " LEFT JOIN HospitalDepartment hd ON hd.id=ahdi.hospitalDepartment.id  " +
                " LEFT JOIN HospitalDepartmentRoomInfo hdri ON hdri.id=ahdi.hospitalDepartmentRoomInfo.id  " +
                " LEFT JOIN Room r ON r.id=hdri.room.id  " +
                " LEFT JOIN Patient p ON p.id=a.patientId.id  " +
                " LEFT JOIN Hospital h ON h.id=a.hospitalId.id  " +
                " LEFT JOIN HospitalAppointmentServiceType hast ON hast.id=a.hospitalAppointmentServiceType.id" +
                " LEFT JOIN AppointmentServiceType ast ON ast.id=hast.appointmentServiceType.id  " +
                " WHERE " +
                " a.isFollowUp='Y'" +
                " AND a.status!='RE'" +
                " AND a.appointmentDate BETWEEN :fromDate AND :toDate " +
                " AND ast.code=:appointmentServiceTypeCode";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id =:hospitalId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND ahdi.hospitalDepartment.id =:hospitalDepartmentId";

        SQL += " GROUP BY a.isFollowUp ";

        return SQL;
    }


}
