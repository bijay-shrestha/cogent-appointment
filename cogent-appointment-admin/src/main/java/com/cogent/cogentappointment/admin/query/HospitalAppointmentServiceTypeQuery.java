package com.cogent.cogentappointment.admin.query;

/**
 * @author smriti on 27/05/20
 */
public class HospitalAppointmentServiceTypeQuery {

    public static final String QUERY_TO_UPDATE_IS_PRIMARY_STATUS =
            " UPDATE HospitalAppointmentServiceType h " +
                    " SET h.isPrimary = 'Y'" +
                    " WHERE h.hospital.id =:hospitalId" +
                    " AND h.appointmentServiceType.id = :appointmentServiceTypeId" +
                    " AND h.status = 'Y'";

    public static final String QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE =
            " SELECT h.id as hospitalAppointmentServiceTypeId, " +                          //[0]
                    " h.appointmentServiceType.id as appointmentServiceTypeId," +           //[1]
                    " h.appointmentServiceType.name as appointmentServiceTypeName," +       //[2]
                    " h.isPrimary as isPrimary" +                                           //[3]
                    " FROM HospitalAppointmentServiceType h " +
                    " WHERE h.status = 'Y'" +
                    " AND h.hospital.id =:hospitalId";
}
