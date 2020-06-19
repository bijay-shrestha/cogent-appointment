package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterQuery {

    public static final String QUERY_TO_FETCH_HDD_ROSTER_STATUS =
            " SELECT MAX(d.isRoomEnabled)" +
                    " FROM HospitalDepartmentDutyRoster d" +
                    " WHERE" +
                    " d.status != 'D'" +
                    " AND d.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_HDD_ROSTER_STATUS_EXCEPT_CURRENT_ID =
            " SELECT MAX(d.isRoomEnabled)" +
                    " FROM HospitalDepartmentDutyRoster d" +
                    " WHERE" +
                    " d.status != 'D'" +
                    " AND d.id != :id" +
                    " AND d.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static String QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT_DUTY_ROSTER(
            HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO) {

        String sql = " SELECT" +
                " dr.id as hddRosterId," +                                             //[0]
                " hd.name as hospitalDeptName," +                                      //[1]
                " dr.rosterGapDuration as rosterGapDuration," +                        //[2]
                " dr.fromDate as fromDate," +                                          //[3]
                " dr.toDate as toDate," +                                              //[4]
                " dr.status as status," +                                              //[5],
                " dr.hospital.name as hospitalName," +                                 //[6]
                " dr.isRoomEnabled as isRoomEnabled," +                                 //[7]
                " CASE WHEN dr.isRoomEnabled = 'N' THEN null" +
                " WHEN dr.isRoomEnabled = 'Y' THEN hri.room.roomNumber" +
                " END AS roomNumber" +                                                 //[8]
                " FROM HospitalDepartmentDutyRoster dr" +
                " LEFT JOIN HospitalDepartment hd ON hd.id = dr.hospitalDepartment.id" +
                " LEFT OUTER JOIN HospitalDepartmentDutyRosterRoomInfo hr ON dr.id = hr.hospitalDepartmentDutyRoster.id" +
                " LEFT JOIN HospitalDepartmentRoomInfo hri ON hri.id = hr.hospitalDepartmentRoomInfo.id" +
                " WHERE" +
                " dr.status !='D'" +
                " AND dr.toDate >=:fromDate AND dr.fromDate <=:toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            sql += " AND dr.status='" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalDepartmentId()))
            sql += " AND hd.id = " + searchRequestDTO.getHospitalDepartmentId();

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            sql += " AND  hd.hospital.id= " + searchRequestDTO.getHospitalId();

        return sql + " ORDER BY dr.id DESC";
    }

    public static final String QUERY_TO_FETCH_HDD_ROSTER_DETAIL =

            " SELECT" +
                    " ddr.id as hddRosterId," +                                         //[0]
                    " hd.id as hospitalDeptId," +                                       //[1]
                    " hd.name as hospitalDeptName," +                                   //[2]
                    " ddr.rosterGapDuration as rosterGapDuration," +                    //[3]
                    " ddr.fromDate as fromDate," +                                      //[4]
                    " ddr.toDate as toDate," +                                          //[5]
                    " ddr.status as status," +                                          //[6]
                    " ddr.remarks as remarks," +                                        //[7]
                    " ddr.hasOverrideDutyRoster as hasOverrideDutyRoster," +            //[8]
                    " ddr.isRoomEnabled as isRoomEnabled," +                            //[9]
                    " ddr.hospital.name as hospitalName," +                             //[10]
                    " ddr.hospital.id as hospitalId," +                                  //[11]
                    HDD_ROSTER_AUDITABLE_QUERY() +
                    " FROM HospitalDepartmentDutyRoster ddr" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ddr.hospitalDepartment.id" +
                    " WHERE ddr.status !='D'" +
                    " AND ddr.id = :id";

    private static String HDD_ROSTER_AUDITABLE_QUERY() {
        return " ddr.createdBy as createdBy," +
                " ddr.createdDate as createdDate," +
                " ddr.lastModifiedBy as lastModifiedBy," +
                " ddr.lastModifiedDate as lastModifiedDate";
    }

    public static final String QUERY_TO_FETCH_HDD_WEEK_DAYS_DETAIL =
            " SELECT" +
                    " dw.id as rosterWeekDaysId," +                                     //[0]
                    " dw.startTime as startTime," +                                     //[1]
                    " dw.endTime as endTime," +                                         //[2]
                    " dw.dayOffStatus as dayOffStatus," +                               //[3]
                    " dw.weekDays.id as weekDaysId," +                                  //[4]
                    " dw.weekDays.name as weekDaysName" +                               //[5]
                    " FROM HospitalDepartmentWeekDaysDutyRoster dw" +
                    " WHERE" +
                    " dw.hospitalDepartmentDutyRoster.status !='D'" +
                    " AND dw.hospitalDepartmentDutyRoster.id = :id";

    public static final String QUERY_TO_FETCH_EXISTING_DUTY_ROSTER =
            " SELECT" +
                    " dd.id as hddRosterId," +                                             //[0]
                    " dd.fromDate as fromDate," +                                          //[1]
                    " dd.toDate as toDate," +                                              //[2]
                    " dd.rosterGapDuration as rosterGapDuration," +                        //[3]
                    " dd.isRoomEnabled as isRoomEnabled" +                                 //[4]
                    " FROM HospitalDepartmentDutyRoster dd" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = dd.hospitalDepartment.id" +
                    " WHERE dd.status != 'D'" +
                    " AND hd.id=:id" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_STATUS_WITH_ROOM
            (HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT" +
                " hddr.from_date as fromDate," +
                " hddr.to_date as toDate," +
                " hddr.roster_gap_duration as gapDuration," +
                " hd.id as hospitalDepartmentId," +
                " hd.name as hospitalDepartmentName," +
                " hdri.id  as roomId," +
                " r.room_number as roomNumber," +
                " GROUP_CONCAT((CONCAT( DATE_FORMAT(dw.start_time, '%H:%i'), '-', DATE_FORMAT(dw.end_time, '%H:%i'), '-', dw.day_off_status, '-', w.name))) as doctorTimeDetails," +
                " hddr.id as rosterId" +
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
                " AND hddr.is_room_enabled='Y'" +
                " AND hddr.to_date >=:fromDate" +
                " AND hddr.from_date <=:toDate";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hd.id = :hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id = :hospitalId";

        SQL += " GROUP BY hddr.to_date,hddr.from_date ";

        return SQL;

    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_STATUS_WITHOUT_ROOM
            (HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT" +
                " hddr.from_date as fromDate," +
                " hddr.to_date as toDate," +
                " hddr.roster_gap_duration as gapDuration," +
                " hd.id as hospitalDepartmentId," +
                " hd.name as hospitalDepartmentName," +
                " CASE WHEN hddr.is_room_enabled='N' " +
                " THEN null" +
                " ELSE hdri.id END as roomId," +
                " CASE WHEN hddr.is_room_enabled='N' " +
                " THEN 'N/A'" +
                " ELSE r.room_number END as roomNumber," +
                " GROUP_CONCAT((CONCAT( DATE_FORMAT(dw.start_time, '%H:%i'), '-', DATE_FORMAT(dw.end_time, '%H:%i')," +
                " '-', dw.day_off_status, '-', w.name))) as doctorTimeDetails," +
                " hddr.id as rosterId" +
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
                " AND hddr.is_room_enabled='N'";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hd.id = :hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id = :hospitalId";

        SQL += " GROUP BY hddr.id";

        return SQL;

    }

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_STATUS_ROOM_WISE
            (HospitalDeptAppointmentStatusRequestDTO requestDTO) {

        String SQL = "SELECT" +
                " hddr.from_date as fromDate," +
                " hddr.to_date as toDate," +
                " hddr.roster_gap_duration as gapDuration," +
                " hd.id as hospitalDepartmentId," +
                " hd.name as hospitalDepartmentName," +
                " CASE WHEN hddr.is_room_enabled='N' " +
                " THEN null" +
                " ELSE hdri.id END as roomId," +
                " CASE WHEN hddr.is_room_enabled='N' " +
                " THEN 'N/A'" +
                " ELSE r.room_number END as roomNumber," +
                " GROUP_CONCAT((CONCAT( DATE_FORMAT(dw.start_time, '%H:%i'), '-', DATE_FORMAT(dw.end_time, '%H:%i'), '-', dw.day_off_status, '-', w.name))) as doctorTimeDetails," +
                " hddr.id as rosterId" +
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
                " AND hddr.from_date <=:toDate";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hd.id = :hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            SQL += " AND hdri.id = :hospitalDepartmentRoomInfoId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND h.id = :hospitalId";

        SQL += " GROUP BY hddr.id";

        return SQL;

    }

    public static String QUERY_TO_FETCH_ROSTER_DETAILS_BY_HOSPITAL_DEPARTMENT_ID(Long hospitalDepartmentRoomInfoId) {
        String query = "SELECT" +
                " hddr.id as rosterId," +
                " hddr.rosterGapDuration as rosterGapDuration," +
                " DATE_FORMAT(hdwddr.startTime ,'%H:%i') as startTime," +
                " DATE_FORMAT(hdwddr.endTime ,'%H:%i') as endTime," +
                " hdwddr.dayOffStatus as dayOffStatus," +
                " wd.name as weekDayName," +
                " hddr.hasOverrideDutyRoster as hasRosterOverRide" +
                " FROM" +
                " HospitalDepartmentDutyRoster hddr" +
                " LEFT JOIN HospitalDepartmentWeekDaysDutyRoster hdwddr ON " +
                " hdwddr.hospitalDepartmentDutyRoster.id=hddr.id" +
                " LEFT JOIN WeekDays wd ON wd.id=hdwddr.weekDays.id " +
                " LEFT JOIN HospitalDepartmentWeekDaysDutyRosterDoctorInfo di ON " +
                " di.hospitalDepartmentWeekDaysDutyRoster.id=hdwddr.id" +
                " LEFT JOIN HospitalDepartmentDutyRosterRoomInfo hddrri ON hddrri.hospitalDepartmentDutyRoster.id=hddr.id " +
                " WHERE hddr.hospitalDepartment.id=:hospitalDepartmentId " +
                " AND" +
                " (hddr.fromDate <= :date" +
                " AND hddr.toDate >=:date)" +
                " AND wd.name =DATE_FORMAT(:date,'%W') ";

        if (!Objects.isNull(hospitalDepartmentRoomInfoId))
            query+= " AND hddrri.hospitalDepartmentRoomInfo.id=:hospitalDepartmentRoomInfoId";

            query += " GROUP BY hddr.id";

        return query;
    }

}
