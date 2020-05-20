package com.cogent.cogentappointment.client.query;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public class HospitalDepartmentQuery {

    public static final String QUERY_TO_VALIDATE_DUPLICITY =
            " SELECT hd.name," +
                    " hd.code" +
                    " FROM  HospitalDepartment hd" +
                    " WHERE" +
                    " hd.status !='D'" +
                    " AND hd.hospital.status !='D'" +
                    " AND (hd.name=:name OR hd.code=:code)" +
                    " AND hd.hospital.id =:hospitalId";

    public static final String QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE =
            " SELECT hd.name," +
                    " hd.code" +
                    " FROM  HospitalDepartment hd" +
                    " WHERE" +
                    " hd.status !='D'" +
                    " AND hd.hospital.status !='D'" +
                    " AND hd.id!=:id" +
                    " AND (hd.name=:name OR hd.code=:code)" +
                    " AND hd.hospital.id =:hospitalId";

    public static final String QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " hd.id as value," +
                    " hd.name as label" +
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status != 'D'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY hd.id DESC";

    public static final String QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN =
            "SELECT" +
                    " hd.id as value," +
                    " hd.name as label" +
                    " FROM HospitalDepartment hd" +
                    " WHERE hd.status = 'Y'" +
                    " AND hd.hospital.id= :hospitalId" +
                    " ORDER BY hd.id DESC";

    public static String DEPARTMENT_AUDITABLE_QUERY() {
        return " d.createdBy as createdBy," +
                " d.createdDate as createdDate," +
                " d.lastModifiedBy as lastModifiedBy," +
                " d.lastModifiedDate as lastModifiedDate";
    }
}
