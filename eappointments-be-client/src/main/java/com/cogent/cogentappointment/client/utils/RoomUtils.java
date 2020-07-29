package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Room;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public class RoomUtils {
    public static Room parseToRoom(RoomRequestDTO requestDTO,
                                   Hospital hospital) {

        Room room = new Room();
        room.setRoomNumber(requestDTO.getRoomNumber());
        room.setStatus(requestDTO.getStatus());
        room.setHospital(hospital);
        return room;
    }

    public static Room parseToUpdatedRoom(RoomUpdateRequestDTO requestDTO,
                                          Room room,
                                          Hospital hospital) {

        room.setRoomNumber(requestDTO.getRoomNumber());
        room.setStatus(requestDTO.getStatus());
        room.setRemarks(requestDTO.getRemarks());
        room.setHospital(hospital);

        return room;
    }

    public static Room parseToDeletedRoom(Room room,
                                          DeleteRequestDTO deleteRequestDTO) {
        room.setStatus(deleteRequestDTO.getStatus());
        room.setRemarks(deleteRequestDTO.getRemarks());

        return room;
    }
}
