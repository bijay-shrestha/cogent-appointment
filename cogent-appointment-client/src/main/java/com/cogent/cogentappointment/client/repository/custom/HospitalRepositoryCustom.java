package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalFollowUpResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTO;
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

    Integer fetchHospitalFollowUpIntervalDays(Long hospitalId);

    Integer fetchHospitalFollowUpCount(Long hospitalId);

    HospitalFollowUpResponseDTO fetchFollowUpDetails(Long hospitalId);
}
