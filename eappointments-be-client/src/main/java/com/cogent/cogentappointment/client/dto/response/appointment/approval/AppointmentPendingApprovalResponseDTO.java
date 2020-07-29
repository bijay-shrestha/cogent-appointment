package com.cogent.cogentappointment.client.dto.response.appointment.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rupak
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPendingApprovalResponseDTO implements Serializable {
    private List<AppointmentPendingApprovalDTO> pendingAppointmentApprovals;

    private int totalItems;
}
