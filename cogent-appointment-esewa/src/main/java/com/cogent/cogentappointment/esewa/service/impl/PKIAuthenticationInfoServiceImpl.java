package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.PKIAuthenticationInfoRepository;
import com.cogent.cogentappointment.esewa.service.PKIAuthenticationInfoService;
import com.cogent.cogentappointment.persistence.model.PKIAuthenticationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

import static com.cogent.cogentappointment.commons.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.PKIAuthenticationInfoLog.*;

/**
 * @author smriti on 06/07/20
 */
@Service
@Transactional
@Slf4j
public class PKIAuthenticationInfoServiceImpl implements PKIAuthenticationInfoService {

    private final PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository;

    public PKIAuthenticationInfoServiceImpl(PKIAuthenticationInfoRepository pkiAuthenticationInfoRepository) {
        this.pkiAuthenticationInfoRepository = pkiAuthenticationInfoRepository;
    }

    @Override
    public String findServerPrivateKeyByClientId(String clientId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PKI_SERVER_PRIVATE_KEY);

        String serverPrivateKey = pkiAuthenticationInfoRepository.fetchServerPrivateKeyByClientId(clientId)
                .orElseThrow(() -> PKI_AUTHENTICATION_INFO_NOT_FOUND.apply(clientId));

        log.info(FETCHING_PROCESS_COMPLETED, PKI_SERVER_PRIVATE_KEY, getDifferenceBetweenTwoTime(startTime));

        return serverPrivateKey;
    }

    @Override
    public String findClientPublicKeyByClientId(String clientId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PKI_CLIENT_PUBLIC_KEY);

        String clientPublicKey = pkiAuthenticationInfoRepository.fetchClientPublicKeyByClientId(clientId)
                .orElseThrow(() -> PKI_AUTHENTICATION_INFO_NOT_FOUND.apply(clientId));

        log.info(FETCHING_PROCESS_COMPLETED, PKI_CLIENT_PUBLIC_KEY, getDifferenceBetweenTwoTime(startTime));

        return clientPublicKey;
    }

    private final Function<String, NoContentFoundException> PKI_AUTHENTICATION_INFO_NOT_FOUND = (clientId) -> {
        log.error(CONTENT_NOT_FOUND, PKI_AUTHENTICATION_INFO);
        throw new NoContentFoundException(PKIAuthenticationInfo.class, "clientId", clientId);
    };
}
