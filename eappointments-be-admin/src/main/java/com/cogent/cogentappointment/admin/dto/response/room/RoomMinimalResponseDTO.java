package com.cogent.cogentappointment.admin.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-09-25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomMinimalResponseDTO implements Serializable {

    private List<RoomMinimalResponse> response;

    private int totalItems;
}
