package com.cogent.cogentappointment.client.query;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author Sauravi Thapa ON 5/18/20
 */
public class HospitalDeptDutyRosterQuery {

    public static final String VALIDATE_DEPT_DUTY_ROSTER_COUNT =
            " SELECT COUNT(sdr.id)" +
                    " FROM HospitalDepartmentDutyRoster sdr" +
                    " WHERE sdr.status != 'D'" +
                    " AND sdr.specialization.id= :specializationId" +
                    " AND sdr.toDate >=:fromDate" +
                    " AND sdr.fromDate <=:toDate";

    public static String QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT_DUTY_ROSTER(
            HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO) {

        String sql = " SELECT" +
                " dr.id as id," +                                                      //[0]
                " hd.name as hospitalDeptName," +                                      //[1]
                " dr.rosterGapDuration as rosterGapDuration," +                        //[2]
                " dr.fromDate as fromDate," +                                          //[3]
                " dr.toDate as toDate," +                                              //[4]
                " dr.status as status," +                                              //[5]
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

        return sql + " ORDER BY ddr.id DESC";
    }


}
