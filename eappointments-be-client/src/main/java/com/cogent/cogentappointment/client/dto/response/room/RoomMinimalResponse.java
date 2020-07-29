package com.cogent.cogentappointment.client.dto.response.room;

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
public class RoomMinimalResponse implements Serializable {
    private Long id;

    private String roomNumber;

    private Character status;
}
