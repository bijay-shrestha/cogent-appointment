package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 6/15/20
 */

@Repository
@Qualifier("addressRepositoryCustom")
public interface AddressRepositoryCustom {

    List<DropDownResponseDTO> getListOfZone();

    List<DropDownResponseDTO> getListOfProvince();
}
