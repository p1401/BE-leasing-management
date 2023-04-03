package com.fu.lhm.room.service;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.house.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.modal.SendListRoomAndInforRequest;
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

    private final TenantRepository tenantRepository;
    private final ContractRepository contractRepository;

    private final BillRepository billRepository;

    public Page<Room> getListRoomByHouseIdAndFloor(Long houseid, int floor, Pageable page){

        return roomrepository.findAllByHouse_IdAndFloor(houseid, floor, page);


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
        oldRoom.setMaxTenant(updateRoom.getMaxTenant());
        oldRoom.setName(updateRoom.getName());

        //Cap nhat so dien nuoc co phong de tao hoa don thu tien
        oldRoom.setElectricNumber(updateRoom.getChiSoCuoiDien()-oldRoom.getChiSoCuoiDien());
        oldRoom.setWaterNumber(updateRoom.getChiSoCuoiNuoc()-oldRoom.getChiSoCuoiNuoc());

        //Cap nhat lai chi so cuoi dien nuoc
        oldRoom.setChiSoCuoiDien(updateRoom.getChiSoCuoiDien());
        oldRoom.setChiSoCuoiNuoc(updateRoom.getChiSoCuoiNuoc());

        return roomrepository.save(oldRoom);
    }

    public Room getRoomById(Long roomId){

        return roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
    }




    public void deleteRoom(Long roomId) {

        roomrepository.deleteById(roomId);
    }

}
