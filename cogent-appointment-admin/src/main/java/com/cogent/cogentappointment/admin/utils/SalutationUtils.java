package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import com.cogent.cogentappointment.persistence.model.Salutation;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.DELETED;

public class SalutationUtils {

    public static DoctorSalutation parseToDoctorSalutation(Doctor doctor, Salutation salutation) {

        DoctorSalutation doctorSalutation = new DoctorSalutation();
        doctorSalutation.setDoctorId(doctor.getId());
        doctorSalutation.setSalutationId(salutation.getId());
        doctorSalutation.setStatus(ACTIVE);
        return doctorSalutation;

    }

    public static Salutation parseToSalutaionToDeleted(Salutation salutation, DeleteRequestDTO deleteRequestDTO) {

        salutation.setStatus(deleteRequestDTO.getStatus());
        salutation.setRemarks(deleteRequestDTO.getRemarks());

        return salutation;
    }

    public static List<DoctorSalutation> parseDoctorSalutationToDeleted(List<DoctorSalutation> doctorSalutation) {

        doctorSalutation.forEach(salutation -> {

            salutation.setStatus(DELETED);
            salutation.setRemarks("Deleted...");


        });

        return doctorSalutation;
    }
}
