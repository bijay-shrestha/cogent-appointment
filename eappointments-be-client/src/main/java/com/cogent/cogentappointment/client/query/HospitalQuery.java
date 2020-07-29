package com.cogent.cogentappointment.client.query;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalQuery {

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS =
            " SELECT h.followUpIntervalDays as followUpIntervalDays" +
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId AND h.isCompany = 'N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT =
            " SELECT h.numberOfFollowUps as numberOfFollowUps" +
                    " FROM Hospital h" +
                    " WHERE" +
                    " h.id =:hospitalId AND h.isCompany = 'N'";

    public static final String QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_DETAILS =
            " SELECT h.numberOfFollowUps as numberOfFollowUps," +                   //[0]
                    " h.followUpIntervalDays as followUpIntervalDays" +             //[1]
                    " FROM Hospital h" +
                    " WHERE h.id =:hospitalId" +
                    " AND h.status = 'Y' AND h.isCompany = 'N'";

    public static String QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE =
            " SELECT " +
                    " ast.name as name," +                          //[0]
                    " ast.code as code," +                          //[1]
                    " has.isPrimary as isPrimary"+                  //[2]
                    " FROM Hospital h " +
                    " LEFT JOIN HospitalAppointmentServiceType has ON h.id = has.hospital.id" +
                    " LEFT JOIN AppointmentServiceType ast ON ast.id = has.appointmentServiceType.id" +
                    " WHERE" +
                    " has.status = 'Y'" +
                    " AND h.id =:hospitalId";

}
