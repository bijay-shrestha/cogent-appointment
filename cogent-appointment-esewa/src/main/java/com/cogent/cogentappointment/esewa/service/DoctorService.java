package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctor.DoctorSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctor.DoctorUpdateRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
public interface DoctorService {

    Doctor fetchActiveDoctorByIdAndHospitalId(Long id, Long hospitalId);
}
