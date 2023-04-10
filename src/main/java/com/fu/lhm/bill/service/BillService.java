package com.fu.lhm.bill.service;

import com.fu.lhm.bill.modal.BillRequest;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.bill.Bill;
import com.fu.lhm.bill.BillContent;
import com.fu.lhm.bill.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.house.House;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Bill createBillTienPhong(Long roomId, BillRequest billRequest) {

        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        Contract contract = contractRepository.findByTenant_Room_Id(roomId);
        Bill bill = mapToBill(billRequest);
        bill.setBillCode("PT"+randomNumber);
        bill.setContract(contract);
        return billRepository.save(bill);
    }

    public static Bill mapToBill(BillRequest billRE) {
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



    public Page<Bill> getListBillByRoomId(Long roomId, Pageable pageable) {

        return billRepository.findAllByContract_Tenant_Room_Id(roomId, pageable);
    }

    public void deleteBill(Long billId) {
        billRepository.delete(billRepository.findById(billId).get());
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
    }

    public Bill payBill(Long billId) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new BadRequestException("Hóa đơn không tồn tại!"));
        bill.setIsPay(true);

        return billRepository.save(bill);
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public List<Bill> createAllBill() {

        List<Contract> listContract = contractRepository.findAll();

        List<Bill> listBill = billRepository.findAll();

        List<Bill> newListBill = new ArrayList<>();
        //Lay thang hien tai
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        //check xem từng hợp đồng đã tạo hóa đơn tháng này chưa
        for (Contract contract : listContract) {

            Room room = contract.getTenant().getRoom();
            House house = contract.getTenant().getRoom().getHouse();
            //check bill tien phong da tao thang nay chua
            boolean isCreate = false;
            for (Bill bill : listBill) {
//                LocalDate dateCreate = convertToLocalDateViaInstant(bill.getDateCreate());
                if (bill.getDateCreate().getMonthValue() == month
                        && bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")
                        && bill.getBillType().name().equalsIgnoreCase("RECEIVE")
                        && bill.getContract().getTenant().getRoom() == contract.getTenant().getRoom()) {
                    isCreate = true;
                }
            }

            //Neu isCreate==false thi tao tien phong thang nay cho tung hop dong
            if (contract.getIsActive() && !isCreate) {
                int randomNumber = (int) (Math.random() * (99999 - 10000 + 1) + 10000);
                Bill bill = new Bill();
                bill.setBillCode("PT" + randomNumber);
                bill.setRoomMoney(room.getRoomMoney());
                bill.setElectricNumber(room.getWaterElectric().getNumberElectric());
                bill.setWaterNumber(room.getWaterElectric().getNumberWater());
                bill.setElectricMoney(room.getWaterElectric().getNumberElectric() * house.getElectricPrice());
                bill.setWaterMoney(room.getWaterElectric().getNumberWater() * house.getWaterPrice());
                bill.setPayer(contract.getTenant().getName());
                bill.setIsPay(false);
//                bill.setDateCreate(new Date());
                bill.setDescription("Tiền phòng " + contract.getTenant().getRoom().getName() + " tháng " + month);
                bill.setTotalMoney(room.getRoomMoney() + room.getWaterElectric().getNumberElectric() * house.getElectricPrice() + room.getWaterElectric().getNumberWater() * house.getWaterPrice());
                bill.setBillContent(BillContent.TIENPHONG);
                bill.setBillType(BillType.RECEIVE);
                bill.setContract(contract);

                billRepository.save(bill);
                newListBill.add(bill);
            }
        }

        return newListBill;
    }




}
