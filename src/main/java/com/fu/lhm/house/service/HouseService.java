package com.fu.lhm.house.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.house.validate.HouseValidate;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {
    private final HouseRepository houseRepository;
    private final RoomRepository roomRepository;
    private final HouseValidate houseValidate;
    public House createHouse(House house, User user) throws BadRequestException {
//        houseValidate.validateCreateHouse(house,user);
        house.setRoomNumber(0);
        house.setEmptyRoom(0);
        return houseRepository.save(house);
    }

    public House updateHouse(Long houseId, House updateHouse) throws BadRequestException {
        House oldHouse = houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nhà không tồn tại!"));
        oldHouse.setName(updateHouse.getName());
        oldHouse.setCity(updateHouse.getCity());
        oldHouse.setDistrict(updateHouse.getDistrict());
        oldHouse.setAddress(updateHouse.getAddress());
        oldHouse.setFloor(updateHouse.getFloor());
        oldHouse.setElectricPrice(updateHouse.getElectricPrice());
        oldHouse.setWaterPrice(updateHouse.getWaterPrice());

        return houseRepository.save(oldHouse);
    }

    public Page<House> getListHouse(User user,String houseName, Pageable pageable) {

        Page<House> listHouse = houseRepository.findByUserAndNameContaining(user, houseName, pageable);

        for(House house : listHouse){
            int emptyRoom = 0;
            int roomNumber = 0;
            List<Room> listRoom = roomRepository.findAllByHouse_Id(house.getId());
            for(Room room : listRoom){
                if(room.getCurrentTenant()==0){
                    emptyRoom=emptyRoom+1;
                }
                roomNumber=roomNumber+1;
            }
            house.setEmptyRoom(emptyRoom);
            house.setRoomNumber(roomNumber);

        }

        return listHouse;
    }

    public House getHouseById(Long houseId) throws BadRequestException {

        return houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nhà không tồn tại!"));
    }
    public void deleteHouse(Long houseId) {

        houseRepository.deleteById(houseId);
    }

}
