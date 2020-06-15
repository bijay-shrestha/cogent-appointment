package com.cogent.cogentappointment.commons.service.impl;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.commons.repository.custom.AddressRepositoryCustom;
import com.cogent.cogentappointment.commons.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sauravi Thapa ON 6/15/20
 */

@Service
@Slf4j
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService{

    private final AddressRepositoryCustom addressRepositoryCustom;

    public AddressServiceImpl(AddressRepositoryCustom addressRepositoryCustom) {
        this.addressRepositoryCustom = addressRepositoryCustom;
    }


    @Override
    public List<DropDownResponseDTO> fetchZoneDropDown() {
        List<DropDownResponseDTO> responseDTOS=addressRepositoryCustom.getListOfZone();

        return responseDTOS;
    }
}
