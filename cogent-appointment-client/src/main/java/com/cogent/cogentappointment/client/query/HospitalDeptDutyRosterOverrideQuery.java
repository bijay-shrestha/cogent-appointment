package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;

import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;

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
                    " AND dd.room.id IS NULL" +
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
                    " AND dd.room.id IS NOT NULL" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.room.id =:roomId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static final String QUERY_TO_UPDATE_OVERRIDE_STATUS =
            " UPDATE HospitalDepartmentDutyRosterOverride h" +
                    " SET h.status = 'N'" +
                    " WHERE h.hospitalDepartmentDutyRoster.id = :id";

    public static final String QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM_EXCEPT_CURRENT_ID =
            " SELECT COUNT(dd.id)" +
                    " FROM HospitalDepartmentDutyRosterOverride dd" +
                    " LEFT JOIN HospitalDepartmentDutyRoster dr ON dr.id = dd.hospitalDepartmentDutyRoster.id" +
                    " WHERE dr.status != 'D'" +
                    " AND dd.status = 'Y'" +
                    " AND dr.isRoomEnabled = 'N'" +
                    " AND dd.room.id IS NULL" +
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
                    " AND dd.room.id IS NOT NULL" +
                    " AND dd.id!=:id" +
                    " AND dr.hospitalDepartment.id= :hospitalDepartmentId" +
                    " AND dd.room.id =:roomId" +
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
}
