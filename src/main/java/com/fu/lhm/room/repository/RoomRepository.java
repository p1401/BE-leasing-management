package com.fu.lhm.room.repository;

import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findAllByHouse_IdAndFloorAndNameContainingIgnoreCase(Long houseId, int floor,String roomName, Pageable pageable);

    List<Room> findAllByHouse_Id(Long id);

    Integer countByHouse_Id(long houseId);

    boolean existsByNameAndFloorAndHouse_Id(String roomName, int floor, Long houseId);


    Room findAllById(Long roomId);
}
