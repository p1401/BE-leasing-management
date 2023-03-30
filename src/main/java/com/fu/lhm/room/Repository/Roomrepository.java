package com.fu.lhm.room.Repository;

import com.fu.lhm.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Roomrepository extends JpaRepository<Room, Long> {

    List<Room> findByHouseId(Long houseId);




}
