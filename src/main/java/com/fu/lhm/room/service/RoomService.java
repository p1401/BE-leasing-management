package com.fu.lhm.room.service;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.house.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.WaterElectric;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.modal.SendListRoomAndInforRequest;
import com.fu.lhm.room.repository.WaterElectricRepositoy;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomrepository;

    private final HouseRepository houseRepository;

    private final WaterElectricRepositoy waterElectricRepositoy;

    public Page<Room> getListRoomByHouseIdAndFloor(Long houseid, int floor, Pageable page){

        return roomrepository.findAllByHouse_IdAndFloor(houseid, floor, page);


    }

    public Room createroom(Long houseId, Room room) {

        House house = houseRepository.findById(houseId).orElseThrow(() -> new EntityNotFoundException("Nha không tồn tại!"));
        Room newRoom = new Room();
        newRoom.setName(room.getName());
        newRoom.setArea(room.getArea());
        newRoom.setRoomMoney(room.getRoomMoney());
        newRoom.setMaxTenant(room.getMaxTenant());
        newRoom.setHouse(house);
        newRoom.setFloor(room.getFloor());

        WaterElectric waterElectric = new WaterElectric();
        waterElectric.setChiSoDauDien(0);
        waterElectric.setChiSoDauNuoc(0);
        waterElectric.setChiSoCuoiDien(0);
        waterElectric.setChiSoCuoiNuoc(0);
        waterElectric.setRoom(roomrepository.save(newRoom));
        waterElectricRepositoy.save(waterElectric);

        return newRoom;

    }


    public Room updateRoom(Long roomId, Room updateRoom) {

        Room oldRoom = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        oldRoom.setFloor(updateRoom.getFloor());
        oldRoom.setArea(updateRoom.getArea());
        oldRoom.setRoomMoney(updateRoom.getRoomMoney());
        oldRoom.setMaxTenant(updateRoom.getMaxTenant());
        oldRoom.setName(updateRoom.getName());

        return roomrepository.save(oldRoom);
    }

    public Room getRoomById(Long roomId){

        return roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
    }




    public void deleteRoom(Long roomId) {

        roomrepository.deleteById(roomId);
    }

}
