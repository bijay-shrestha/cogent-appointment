package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Qualifier("specializationRepositoryCustom")
public interface SpecializationRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId);
}
