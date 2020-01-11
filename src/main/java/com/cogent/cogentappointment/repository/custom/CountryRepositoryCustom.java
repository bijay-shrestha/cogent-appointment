package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
@Repository
@Qualifier("countryRepositoryCustom")
public interface CountryRepositoryCustom {
    List<DropDownResponseDTO> fetchActiveCountry();

}
