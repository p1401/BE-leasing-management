package com.fu.lhm.room.repository;

import com.fu.lhm.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findAllByHouse_IdAndFloor(Long houseId, int floor, Pageable pageable);

    List<Room> findAllByHouse_Id(Long id);
    boolean existsByNameAndFloorAndHouse_Id(String roomName, int floor, Long houseId);


}
