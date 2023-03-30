package com.example.demo.room.Service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.house.House;
import com.example.demo.house.Repository.HouseRepository;
import com.example.demo.room.Repository.Roomrepository;
import com.example.demo.room.Room;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    Roomrepository roomrepository;

    HouseRepository houseRepository;
    public List<Room> getListRoom(Long houseId){
        return roomrepository.findByHouseId(houseId);
    }

    public Room createroom(Long houseId, Room room) {


        houseRepository.findById(houseId).map(house->{
            room.setHouseId(house);
            return roomrepository.save(room);
        }).orElseThrow(() -> new BadRequestException("Not found house with id"+houseId));

        return null;

    }


    public Room updateRoom(Long roomId, Room updateRoom) {
        Room oldRoom = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        oldRoom.setFloor(updateRoom.getFloor());
        oldRoom.setArea(updateRoom.getArea());
        oldRoom.setBedroomNumber(updateRoom.getBedroomNumber());
        oldRoom.setName(updateRoom.getName());
        oldRoom.setElectricNumber(updateRoom.getElectricNumber());
        oldRoom.setWaterNumber(updateRoom.getWaterNumber());
        return roomrepository.save(oldRoom);
    }




    public void deleteRoom(Long roomId) {

        roomrepository.deleteById(roomId);
    }

}
