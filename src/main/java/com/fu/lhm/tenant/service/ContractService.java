package com.fu.lhm.tenant.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.JwtService;
import com.fu.lhm.notification.Notification;
import com.fu.lhm.notification.repository.NotificationRepository;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.tenant.validate.ContractValidate;
import com.fu.lhm.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;

    private NotificationRepository notificationRepository;


    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    private static final int ALERT_THRESHOLD = 10;

    public Contract getContractById(Long contractId){

        return contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
    }

    public Page<Contract> getListContractByHouseId(Long houseId, Pageable pageable){

        return contractRepository.findAllByTenant_Room_House_Id(houseId, pageable);
    }

    public List<Contract> getListContract(){

        return contractRepository.findAllByTenant_Room_House_User(this.getUserToken());
    }

    public Contract createContract(Long roomId, CreateContractRequest createContractRequest){
        int randomNumber = (int)(Math.random()*(99999-10000+1)+10000);

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()+1);
        //create tenant
        Tenant tenant = new Tenant();
        tenant.setName(createContractRequest.getTenantName());
        tenant.setEmail(createContractRequest.getEmail());
        tenant.setPhone(createContractRequest.getPhone()+"");
        tenant.setAddress(createContractRequest.getAddress());
        tenant.setBirth(createContractRequest.getBirth());
        tenant.setIdentityNumber(createContractRequest.getIdentityNumber());
        tenant.setContractHolder(true);
        tenant.setRoomName(room.getName());
        tenant.setHouseName(room.getHouse().getName());
        tenant.setRoom(room);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ"+randomNumber);
        contract.setHouseName(createContractRequest.getHouseName());
        contract.setRoomName(createContractRequest.getRoomName());
        contract.setFloor(createContractRequest.getFloor());
        contract.setActive(true);
        contract.setDeposit(createContractRequest.getDeposit());
        contract.setFromDate(createContractRequest.getFromDate());
        contract.setToDate(createContractRequest.getToDate());
        contract.setTenant(tenantRepository.save(tenant));
        return contractRepository.save(contract);
    }


    public Contract changeHolder(Long contractid, Long oldTenantId, Long newTenantId){
        Contract contract = contractRepository.findById(contractid).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        Tenant oldTenant = tenantRepository.findById(oldTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));
        Tenant newTenant = tenantRepository.findById(newTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));

        oldTenant.setContractHolder(false);
        newTenant.setContractHolder(true);

        contract.setTenant(newTenant);

        return contract;
    }

    public Contract updateContract(Long contractId, Contract newContract){
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        oldContract.setToDate(newContract.getToDate());
        contractRepository.save(oldContract);
        return oldContract;
    }


    @Scheduled(cron = "0/5 * * * * *") // run every 5 seconds
    public void generateNotifications() {
        List<Contract> contracts = this.getListContract();
        LocalDate now = LocalDate.now();

        for (Contract contract : contracts) {
            if (contract.getToDate().isAfter(now)) {
                // generate alert for expired contract
//                Notification notification = new Notification();
//                notification.setTitle("Contract Expiration");
//                notification.setDescription("Contract of room " + contract.getRoomName() +
//                                            " is expiring on " + contract.getToDate());
//                notification.setTime(LocalDate.now());
//                notificationRepository.save(notification);
                System.out.println("Hop dong phong " + contract.getRoomName() + "da qua han");
            } else {
                // generate alert for upcoming contract expiration
                Period period = Period.between(contract.getToDate(), now);
                long timeUntilExpiration = period.getDays();
                if (timeUntilExpiration <= ALERT_THRESHOLD) {
//                    alertService.generateContractExpirationAlert(contract);
                    System.out.println("Hop dong phong " + contract.getRoomName() + "con " + period + " ngay se het han");
                }
            }
        }
    }

    private User getUserToken() {
        return jwtService.getUser(httpServletRequest);
    }

}
