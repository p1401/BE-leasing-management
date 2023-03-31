package com.fu.lhm.room.Service;

import com.fu.lhm.room.Room;
import com.fu.lhm.house.House;
import com.fu.lhm.house.Repository.HouseRepository;
import com.fu.lhm.room.Repository.Roomrepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final Roomrepository roomrepository;

    private final HouseRepository houseRepository;

    public List<Room> getListRoom(Long houseid){

        return roomrepository.findAllByHouse_Id(houseid);
    }

    public Room createroom(Long houseId, Room room) {

        House house = houseRepository.findById(houseId).orElseThrow(() -> new EntityNotFoundException("Nha không tồn tại!"));
        room.setHouse(house);

        return roomrepository.save(room);

    }


    public Room updateRoom(Long roomId, Room updateRoom) {
        Room oldRoom = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        oldRoom.setFloor(updateRoom.getFloor());
        oldRoom.setArea(updateRoom.getArea());
        oldRoom.setBedroomNumber(updateRoom.getBedroomNumber());
        oldRoom.setName(updateRoom.getName());
        oldRoom.setElectricNumber(updateRoom.getElectricNumber());
        oldRoom.setWaterNumber(updateRoom.getWaterNumber());
        House house = houseRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Nha không tồn tại!"));
        return roomrepository.save(oldRoom);
    }




    public void deleteRoom(Long roomId) {

        roomrepository.deleteById(roomId);
    }

}
