package com.cogent.cogentappointment.esewa.query;

/**
 * @author Sauravi Thapa ON 5/25/20
 */
public class AppointmentRefundDetailQuery {

    public static String QUERY_TO_GET_APPOINTMENT_REFUND_DETAILS=
            "SELECT" +
                    " ard" +
                    " FROM" +
                    " AppointmentRefundDetail ard" +
                    " LEFT JOIN Appointment a ON a.id=ard.appointmentId.id " +
                    " LEFT JOIN AppointmentTransactionDetail atd ON atd.appointment.id=a.id " +
                    " WHERE atd.transactionNumber=:transactionNumber" +
                    " AND a.hospitalId.esewaMerchantCode =:esewaMerchantCode" +
                    " AND a.patientId.eSewaId =:esewaId";

}
