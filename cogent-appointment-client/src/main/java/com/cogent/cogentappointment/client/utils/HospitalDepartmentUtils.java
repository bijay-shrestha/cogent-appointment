package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public class HospitalDepartmentUtils {

    public static HospitalDepartment parseToHospitalDepartment(HospitalDepartmentRequestDTO requestDTO,
                                                               Hospital hospital){

        HospitalDepartment hospitalDepartment=new HospitalDepartment();
        hospitalDepartment.setName(toNormalCase(requestDTO.getName()));
        hospitalDepartment.setCode(toUpperCase(requestDTO.getCode()));
        hospitalDepartment.setDescription(requestDTO.getDescription());
        hospitalDepartment.setStatus(requestDTO.getStatus());
        hospitalDepartment.setHospital(hospital);

        return hospitalDepartment;
    }

    public static HospitalDepartmentCharge parseToHospitalDepartmentCharge(HospitalDepartmentRequestDTO requestDTO,
                                                                           HospitalDepartment hospitalDepartment){

        HospitalDepartmentCharge hospitalDepartmentCharge=new HospitalDepartmentCharge();
        hospitalDepartmentCharge.setAppointmentCharge(requestDTO.getAppointmentCharge());
        hospitalDepartmentCharge.setAppointmentFollowUpCharge(requestDTO.getFollowUpCharge());
        hospitalDepartmentCharge.setStatus(requestDTO.getStatus());
        hospitalDepartmentCharge.setHospitalDepartment(hospitalDepartment);

        return hospitalDepartmentCharge;
    }

    public static HospitalDepartmentDoctorInfo parseToHospitalDepartmentDoctorInfo(HospitalDepartment hospitalDepartment,
                                                                                   HospitalDepartmentRequestDTO requestDTO,
                                                                                   Doctor doctor){
        HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo=new HospitalDepartmentDoctorInfo();
        hospitalDepartmentDoctorInfo.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentDoctorInfo.setDoctor(doctor);
        hospitalDepartmentDoctorInfo.setStatus(requestDTO.getStatus());

        return hospitalDepartmentDoctorInfo;

    }

    public static HospitalDepartmentRoomInfo parseToHospitalDepartmentRoomInfo(HospitalDepartment hospitalDepartment,
                                                                                   HospitalDepartmentRequestDTO requestDTO,
                                                                                   Room room){
        HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo=new HospitalDepartmentRoomInfo();
        hospitalDepartmentRoomInfo.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentRoomInfo.setRoom(room);
        hospitalDepartmentRoomInfo.setStatus(requestDTO.getStatus());

        return hospitalDepartmentRoomInfo;

    }


}
