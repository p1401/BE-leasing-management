package com.fu.lhm.room.repository;

import com.fu.lhm.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHouse_Id(Long houseId);

    boolean existsByNameAndFloorAndHouse_Id(String roomName, int floor, Long houseId);
}
