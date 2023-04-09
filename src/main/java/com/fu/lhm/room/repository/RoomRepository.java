package com.fu.lhm.room.repository;

import com.fu.lhm.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findAllByHouse_IdAndFloor(Long houseId, int floor, Pageable pageable);

    List<Room> findAllByHouse_Id(Long id);
    boolean existsByNameAndFloorAndHouse_Id(String roomName, int floor, Long houseId);


}
