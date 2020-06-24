package com.cogent.cogentappointment.admin.service;

/**
 * @author Sauravi Thapa ON 6/22/20
 */
public interface HmacService {

    String getHmacForFrontend(Long appointmentId);
}
