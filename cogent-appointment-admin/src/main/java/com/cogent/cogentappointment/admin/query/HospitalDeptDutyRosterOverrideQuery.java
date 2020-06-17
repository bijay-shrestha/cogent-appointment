package com.cogent.cogentappointment.admin.query;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;

import java.util.List;
import java.util.Objects;
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

    public static String QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_STATUS(
            HospitalDeptAppointmentStatusRequestDTO requestDTO,List<Long> rosterIdList) {

        String SQL =
                "SELECT" +
                        " hddro.fromDate as fromDate," +                                                             //[0]
                        " hddro.toDate as toDate," +                                                                 //[1]
                        " DATE_FORMAT(hddro.startTime, '%H:%i') as startTime," +                                     //[2]
                        " DATE_FORMAT(hddro.endTime, '%H:%i') as endTime," +                                         //[3]
                        " hddro.dayOffStatus as dayOffStatus ," +                                                    //[4]
                        " hddr.rosterGapDuration as gapDuration," +                                                  //[5]
                        " hddr.hospitalDepartment.id as hospitalDepartmentId," +                                     //[6]
                        " hddr.hospitalDepartment.name as hospitatDepartmentName," +                                //[7]
                        " CASE WHEN hddro.hospitalDepartmentRoomInfo.id Is NULL " +                                  //[8]
                        " THEN null" +
                        " ELSE hddro.hospitalDepartmentRoomInfo.id END as roomId," +
                        " CASE WHEN hddro.hospitalDepartmentRoomInfo.id Is NULL " +                                  //[9]
                        " THEN 'N/A'" +
                        " ELSE hddro.hospitalDepartmentRoomInfo.room.roomNumber END as roomNumber," +
                        " hddr.id as hospitalDepartmentDutyRosterId" +
                        " FROM HospitalDepartmentDutyRosterOverride hddro" +
                        " LEFT JOIN HospitalDepartmentDutyRoster hddr ON hddr.id = hddro .hospitalDepartmentDutyRoster.id" +
                        " WHERE" +
                        " hddro.status = 'Y'" +
                        " AND hddr.status = 'Y'" +
                        " AND hddro.toDate >=:fromDate" +
                        " AND hddro.fromDate <=:toDate";

        if (rosterIdList.size() > 0)
            SQL += " AND hddr.id IN (:hospitalDepartmentDutyRosterId) ";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            SQL += " AND hddr.hospitalDepartment.id = :hospitalDepartmentId";

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            SQL += " AND hddro.hospitalDepartmentRoomInfo.id = :hospitalDepartmentRoomInfoId";

        if (!Objects.isNull(requestDTO.getHospitalId()))
            SQL += " AND hddr.hospital.id = :hospitalId";

        return SQL;
    }

}
