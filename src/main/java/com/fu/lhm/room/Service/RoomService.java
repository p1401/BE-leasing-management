package com.fu.lhm.room.Service;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.house.House;
import com.fu.lhm.house.Repository.HouseRepository;
import com.fu.lhm.room.Repository.Roomrepository;
import com.fu.lhm.room.SendListRoomAndInforRequest;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final Roomrepository roomrepository;

    private final HouseRepository houseRepository;

    private final TenantRepository tenantRepository;
    private final ContractRepository contractRepository;

    private final BillRepository billRepository;

    public List<SendListRoomAndInforRequest> getListRoomAndInfor(Long houseid){

        List<Room> listRoom = roomrepository.findAllByHouse_Id(houseid);

        List<Tenant> tenantList = tenantRepository.findAll();

        List<Contract> contractList = contractRepository.findAll();

        List<Bill> billList =billRepository.findAll();

        List<SendListRoomAndInforRequest> list = new ArrayList<>();

        for(Room room : listRoom){
            SendListRoomAndInforRequest sendListRoomAndInforRequest = new SendListRoomAndInforRequest();

            sendListRoomAndInforRequest.setRoomId(room.getId());
            sendListRoomAndInforRequest.setRoomName(room.getName());
            sendListRoomAndInforRequest.setPrice(room.getRoomMoney());
            sendListRoomAndInforRequest.setArea((room.getArea()));
            sendListRoomAndInforRequest.setMaxTenant(room.getMaxTenant());

            int curentTenant =0;
            for(Tenant tenant : tenantList){
                if(tenant.getRoom().getId()==room.getId()&&tenant.isBookRoom()==false){
                    curentTenant=curentTenant+1;
                }
            }
            sendListRoomAndInforRequest.setCurrenTenant(curentTenant);

            //Nếu đã có người ký hợp đồng thì thay
            String currentContract ="Chưa có";
            for(Contract contract : contractList){
                if(contract.getRoom().getId() == room.getId() && contract.isActive()==true){
                    currentContract = contract.getTenant().getName();
                }
            }
            sendListRoomAndInforRequest.setCurrentContract(currentContract);

            //Set tien chua thanh toan
            int moneyNotPay=0;
            for(Bill bill : billList){
                if(bill.getRoom().getId()==room.getId() && bill.isPay()==false){
                    moneyNotPay=moneyNotPay+bill.getTotalMoney();
                }
            }
            sendListRoomAndInforRequest.setMoneyNotPay(moneyNotPay);

            String tenantBooking = "Chua co";
            for(Tenant tenant : tenantList){
                if(tenant.getRoom().getId()==room.getId()&&tenant.isBookRoom()==true){
                    tenantBooking = tenant.getName();
                }
            }
            sendListRoomAndInforRequest.setTenantBooking(tenantBooking);

            list.add(sendListRoomAndInforRequest);
        }

        return list;
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




    public void deleteRoom(Long roomId) {

        roomrepository.deleteById(roomId);
    }

}
