package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import com.cogent.cogentappointment.persistence.model.Salutation;

import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.DELETED;

public class SalutationUtils {

    public static DoctorSalutation parseToDoctorSalutation(Doctor doctor, Salutation salutation) {

        DoctorSalutation doctorSalutation = new DoctorSalutation();
        doctorSalutation.setDoctorId(doctor.getId());
        doctorSalutation.setSalutationId(salutation.getId());
        doctorSalutation.setStatus(ACTIVE);
        return doctorSalutation;

    }

    public static List<DoctorSalutation> parseToDoctorSalutation(Long doctorId, List<Long> salutationIds) {

        List<DoctorSalutation> doctorSalutationList = new ArrayList<>();

        salutationIds.forEach(id -> {

            DoctorSalutation doctorSalutation = new DoctorSalutation();
            doctorSalutation.setSalutationId(id);
            doctorSalutation.setDoctorId(doctorId);
            doctorSalutation.setStatus(ACTIVE);

            doctorSalutationList.add(doctorSalutation);
        });

        return doctorSalutationList;
    }

    public static Salutation parseToSalutationToDeleted(Salutation salutation, DeleteRequestDTO deleteRequestDTO) {

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
