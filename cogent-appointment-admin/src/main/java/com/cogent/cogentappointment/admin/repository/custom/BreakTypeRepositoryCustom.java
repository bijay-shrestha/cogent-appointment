package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 06/05/20
 */
@Repository
@Qualifier("breakTypeRepositoryCustom")
public interface BreakTypeRepositoryCustom {

    List<DropDownResponseDTO> fetchBreakTypeByHospitalId(Long hospitalId);
}
