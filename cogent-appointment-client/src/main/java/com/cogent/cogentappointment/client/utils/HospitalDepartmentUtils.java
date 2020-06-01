package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public class HospitalDepartmentUtils {

    public static HospitalDepartment parseToHospitalDepartment(HospitalDepartmentRequestDTO requestDTO,
                                                               Hospital hospital) {

        HospitalDepartment hospitalDepartment = new HospitalDepartment();
        hospitalDepartment.setName(toNormalCase(requestDTO.getName()));
        hospitalDepartment.setCode(toUpperCase(requestDTO.getCode()));
        hospitalDepartment.setDescription(requestDTO.getDescription());
        hospitalDepartment.setStatus(requestDTO.getStatus());
        hospitalDepartment.setHospital(hospital);

        return hospitalDepartment;
    }


    public static HospitalDepartmentBillingModeInfo parseToHospitalDepartmentCharge(HospitalDepartmentRequestDTO requestDTO,
                                                                                    HospitalDepartment hospitalDepartment) {

        HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo = new HospitalDepartmentBillingModeInfo();
        hospitalDepartmentBillingModeInfo.setAppointmentCharge(requestDTO.getAppointmentCharge());
        hospitalDepartmentBillingModeInfo.setAppointmentFollowUpCharge(requestDTO.getFollowUpCharge());
        hospitalDepartmentBillingModeInfo.setStatus(requestDTO.getStatus());
        hospitalDepartmentBillingModeInfo.setHospitalDepartment(hospitalDepartment);

        return hospitalDepartmentBillingModeInfo;
    }


    public static HospitalDepartmentDoctorInfo parseToHospitalDepartmentDoctorInfo(HospitalDepartment hospitalDepartment,
                                                                                   Character status,
                                                                                   Doctor doctor) {
        HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo = new HospitalDepartmentDoctorInfo();
        hospitalDepartmentDoctorInfo.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentDoctorInfo.setDoctor(doctor);
        hospitalDepartmentDoctorInfo.setStatus(status);

        return hospitalDepartmentDoctorInfo;

    }

    public static HospitalDepartmentRoomInfo parseToHospitalDepartmentRoomInfo(HospitalDepartment hospitalDepartment,
                                                                               Character status,
                                                                               Room room) {
        HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo = new HospitalDepartmentRoomInfo();
        hospitalDepartmentRoomInfo.setHospitalDepartment(hospitalDepartment);
        hospitalDepartmentRoomInfo.setRoom(room);
        hospitalDepartmentRoomInfo.setStatus(status);

        return hospitalDepartmentRoomInfo;

    }

    public static HospitalDepartment parseToUpdateHospitalDepartment(HospitalDepartment hospitalDepartment,
                                                                     HospitalDepartmentUpdateRequestDTO requestDTO) {
        hospitalDepartment.setName(toNormalCase(requestDTO.getName()));
        hospitalDepartment.setCode(toUpperCase(requestDTO.getCode()));
        hospitalDepartment.setDescription(requestDTO.getDescription());
        hospitalDepartment.setStatus(requestDTO.getStatus());
        hospitalDepartment.setRemarks(requestDTO.getRemarks());

        return hospitalDepartment;
    }

    public static HospitalDepartmentBillingModeInfo parseToUpdateHospitalDepartmentCharge(HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo,
                                                                                          HospitalDepartmentUpdateRequestDTO requestDTO) {

        hospitalDepartmentBillingModeInfo.setAppointmentCharge(requestDTO.getAppointmentCharge());
        hospitalDepartmentBillingModeInfo.setAppointmentFollowUpCharge(requestDTO.getFollowUpCharge());
        hospitalDepartmentBillingModeInfo.setStatus(requestDTO.getStatus());
        hospitalDepartmentBillingModeInfo.setRemarks(requestDTO.getRemarks());

        return hospitalDepartmentBillingModeInfo;
    }

    public static List<Long> mergeExisitingAndNewListId(List<Long> existingIdList,
                                                        List<Long> newIdList) {


        return existingIdList.stream()
                .filter(oldId -> (newIdList.stream()
                        .filter(newId -> (newId == oldId))
                        .count()) < 1)
                .collect(Collectors.toList());

    }

    public static HospitalDepartment parseToDeleteHospitalDept(HospitalDepartment hospitalDepartment,
                                                               DeleteRequestDTO requestDTO) {
        hospitalDepartment.setStatus(requestDTO.getStatus());
        hospitalDepartment.setRemarks(requestDTO.getRemarks());

        return hospitalDepartment;
    }

    public static HospitalDepartmentBillingModeInfo parseToDeleteHospitalDeptCharge(HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo,
                                                                                    DeleteRequestDTO requestDTO) {
        hospitalDepartmentBillingModeInfo.setStatus(requestDTO.getStatus());
        hospitalDepartmentBillingModeInfo.setRemarks(requestDTO.getRemarks());

        return hospitalDepartmentBillingModeInfo;
    }


    public static List<HospitalDepartmentDoctorInfo> parseToDeleteHospitalDeptDoctorInfos(List<HospitalDepartmentDoctorInfo> doctorInfos,
                                                                                          DeleteRequestDTO requestDTO) {
        doctorInfos.forEach(doctorInfo -> {
            doctorInfo.setStatus(requestDTO.getStatus());
            doctorInfo.setRemarks(requestDTO.getRemarks());
        });

        return doctorInfos;
    }

    public static HospitalDepartmentDoctorInfo parseToDeleteHospitalDeptDoctorInfo(HospitalDepartmentDoctorInfo doctorInfo,
                                                                                   Character status,
                                                                                   String remarks) {

        doctorInfo.setStatus(status);
        doctorInfo.setRemarks(remarks);

        return doctorInfo;
    }

    public static List<HospitalDepartmentRoomInfo> parseToDeleteHospitalDeptRoomInfos(List<HospitalDepartmentRoomInfo> roomInfos,
                                                                                      DeleteRequestDTO requestDTO) {

        roomInfos.forEach(roomInfo -> {
            roomInfo.setStatus(requestDTO.getStatus());
            roomInfo.setRemarks(requestDTO.getRemarks());
        });

        return roomInfos;
    }

    public static HospitalDepartmentRoomInfo parseToDeleteHospitalDeptRoomInfo(HospitalDepartmentRoomInfo roomInfo,
                                                                               Character status,
                                                                               String remarks) {

        roomInfo.setStatus(status);
        roomInfo.setRemarks(remarks);

        return roomInfo;
    }


}
