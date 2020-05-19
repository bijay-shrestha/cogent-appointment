package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.RoomRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
@Repository
public interface RoomRepository extends JpaRepository<Room,Long>,RoomRepositoryCustom{

    @Query("SELECT r FROM Room r WHERE r.status!='D' AND r.id = :id")
    Optional<Room> fetchRoomById(@Param("id") Long id);
}
