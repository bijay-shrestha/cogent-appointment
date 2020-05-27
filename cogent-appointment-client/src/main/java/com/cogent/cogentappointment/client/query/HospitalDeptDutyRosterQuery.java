package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
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
                    " AND dd.fromDate <=:toDate" +
                    " AND hd.hospital.id=:hospitalId";
}
