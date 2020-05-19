package com.cogent.cogentappointment.admin.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomMinimalResponseDTO implements Serializable {
    private Long id;

    private Integer roomNumber;

    private Character status;

    private String hospitalName;

    private int totalItems;
}
