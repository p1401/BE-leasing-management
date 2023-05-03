package com.fu.lhm.scheduled;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.tenant.repository.TenantRepository;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Generated
@Component
@RequiredArgsConstructor
public class BillSchedule {

    private final BillRepository billRepository;

    private final NotificationRepository notificationRepository;
    private final ContractRepository contractRepository;

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    //Run every day at 12h
    @Scheduled(cron = "0 0 6 * * *")
    public void checkDurationBill() {
        LocalDate today = LocalDate.now();
        List<Bill> listBill = billRepository.findAllByIsPayFalse();
        for (Bill bill : listBill) {
            long days = today.until(bill.getDateCreate(), ChronoUnit.DAYS);
            //neu qua 15 ngay chưa thanh toán
                if (days<=-15) {
                    System.out.println("Quá hạn 15 ngày");
                    checkIfBill15days(bill, -days);
            }
        }
    }

    //Tự động tạo hóa đơn theo ngày nhập ở hợp đồng
    @Scheduled(cron = "0 0 6 * * *")
    public void checkAutoCreateBill() {
        LocalDate today = LocalDate.now();
        List<Contract> listContract = contractRepository.findAllByIsActiveTrue();
        for (Contract contract : listContract) {
            int autoBillDate = contract.getAutoBillDate();
            if (autoBillDate > today.lengthOfMonth()) {
                autoBillDate = today.lengthOfMonth();
            }
            if(autoBillDate==today.getDayOfMonth()){
                long roomId = contract.getTenant().getRoom().getId();
                int month = today.getMonthValue();
                int year = today.getYear();
                if(checkBillExistsInMonthAndYear(roomId, month,year)==false){
                    createBill(contract);
                }
            }
        }
    }

    public void checkIfBill15days(Bill bill, long days){
        String billType = "";

        if(bill.getBillType().name().equalsIgnoreCase("RECEIVE") && bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")){
            billType="tiền phòng";
        }else if(bill.getBillType().name().equalsIgnoreCase("RECEIVE") && bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI")){
            billType="tiền phụ trội";
        }
        Notification notification = new Notification();
        notification.setMessage("Hóa đơn "+billType+" ở phòng " + bill.getContract().getTenant().getRoom().getName()+" đã "+days+" ngày chưa thanh toán");
        notification.setDateCreate(new Date());
        notification.setIsRead(false);
        notification.setRoomId(bill.getRoomId());
        notification.setHouseId(bill.getHouseId());
        notification.setUser(bill.getContract().getTenant().getRoom().getHouse().getUser());

       notificationRepository.save(notification);
    }

    public void createBill(Contract contract){
        Room room = contract.getTenant().getRoom();
        House house = room.getHouse();
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
        bill.setDateCreate(LocalDate.now());
        bill.setDescription("Tiền phòng " + contract.getTenant().getRoom().getName() + " ngày "+LocalDate.now());
        bill.setTotalMoney(room.getRoomMoney() + room.getWaterElectric().getNumberElectric() * house.getElectricPrice() + room.getWaterElectric().getNumberWater() * house.getWaterPrice());
        bill.setBillContent(BillContent.TIENPHONG);
        bill.setBillType(BillType.RECEIVE);
        bill.setRoomId(contract.getTenant().getRoom().getId());
        bill.setHouseId(contract.getTenant().getRoom().getHouse().getId());

        bill.setContract(contract);

        billRepository.save(bill);
    }

    public Boolean checkBillExistsInMonthAndYear( Long roomId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Bill> listBill = billRepository.findByContract_Tenant_Room_IdAndBillTypeAndBillContentAndDateCreateBetween(roomId, BillType.RECEIVE, BillContent.TIENPHONG, startDate, endDate);
        if (!listBill.isEmpty()) {
            return false;
        }
        return true;
    }
}


