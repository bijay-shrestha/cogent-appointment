package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 28/05/20
 */
@Repository
@Qualifier("hospitalDepartmentRepositoryCustom")
public interface HospitalDepartmentRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveHospitalDepartment(Long hospitalId);

}
