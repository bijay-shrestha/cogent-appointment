package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorSalutationResponseDTO;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
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

    List<DoctorSalutation> validateDoctorSalutationCount(String ids);
}
