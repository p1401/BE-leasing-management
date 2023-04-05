package com.fu.lhm.financial.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.BillContent;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.house.House;
import com.fu.lhm.room.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ContractRepository contractRepository;

    private final BillRepository billRepository;

    private final RoomRepository roomRepository;

    public Bill createBillTienPhong(Long roomId,Bill bill){

        Contract contract = contractRepository.findByTenant_Room_Id(roomId);
        Room room = contract.getTenant().getRoom();
        bill.setContract(contract);
        room.setMoneyNotPay(room.getMoneyNotPay()+bill.getTotalMoney());
        roomRepository.save(room);
        return billRepository.save(bill);
    }

    public Bill payBill(Long billId){
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
        bill.setPay(true);
        return billRepository.save(bill);
    }

    public Bill getBillById(Long billId){
        return billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
    }

    public void deleteBill(Long billId){
        billRepository.deleteById(billId);
    }

    public Page<Bill> getListBillByRoomId(Long roomId, Pageable page){

        return billRepository.findAllByContract_Tenant_Room_Id(roomId,page);
    }

    public Page<Bill> getListBillByRoomIdNotPay(Long roomId, Pageable page){

        return billRepository.findByPayFalseAndContract_Tenant_Room_id(roomId,page);
    }

    public Page<Bill> getListBillByHouseId(Long houseId, Pageable page){

        return billRepository.findAllByContract_Tenant_Room_House_Id(houseId, page);
    }

    public List<Bill> createAllBill(){

        List<Contract> listContract = contractRepository.findAll();

        List<Bill> listBill = billRepository.findAll();

        List<Bill> newListBill = new ArrayList<>();
        //Lay thang hien tai
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        //check xem từng hợp đồng đã tạo hóa đơn tháng này chưa
        for(Contract contract : listContract){
            House house = contract.getTenant().getRoom().getHouse();
            Room room = contract.getTenant().getRoom();
            //check bill tien phong da tao thang nay chua
            boolean isCreate = false;
            for(Bill bill : listBill){
                if(bill.getDateCreate().getMonthValue()==month
                        && bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")
                        && bill.getContract().getTenant().getRoom()==contract.getTenant().getRoom()){
                    isCreate=true;
                }
            }

            //Neu isCreate==false thi tao tien phong thang nay cho tung hop dong
            if(contract.isActive()==true && isCreate==false){

                int randomNumber = (int)(Math.random()*(99999-10000+1)+10000);
                Bill bill = new Bill();
                bill.setBillCode("PT"+randomNumber);
                bill.setRoomMoney(room.getRoomMoney());
                bill.setElectricNumber(room.getWaterElectric().getNumberElectric());
                bill.setWaterNumber(room.getWaterElectric().getNumberWater());
                bill.setElectricMoney(room.getWaterElectric().getNumberElectric()*house.getElectricPrice());
                bill.setWaterMoney(room.getWaterElectric().getNumberWater()*house.getWaterPrice());
                bill.setPayer(contract.getTenant().getName());
                bill.setPay(false);
                bill.setDateCreate(LocalDate.now());
                bill.setDescription("Tiền phòng "+contract.getTenant().getRoom().getName()+" tháng " +month);
                bill.setTotalMoney(room.getRoomMoney()+room.getWaterElectric().getNumberElectric()*house.getElectricPrice()+room.getWaterElectric().getNumberWater()*house.getWaterPrice());
                bill.setBillContent(BillContent.TIENPHONG);
                bill.setContract(contract);

                billRepository.save(bill);
                //luu tien chua dong vao room
                room.setMoneyNotPay(room.getMoneyNotPay()+bill.getTotalMoney());
                roomRepository.save(room);

                newListBill.add(bill);
            }
        }

        return newListBill;
    }







}
