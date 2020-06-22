package com.cogent.cogentappointment.client.service;

/**
 * @author Sauravi Thapa ON 6/22/20
 */
public interface HmacService {

    String getHmacForFrontend(Long appointmentId);
}
