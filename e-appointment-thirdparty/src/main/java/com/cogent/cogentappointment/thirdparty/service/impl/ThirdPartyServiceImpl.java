package com.cogent.cogentappointment.thirdparty.service.impl;

import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import com.cogent.cogentappointment.thirdparty.repository.ThirdPartyRepository;
import com.cogent.cogentappointment.thirdparty.service.ThirdPartyService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository thirdPartyRepository;

    public ThirdPartyServiceImpl(ThirdPartyRepository thirdPartyRepository) {
        this.thirdPartyRepository = thirdPartyRepository;
    }

    @Override
    public ThirdPartyInfo saveThirdPartyData(ThirdPartyInfo thirdPartyInfo) {
        return thirdPartyRepository.save(thirdPartyInfo);
    }
}
