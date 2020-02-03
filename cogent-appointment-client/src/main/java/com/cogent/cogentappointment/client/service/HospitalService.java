package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.client.model.Hospital;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
public interface HospitalService {

    void save(HospitalRequestDTO requestDTO, MultipartFile multipartFile);

    void update(HospitalUpdateRequestDTO updateRequestDTO, MultipartFile multipartFile);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO,
                                            Pageable pageable);

    Hospital fetchActiveHospital(Long id);

    List<HospitalDropdownResponseDTO> fetchHospitalForDropDown();

    HospitalResponseDTO fetchDetailsById(Long hospitalId);

    List<HospitalMinResponseDTO> fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO);
}
