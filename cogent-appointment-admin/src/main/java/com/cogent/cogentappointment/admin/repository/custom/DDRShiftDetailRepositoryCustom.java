package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRShiftMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Qualifier("ddrShiftDetailRepositoryCustom")
public interface DDRShiftDetailRepositoryCustom {

    List<DDRShiftMinResponseDTO> fetchExistingShift(Long ddrId);
}
