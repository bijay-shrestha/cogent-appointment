package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;

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
        appointmentMode.setName(toNormalCase(requestDTO.getName()));
        appointmentMode.setCode(requestDTO.getCode());
        appointmentMode.setStatus(requestDTO.getStatus());
        appointmentMode.setRemarks(requestDTO.getRemarks());
        appointmentMode.setDescription(requestDTO.getDescription());
        appointmentMode.setIsEditable(requestDTO.getIsEditable());
    }

    public static void parseToDeletedAppointmentMode(AppointmentMode appointmentMode,
                                                DeleteRequestDTO deleteRequestDTO) {
        appointmentMode.setStatus(deleteRequestDTO.getStatus());
        appointmentMode.setRemarks(deleteRequestDTO.getRemarks());
    }
}
