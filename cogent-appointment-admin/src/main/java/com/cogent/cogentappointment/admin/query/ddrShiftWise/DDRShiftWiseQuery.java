package com.cogent.cogentappointment.admin.query.ddrShiftWise;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author smriti on 08/05/20
 */
public class DDRShiftWiseQuery {

    public static final String VALIDATE_DDR_SHIFT_WISE_COUNT =
            " SELECT COUNT(d.id)" +
                    " FROM DoctorDutyRosterShiftWise d" +
                    " WHERE d.status != 'D'" +
                    " AND d.doctor.id=:doctorId" +
                    " AND d.specialization.id= :specializationId" +
                    " AND d.toDate >=:fromDate" +
                    " AND d.fromDate <=:toDate";

    public static final String QUERY_TO_FETCH_EXISTING_DDR =
            " SELECT" +
                    " dd.id as ddrId," +                                             //[0]
                    " dd.fromDate as fromDate," +                                    //[1]
                    " dd.toDate as toDate," +                                        //[2]
                    " DATEDIFF(toDate, fromDate) + 1 as totalDays," +               //[3]
                    " ABS(DATEDIFF(toDate, CURDATE())) + 1 as remainingDays" +        //[4]
                    " FROM DoctorDutyRosterShiftWise dd" +
                    " WHERE dd.status != 'D'" +
                    " AND dd.doctor.id=:doctorId" +
                    " AND dd.specialization.id= :specializationId" +
                    " AND dd.toDate >=:fromDate" +
                    " AND dd.fromDate <=:toDate";

    public static String QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(DDRSearchRequestDTO searchRequestDTO) {

        String sql = " SELECT" +
                " ddr.id as ddrId," +                                                   //[0]
                " d.name as doctorName," +                                              //[1]
                " s.name as specializationName," +                                      //[2]
                " ddr.fromDate as fromDate," +                                          //[3]
                " ddr.toDate as toDate," +                                              //[4]
                " ddr.status as status," +                                              //[5]
                " h.name as hospitalName," +                                            //[6]
                " CASE WHEN" +
                " (da.status is null OR da.status = 'N')" +
                " THEN null" +
                " ELSE" +
                " da.fileUri" +
                " END as fileUri" +                                                     //[7]
                " FROM DoctorDutyRosterShiftWise ddr" +
                " LEFT JOIN Doctor d ON ddr.doctor.id = d.id" +
                " LEFT JOIN Specialization s ON ddr.specialization.id = s.id" +
                " LEFT JOIN Hospital h ON ddr.hospital.id = h.id" +
                " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                " WHERE" +
                " ddr.status !='D'" +
                " AND ddr.toDate >=:fromDate AND ddr.fromDate <=:toDate";

        if (!ObjectUtils.isEmpty(searchRequestDTO.getStatus()))
            sql += " AND ddr.status='" + searchRequestDTO.getStatus() + "'";

        if (!Objects.isNull(searchRequestDTO.getDoctorId()))
            sql += " AND d.id = " + searchRequestDTO.getDoctorId();

        if (!Objects.isNull(searchRequestDTO.getSpecializationId()))
            sql += " AND s.id = " + searchRequestDTO.getSpecializationId();

        if (!Objects.isNull(searchRequestDTO.getHospitalId()))
            sql += " AND h.id = " + searchRequestDTO.getHospitalId();

        return sql + " ORDER BY ddr.id DESC";
    }

    public static final String QUERY_TO_FETCH_DDR_SHIFT_WISE_DETAILS =
            " SELECT" +
                    " ddr.id as ddrId," +                                                //[0]
                    " h.name as hospitalName," +                                        //[1]
                    " s.name as specializationName," +                                  //[2]
                    " d.name as doctorName," +                                          //[3]
                    " ddr.fromDate as fromDate," +                                      //[4]
                    " ddr.toDate as toDate," +                                          //[5]
                    " ddr.status as status," +                                          //[6]
                    " ddr.remarks as remarks," +                                        //[7]
                    " ddr.hasOverride as hasOverride," +                                //[8]
                    " CASE WHEN" +
                    " (da.status is null OR da.status = 'N')" +
                    " THEN null" +
                    " ELSE" +
                    " da.fileUri" +
                    " END as fileUri," +                                                //[9]
                    DOCTOR_DUTY_ROSTERS_AUDITABLE_QUERY() +
                    " FROM DoctorDutyRosterShiftWise ddr" +
                    " LEFT JOIN Doctor d ON ddr.doctor.id = d.id" +
                    " LEFT JOIN DoctorAvatar da ON da.doctorId.id = d.id" +
                    " LEFT JOIN Specialization s ON ddr.specialization.id = s.id" +
                    " LEFT JOIN Hospital h ON ddr.hospital.id = h.id" +
                    " WHERE ddr.status !='D'" +
                    " AND ddr.id = :ddrId";

    private static String DOCTOR_DUTY_ROSTERS_AUDITABLE_QUERY() {
        return " ddr.createdBy as createdBy," +
                " ddr.createdDate as createdDate," +
                " ddr.lastModifiedBy as lastModifiedBy," +
                " ddr.lastModifiedDate as lastModifiedDate";
    }

}
