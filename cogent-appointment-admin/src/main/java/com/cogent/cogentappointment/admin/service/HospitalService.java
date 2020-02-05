package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.model.Hospital;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
public interface HospitalService {

    void save(HospitalRequestDTO requestDTO, MultipartFile logo, MultipartFile banner);

    void update(HospitalUpdateRequestDTO updateRequestDTO, MultipartFile logo, MultipartFile banner);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO hospitalSearchRequestDTO,
                                            Pageable pageable);

    Hospital fetchActiveHospital(Long id);

    List<HospitalDropdownResponseDTO> fetchHospitalForDropDown();

    HospitalResponseDTO fetchDetailsById(Long hospitalId);
}
