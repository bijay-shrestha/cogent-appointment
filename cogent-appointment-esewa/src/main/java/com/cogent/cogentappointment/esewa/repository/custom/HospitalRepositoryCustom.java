package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Qualifier("hospitalRepositoryCustom")
public interface HospitalRepositoryCustom {

    List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO);

    Integer fetchHospitalFreeFollowUpIntervalDays(Long hospitalId);

    Integer fetchHospitalFollowUpCount(Long hospitalId);
}
