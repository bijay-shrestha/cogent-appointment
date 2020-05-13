package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorSalutationResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rupak on 2020-05-13
 */
@Repository
@Qualifier("doctorSalutationRepositoryCustom")
public interface DoctorSalutationRepositoryCustom {
    List<DoctorSalutationResponseDTO> fetchDoctorSalutationByDoctorId(Long id);
}
