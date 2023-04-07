package com.fu.lhm.room.service;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.house.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.room.WaterElectric;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.repository.WaterElectricRepositoy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {


    private final RoomRepository roomRepository;

    private final HouseRepository houseRepository;

    private final WaterElectricRepositoy waterElectricRepositoy;

    private final BillRepository billRepository;

    public Page<Room> getListRoomByHouseIdAndFloor(Long houseId, int floor, Pageable page) {

            Page<Room> listRoom = roomRepository.findAllByHouse_IdAndFloor(houseId, floor, page);

            for(Room room : listRoom){
                List<Bill> listBill = billRepository.findAll();

                for(Bill bill :listBill){
                    if(bill.getIsPay()==false && bill.getBillType().name().equalsIgnoreCase("RECEIVE") && bill.getContract().getTenant().getRoom().getId()==room.getId()){
                        room.setMoneyNotPay(room.getMoneyNotPay()+bill.getTotalMoney());
                    }
                }
            }

        return listRoom;


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
        waterElectric.setRoom(roomRepository.save(newRoom));
        waterElectricRepositoy.save(waterElectric);

        return newRoom;

    }


    public Room updateRoom(Long roomId, Room updateRoom) {

        Room oldRoom = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        oldRoom.setFloor(updateRoom.getFloor());
        oldRoom.setArea(updateRoom.getArea());
        oldRoom.setRoomMoney(updateRoom.getRoomMoney());
        oldRoom.setMaxTenant(updateRoom.getMaxTenant());
        oldRoom.setName(updateRoom.getName());

        return roomRepository.save(oldRoom);
    }

    public Room getRoomById(Long roomId) {

        return roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
    }


    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

}
