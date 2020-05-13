package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import com.cogent.cogentappointment.persistence.model.Salutation;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

public class SalutationUtils {

    public static DoctorSalutation parseToDoctorSalutation(Doctor doctor, Salutation salutation) {

        DoctorSalutation doctorSalutation = new DoctorSalutation();
        doctorSalutation.setDoctorId(doctor);
        doctorSalutation.setSalutationId(salutation);
        doctorSalutation.setStatus(ACTIVE);
        return doctorSalutation;

    }
}
