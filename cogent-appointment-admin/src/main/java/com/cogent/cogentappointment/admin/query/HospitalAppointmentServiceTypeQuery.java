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
}
