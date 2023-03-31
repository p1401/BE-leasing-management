package com.fu.lhm.room.Repository;

import com.fu.lhm.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface Roomrepository extends JpaRepository<Room, Long> {
    List<Room> findAllByHouse_Id(Long houseId);
}
