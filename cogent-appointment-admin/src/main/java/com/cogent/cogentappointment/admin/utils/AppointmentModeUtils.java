package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class AppointmentModeUtils {

    public static AppointmentMode parseToAppointmentMode(AppointmentModeRequestDTO requestDTO) {

        AppointmentMode appointmentMode = new AppointmentMode();
        appointmentMode.setName(requestDTO.getName());
        appointmentMode.setCode(requestDTO.getCode());
        appointmentMode.setStatus(requestDTO.getStatus());
        appointmentMode.setDescription(requestDTO.getDescription());
        appointmentMode.setIsEditable(requestDTO.getIsEditable());
        return appointmentMode;
    }

    public static void parseToUpdatedAppointmentMode(AppointmentModeUpdateRequestDTO requestDTO,
                                                AppointmentMode appointmentMode) {
        appointmentMode.setName(convertToNormalCase(requestDTO.getName()));
        appointmentMode.setCode(requestDTO.getCode());
        appointmentMode.setStatus(requestDTO.getStatus());
        appointmentMode.setRemarks(requestDTO.getRemarks());
        appointmentMode.setDescription(requestDTO.getDescription());
    }

    public static void parseToDeletedAppointmentMode(AppointmentMode appointmentMode,
                                                DeleteRequestDTO deleteRequestDTO) {
        appointmentMode.setStatus(deleteRequestDTO.getStatus());
        appointmentMode.setRemarks(deleteRequestDTO.getRemarks());
    }
}
