package com.fu.lhm.scheduled;

import com.fu.lhm.notification.entity.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import lombok.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractScheduled {

    private final ContractRepository contractRepository;

    private final TenantRepository tenantRepository;

    private final NotificationRepository notificationRepository;


// "0 0 * * * *" = the top of every hour of every day.
// "*/10 * * * * *" = every ten seconds.
// "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
// "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
// "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
// "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
// "0 0 0 25 12 ?" = every Christmas Day at midnight
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    //Run every day at 12h
    @Scheduled(cron = "0 0 6 * * *")
    public void checkDurationContract() {

            LocalDate today = LocalDate.now();
            List<Contract>  listContact = contractRepository.findAllByIsActiveTrue();
            for (Contract contract : listContact) {

                LocalDate toDate = convertToLocalDateViaInstant(contract.getToDate());
                long days = today.until(toDate, ChronoUnit.DAYS);

                //neu hop dong het han
                if (days==0) {
                    checkIfContractExpired(contract);
                } else if (days<=30 && days>0) {

                    checkIfContractDurationUnder30days(contract, days);
                }
            }
        }


    //if contract expired set all tenant in room isStay=false
    public void checkIfContractExpired(Contract contract){
        contract.setIsActive(false);
        //set Tenant in room isStay==false and contract active==false
        List<Tenant> listTenantInRoom = tenantRepository.findAllByRoom_IdAndIsStayTrue(contract.getTenant().getRoom().getId());
        for(Tenant tenant : listTenantInRoom){
            tenant.setIsStay(false);
            tenantRepository.save(tenant);
        }
        contractRepository.save(contract);
    }

    public void checkIfContractDurationUnder30days(Contract contract, long days){
        Notification notification = new Notification();
        notification.setDateCreate(new Date());
        notification.setMessage("Hop dong phong "+contract.getTenant().getRoom().getHouse().getName()+", phong "+contract.getRoomName()+" con " + days + " ngay se het han");
        notification.setIsRead(false);
        notification.setUser(contract.getTenant().getRoom().getHouse().getUser());
        System.out.println("Hop dong phong "+contract.getTenant().getRoom().getHouse().getName()+", phong "+contract.getRoomName()+" con " + days + " ngay se het han");
        notificationRepository.save(notification);
    }
}
