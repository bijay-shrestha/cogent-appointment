package com.cogent.cogentappointment.dto.response.hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Character status;

    private String address;

    private String panNumber;

    private String fileUri;

    private String remarks;

    private List<HospitalContactNumberResponseDTO> contactNumberResponseDTOS;
}
