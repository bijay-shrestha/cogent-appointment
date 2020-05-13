package com.cogent.cogentappointment.client.query;

public class SalutationQuery {

    public static final String QUERY_TO_FETCH_ACTIVE_SALUTATION_FOR_DROPDOWN =
            " SELECT" +
                    " sa.id as value," +
                    " sa.code as label" +
                    " FROM" +
                    " Salutation sa " +
                    " WHERE sa.status='Y'";

    public static String QUERY_TO_VALIDATE_SALUTATION_COUNT(String ids) {

        return " SELECT " +
                " sal " +
                " FROM Salutation sal" +
                " WHERE sal.status ='Y'" +
                " AND sal.id IN (" + ids + ")";

    }

    public static final String QUERY_TO_FETCH_DOCTOR_SALUTATION_BY_DOCTOR_ID(Long id) {

        return " SELECT" +
                " ds.id as doctorSalutationId," +
                " ds.salutationId as salutationId," +
                " s.code as salutationName" +
                " FROM DoctorSalutation ds" +
                " LEFT JOIN Salutation s ON s.id=ds.salutationId" +
                " LEFT JOIN Doctor d ON d.id=ds.doctorId" +
                " WHERE ds.status ='Y'" +
                " AND d.id=" + id;
    }

}
