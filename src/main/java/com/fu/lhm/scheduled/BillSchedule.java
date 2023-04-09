package com.fu.lhm.scheduled;

import com.fu.lhm.bill.Bill;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillSchedule {

    private final BillRepository billRepository;

    private final TenantRepository tenantRepository;

    private final NotificationRepository notificationRepository;

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

            LocalDate dateCreate = convertToLocalDateViaInstant(bill.getDateCreate());
            long days = today.until(dateCreate, ChronoUnit.DAYS);

            //neu hop dong het han
                if (days>=15) {
                    checkIfBill15days(bill, days);
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
        notification.setMessage("Hóa đơn "+billType+" ở phòng " + bill.getContract().getTenant().getRoom().getName()+" đã "+days+" chưa thanh toán");
        notification.setDateCreate(new Date());
        notification.setIsRead(false);
        notification.setUser(bill.getContract().getTenant().getRoom().getHouse().getUser());
        notificationRepository.save(notification);
    }
}
