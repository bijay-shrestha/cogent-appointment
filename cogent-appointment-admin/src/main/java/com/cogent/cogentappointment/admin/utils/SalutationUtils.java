package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.DoctorSalutation;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

public class SalutationUtils {

    public static DoctorSalutation parseToDoctorSalutation(Long doctorId, Long salutationId) {

        DoctorSalutation doctorSalutation = new DoctorSalutation();
        doctorSalutation.setDoctorId(doctorId);
        doctorSalutation.setSalutationId(salutationId);
        doctorSalutation.setStatus(ACTIVE);
        return doctorSalutation;

    }
}
