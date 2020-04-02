package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.persistence.model.Specialization;

/**
 * @author smriti on 2019-08-11
 */
public interface SpecializationService {
    Specialization fetchActiveSpecializationByIdAndHospitalId(Long specializationId, Long hospitalId);
}
