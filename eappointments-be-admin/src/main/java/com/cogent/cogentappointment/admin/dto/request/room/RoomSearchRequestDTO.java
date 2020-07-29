package com.cogent.cogentappointment.admin.dto.request.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchRequestDTO implements Serializable {

    private Long id;

    private Character status;

    private Long hospitalId;
}
