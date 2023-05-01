package com.fu.lhm.bill.service;

import com.fu.lhm.bill.model.BillReceiveRequest;
import com.fu.lhm.bill.model.BillRequest;
import com.fu.lhm.bill.model.BillSpendRequest;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ContractRepository contractRepository;

    private final BillRepository billRepository;

    private final RoomRepository roomRepository;


    public Bill createBillReceive2(User user,Long houseId, Long roomId, BillReceiveRequest billRequest) throws BadRequestException {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Bill bill = mapToBillReceive2(billRequest);
        bill.setBillCode("PT"+randomNumber);
        bill.setRoomId(roomId==null?0:roomId);
        bill.setHouseId(houseId);
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }

    public Bill createBillReceive(User user, Long roomId, BillReceiveRequest billRequest) throws BadRequestException {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        Contract contract = contractRepository.findByTenant_Room_IdAndIsActiveTrue(roomId);
        Bill bill = mapToBillReceive(billRequest);
        bill.setBillCode("PT"+randomNumber);
        bill.setPayer(contract.getTenantName());
        bill.setContract(contract);
        bill.setRoomId(roomId);
        bill.setHouseId(room.getHouse().getId());
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }

    public Bill createBillSpend(User user,Long roomId, BillSpendRequest billRequest) throws BadRequestException {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Bill bill = mapToBillSpend(billRequest);
        bill.setBillContent(BillContent.TIENPHUTROI);
        bill.setBillCode("PC"+randomNumber);
        bill.setIsPay(true);
        bill.setRoomId(roomId);
        bill.setHouseId(room.getHouse().getId());
        bill.setUserId(user.getId());
        return billRepository.save(bill);
    }



    public static Bill mapToBillReceive(BillReceiveRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setRoomMoney(billRE.getRoomMoney());
        bill.setChiSoDauDien(billRE.getChiSoDauDien());
        bill.setChiSoDauNuoc(billRE.getChiSoDauNuoc());
        bill.setChiSoCuoiDien(billRE.getChiSoCuoiDien());
        bill.setChiSoCuoiNuoc(billRE.getChiSoCuoiNuoc());
        bill.setElectricNumber(billRE.getElectricNumber());
        bill.setWaterNumber(billRE.getWaterNumber());
        bill.setElectricMoney(billRE.getElectricMoney());
        bill.setWaterMoney(billRE.getWaterMoney());
        bill.setPayer(billRE.getPayer());
        bill.setIsPay(billRE.getIsPay());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        bill.setBillContent(billRE.getBillContent());
        return bill;
    }

    public static Bill mapToBillReceive2(BillReceiveRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setPayer(billRE.getPayer());
        bill.setIsPay(billRE.getIsPay());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        bill.setBillContent(billRE.getBillContent());
        return bill;
    }

    public static Bill mapToBillSpend(BillSpendRequest billRE) {
        Bill bill = new Bill();
        bill.setId(billRE.getId());
        bill.setDateCreate(billRE.getDateCreate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        bill.setDescription(billRE.getDescription());
        bill.setTotalMoney(billRE.getTotalMoney());
        bill.setBillType(billRE.getBillType());
        return bill;
    }

    public Page<Bill> getListBillByRoomId(Long roomId, Pageable pageable) {

        return billRepository.findAllByRoomId(roomId, pageable);
    }

    public void deleteBill(Long billId) {
        billRepository.deleteById(billId);
    }

    public Bill getBillById(Long billId) throws BadRequestException {
        return billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
    }

    public Bill payBill(Long billId) throws BadRequestException {
        Bill bill = billRepository.findById(billId).get();
        bill.setIsPay(true);

        return billRepository.save(bill);
    }

    public BillRequest getBills(Long userId,
                            Long houseId,
                            Long roomId,
                            Date fromDate,
                            Date toDate,
                            String billType,
                            Boolean isPay,
                            Pageable page) {
        BillRequest billRequest = new BillRequest();
        Integer receive = 0;
        Integer spend=0;
        Integer revenue = 0;
        if(billType.equalsIgnoreCase("")){
            billType=null;
        }
            Page<Bill> listBills = billRepository.findBills(userId,houseId,roomId,fromDate,toDate,billType,isPay, page);
            List<Bill> list =  billRepository.findBills(userId,houseId,roomId,fromDate,toDate,billType,isPay,Pageable.unpaged()).toList();
            for(Bill bill :list){

                if(bill.getBillType().equals(BillType.RECEIVE)
                        && !bill.getBillContent().equals(BillContent.TIENCOC)
                        && bill.getIsPay()==true){

                    receive = receive+bill.getTotalMoney();

                }
                if(bill.getBillType().equals(BillType.SPEND)){

                    spend = spend + bill.getTotalMoney();

                }
            }

            revenue = receive-spend;

            billRequest.setReceive(receive);
            billRequest.setSpend(spend);
            billRequest.setRevenue(revenue);
            billRequest.setListBill(listBills);
            return billRequest;
    }
}
