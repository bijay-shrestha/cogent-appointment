package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;

/**
 * @author smriti on 20/05/20
 */
public class HospitalDeptDutyRosterOverrideQuery {

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'N'" +
                    " AND dd.hospitalDepartmentRoomInfo.id IS NULL" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'Y'" +
                    " AND dd.hospitalDepartmentRoomInfo.id IS NOT NULL" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.hospitalDepartmentRoomInfo.id =:hospitalDepartmentRoomInfoId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_UPDATE_OVERRIDE_STATUS =
            " UPDATE HospitalDepartmentDutyRosterOverride h" +
                    " SET h.status = 'N'" +
                    " WHERE h.hospitalDepartmentDutyRoster.id = :id";

    public static String QUERY_TO_UPDATE_OVERRIDE_ROOM(Long hospitalDepartmentRoomInfoId) {
        return " UPDATE HospitalDepartmentDutyRosterOverride h" +
                " SET h.hospitalDepartmentRoomInfo = " + hospitalDepartmentRoomInfoId +
                " WHERE h.hospitalDepartmentDutyRoster.id = :id";
    }

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM_EXCEPT_CURRENT_ID =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'N'" +
                    " AND dd.hospitalDepartmentRoomInfo.id IS NULL" +
                    " AND dd.id!=:id" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM_EXCEPT_CURRENT_ID =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'Y'" +
                    " AND dd.hospitalDepartmentRoomInfo.id IS NOT NULL" +
                    " AND dd.id!=:id" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.hospitalDepartmentRoomInfo.id =:hospitalDepartmentRoomInfoId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static String QUERY_TO_FETCH_HDD_ROSTER_OVERRIDE(
            List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> overrideUpdateRequestDTOS) {

        String overrideIds = overrideUpdateRequestDTOS.stream()
                .map(request -> request.getRosterOverrideId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        return " SELECT d FROM HospitalDepartmentDutyRosterOverride d" +
                " WHERE d.id IN (" + overrideIds + ")";
    }

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

    public static final String QUERY_TO_CHECK_IF_OVERRIDE_EXISTS =
            " SELECT" +
                    " dd.hasOverrideDutyRoster as hasOverrideDutyRoster" +             //[0]
                    " FROM HospitalDepartmentDutyRoster dd" +
                    " WHERE dd.status !='D'" +
                    " AND dd.id = :id";

}
