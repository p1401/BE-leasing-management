package com.fu.lhm.room.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.waterElectric.repository.WaterElectricRepositoy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {


    private final RoomRepository roomRepository;

    private final HouseRepository houseRepository;

    private final WaterElectricRepositoy waterElectricRepositoy;

    private final BillRepository billRepository;

    public Page<Room> getListRoomByHouseIdAndFloor(Long houseId, int floor,String roomName, Pageable page) {

            Page<Room> listRoom = roomRepository.findAllByHouse_IdAndFloorAndNameContainingIgnoreCase(houseId, floor, roomName, page);

            for(Room room : listRoom){
                List<Bill> listBill = billRepository.findAllByRoomIdAndBillType(room.getId(), BillType.RECEIVE);

                for(Bill bill :listBill){
                    if(bill.getIsPay()==false){
                        room.setMoneyNotPay(room.getMoneyNotPay()+bill.getTotalMoney());
                    }
                }
            }
        return listRoom;
    }

    public Room createRoom(Long houseId, Room room) throws BadRequestException {

        House house = houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nha không tồn tại!"));
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
        waterElectric.setRoom(roomRepository.save(newRoom));
        waterElectric.setDateUpdate(new Date());
        waterElectricRepositoy.save(waterElectric);

        return roomRepository.save(newRoom);

    }


    public Room updateRoom(Long roomId, Room updateRoom) throws BadRequestException {

        Room oldRoom = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        oldRoom.setFloor(updateRoom.getFloor());
        oldRoom.setArea(updateRoom.getArea());
        oldRoom.setRoomMoney(updateRoom.getRoomMoney());
        oldRoom.setMaxTenant(updateRoom.getMaxTenant());
        oldRoom.setName(updateRoom.getName());

        return roomRepository.save(oldRoom);
    }

    public Room getRoom(Long roomId) throws BadRequestException {
        return roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
    }


    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

}
