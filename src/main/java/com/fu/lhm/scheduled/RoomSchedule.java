package com.fu.lhm.scheduled;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.repository.WaterElectricRepositoy;
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
public class RoomSchedule {

    private final ContractRepository contractRepository;

    private final NotificationRepository notificationRepository;

    //check cập nhật điện nước trong 30 ngày chưa
    @Scheduled(cron = "0 0 6 * * *")
    public void checkDurationUpdateWaterElectric() {
        List<Contract> listContract = contractRepository.findAllByIsActiveTrue();

        LocalDate today = LocalDate.now();

        for(Contract contract : listContract){
            WaterElectric waterElectric = contract.getTenant().getRoom().getWaterElectric();
            LocalDate dateUpdate = convertToLocalDateViaInstant(contract.getToDate());
            long days = today.until(dateUpdate, ChronoUnit.DAYS);
            if(days>=30){
                createNotification(days,waterElectric);
            }
        }
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public void createNotification(long days, WaterElectric waterElectric){
        Notification notification = new Notification();
        notification.setDateCreate(new Date());
        notification.setMessage("Chưa cập nhật điện nước ở nhà "+waterElectric.getRoom().getHouse().getName()+", phòng "+waterElectric.getRoom().getName()+" trong " + days + " ngày qua");
        notification.setIsRead(false);
        notification.setUser(waterElectric.getRoom().getHouse().getUser());
        System.out.println("Chưa cập nhật điện nước ở nhà "+waterElectric.getRoom().getHouse().getName()+", phòng "+waterElectric.getRoom().getName()+" trong " + days + " ngày qua");
        notificationRepository.save(notification);

    }



}
