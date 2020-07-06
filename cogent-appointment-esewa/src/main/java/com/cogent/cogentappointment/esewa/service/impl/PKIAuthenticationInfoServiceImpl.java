package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.repository.PKIAuthenticationInfoRepository;
import com.cogent.cogentappointment.esewa.service.PKIAuthenticationInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public String findValueByAccessKey(String accessKey) {
        return null;
    }
}
