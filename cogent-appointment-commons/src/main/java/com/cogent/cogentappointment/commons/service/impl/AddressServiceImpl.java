package com.cogent.cogentappointment.commons.service.impl;

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
