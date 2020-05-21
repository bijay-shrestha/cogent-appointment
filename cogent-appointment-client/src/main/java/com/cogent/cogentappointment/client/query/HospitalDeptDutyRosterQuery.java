package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterQuery {

    public static final String QUERY_TO_FETCH_HDD_ROSTER_COUNT_WITHOUT_ROOM =
            " SELECT COUNT(d.id)" +
                    " FROM HospitalDepartmentDutyRoster d" +
                    " WHERE" +
                    " d.status != 'D'" +
                    " AND d.isRoomEnabled = 'N'" +
                    " AND d.hospitalDepartment.id= :id" +
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
                " dr.status as status" +                                              //[5]
                " FROM HospitalDepartmentDutyRoster dr" +
                " LEFT JOIN HospitalDepartment hd ON hd.id = dr.hospitalDepartment.id" +
                " WHERE" +
                " dr.status !='D'" +
                " AND hd.hospital.id=:hospitalId" +
                " AND dr.toDate >=:fromDate AND dr.fromDate <=:toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            sql += " AND dr.status='" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getHospitalDepartmentId()))
            sql += " AND hd.id = " + searchRequestDTO.getHospitalDepartmentId();

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
                    " ddr.isRoomEnabled as isRoomEnabled," +
                    HDD_ROSTER_AUDITABLE_QUERY() +
                    " FROM HospitalDepartmentDutyRoster ddr" +
                    " LEFT JOIN HospitalDepartment hd ON hd.id = ddr.hospitalDepartment.id" +
                    " WHERE ddr.status !='D'" +
                    " AND ddr.id = :id" +
                    " AND hd.hospital.id =:hospitalId";

    private static String HDD_ROSTER_AUDITABLE_QUERY() {
        return " ddr.createdBy as createdBy," +
                " ddr.createdDate as createdDate," +
                " ddr.lastModifiedBy as lastModifiedBy," +
                " ddr.lastModifiedDate as lastModifiedDate";
    }

    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL =
            " SELECT" +
                    " h.id as rosterRoomId," +                                          //[0]
                    " h.room.id as roomId," +                                           //[1]
                    " h.room.roomNumber as roomNumber" +                                //[2]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

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

    public static final String QUERY_TO_FETCH_HDD_ROSTER_OVERRIDE_DETAILS =
            "SELECT" +
                    " d.id as rosterOverrideId," +                      //[0]
                    " d.fromDate as fromDate," +                        //[1]
                    " d.toDate as toDate," +                            //[2]
                    " d.startTime as startTime," +                      //[3]
                    " d.endTime as endTime," +                          //[4]
                    " d.dayOffStatus as dayOffStatus," +                //[5]
                    " d.remarks as remarks" +                           //[6]
                    " FROM HospitalDepartmentDutyRosterOverride d" +
                    " WHERE" +
                    " d.hospitalDepartmentDutyRoster.status!= 'D'" +
                    " AND d.status = 'Y'" +
                    " AND d.hospitalDepartmentDutyRoster.id = :id";

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
                    " AND dd.fromDate <=:toDate" +
                    " AND hd.hospital.id=:hospitalId";


    public static String QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER =
            " SELECT" +
                    " h.room.roomNumber as roomNumber" +                                //[0]
                    " FROM HospitalDepartmentDutyRosterRoomInfo h" +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospitalDepartmentDutyRoster.id =:id";

    public static final String QUERY_TO_CHECK_IF_OVERRIDE_EXISTS =
            " SELECT" +
                    " dd.hasOverrideDutyRoster as hasOverrideDutyRoster" +             //[0]
                    " FROM HospitalDepartmentDutyRoster dd" +
                    " WHERE dd.status !='D'" +
                    " AND dd.id = :id";

}
