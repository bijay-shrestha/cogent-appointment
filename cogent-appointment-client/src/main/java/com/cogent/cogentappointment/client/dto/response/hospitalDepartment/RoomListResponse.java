package com.cogent.cogentappointment.client.dto.response.hospitalDepartment;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomListResponse implements Serializable {

    List<Integer> roomList;
}
