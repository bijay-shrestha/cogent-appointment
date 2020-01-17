package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.patient.PatientRequestDTO;
import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.enums.Title;
import com.cogent.cogentappointment.model.Hospital;
import com.cogent.cogentappointment.model.Nationality;
import com.cogent.cogentappointment.model.Patient;

import java.util.function.BiFunction;

import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 16/01/2020
 */
public class PatientUtils {

    public static Patient parseToPatient(PatientRequestDTO requestDTO,
                                         Nationality nationality,
                                         Gender gender,
                                         Hospital hospital,
                                         Title title) {
        Patient patient = new Patient();
        patient.setName(toUpperCase(requestDTO.getName()));
        patient.setMobileNumber(requestDTO.getMobileNumber());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setEmail(requestDTO.getEmail());
        patient.setIsSelf(requestDTO.getIsSelf());
        patient.setIsRegistered(requestDTO.getIsRegistered());
        patient.setEsewaId(requestDTO.getEsewaId());
        patient.setAddress(requestDTO.getAddress());
        patient.setGender(gender);
        patient.setNationality(nationality);
        patient.setHospitalId(hospital);
        patient.setTitle(title);
        patient.setStatus(requestDTO.getStatus());
        return patient;
    }


    public static BiFunction<Patient, DeleteRequestDTO, Patient> deletePatient = (patientToUpdate
            , deleteRequestDTO) -> {
        patientToUpdate.setStatus(deleteRequestDTO.getStatus());
        patientToUpdate.setRemarks(deleteRequestDTO.getRemarks());

        return patientToUpdate;
    };



//    public static BiFunction<Patient, PatientUpdateRequestDTO, Patient> updatePatient = (patientToUpdate, updateRequestDTO) -> {
//        patientToUpdate.setFirstName(toUpperCase(updateRequestDTO.getFirstName()));
//        patientToUpdate.setMiddleName(toUpperCase(updateRequestDTO.getMiddleName()));
//        patientToUpdate.setCode(toUpperCase(updateRequestDTO.getCode()));
//        patientToUpdate.setStatus(updateRequestDTO.getStatus());
//        patientToUpdate.setGender(updateRequestDTO.getGender());
//        patientToUpdate.setMobileNumber(updateRequestDTO.getMobileNumber());
//        patientToUpdate.setEmergencyContact(updateRequestDTO.getEmergencyContact());
//        patientToUpdate.setPassportNumber(updateRequestDTO.getPassportNumber());
//        patientToUpdate.setCitizenshipNumber(updateRequestDTO.getCitizenshipNumber());
//        patientToUpdate.setPan(updateRequestDTO.getPan());
//        patientToUpdate.setBloodGroup(updateRequestDTO.getBloodGroup());
//        patientToUpdate.setEducation(updateRequestDTO.getEducation());
//        patientToUpdate.setRemarks(updateRequestDTO.getRemarks());
//        patientToUpdate.setDateOfBirth(updateRequestDTO.getDateOfBirth());
//        patientToUpdate.setAddress(updateRequestDTO.getAddress());
//        patientToUpdate.setCountry(updateRequestDTO.getCountry());
//        patientToUpdate.setCity(updateRequestDTO.getCity());
//        patientToUpdate.setEmail(updateRequestDTO.getEmail());
//        patientToUpdate.setReferredBy(updateRequestDTO.getReferredBy());
//
//        return patientToUpdate;
//    };


}
