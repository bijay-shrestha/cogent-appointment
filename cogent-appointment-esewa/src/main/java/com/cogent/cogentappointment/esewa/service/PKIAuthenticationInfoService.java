package com.cogent.cogentappointment.esewa.service;

/**
 * @author smriti on 06/07/20
 */
public interface PKIAuthenticationInfoService {

    String findServerPrivateKeyByClientId(String clientId);

    String findClientPublicKeyByClientId(String clientId);
}
