package com.fu.lhm.contract.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.modal.BillReceiveRequest;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;
    private final BillService billService;

    public Contract getContractById(Long contractId) {

        return contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
    }

    public Contract createContract(ContractRequest contractRequest) throws BadRequestException {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        long roomId = contractRequest.getRoomId();
        Date fromDate = contractRequest.getFromDate();
        Date toDate = contractRequest.getToDate();

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()+1);
        roomrepository.save(room);
        //create tenant
        Tenant tenant = contractRequest.getTenant();
        tenant.setIsContractHolder(true);
        tenant.setRoomName(room.getName());
        tenant.setHouseName(room.getHouse().getName());
        tenant.setRoom(room);
        tenant.setIsStay(true);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ" + randomNumber);
        contract.setIsActive(true);
        contract.setDeposit(contractRequest.getDeposit());
        contract.setFromDate(fromDate);
        contract.setToDate(toDate);
        contract.setTenant(tenantRepository.save(tenant));
        contract.setRoomName(room.getName());
        contract.setHouseName(room.getHouse().getName());
        contract.setAutoBillDate(contract.getAutoBillDate());

        //Create bill TIENCOC
        BillReceiveRequest bill = new BillReceiveRequest();
        bill.setBillType(BillType.RECEIVE);
        bill.setBillContent(BillContent.TIENCOC);
        bill.setTotalMoney(Integer.parseInt(String.valueOf(contract.getDeposit())));
        bill.setDateCreate(contractRequest.getFromDate());
        bill.setPayer(tenant.getName());
        bill.setIsPay(true);
        bill.setDescription("Tiền cọc của khách "+tenant.getName());
        billService.createBillReceive(roomId,bill);

        return contractRepository.save(contract);
    }


    public Contract changeHolder(Long contractId, Long oldTenantId, Long newTenantId) throws BadRequestException {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        Tenant oldTenant = tenantRepository.findById(oldTenantId).orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại!"));
        Tenant newTenant = tenantRepository.findById(newTenantId).orElseThrow(() -> new BadRequestException("Khách hàng không tồn tại!"));

        oldTenant.setIsContractHolder(false);
        tenantRepository.save(oldTenant);
        newTenant.setIsContractHolder(true);

        contract.setTenant(tenantRepository.save(newTenant));

        return contractRepository.save(contract);
    }

    public Contract updateContract(Long contractId, Contract newContract) throws BadRequestException {
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        oldContract.setToDate(newContract.getToDate());
        contractRepository.save(oldContract);
        return oldContract;
    }

    public Contract liquidationContract(Long contractId) throws BadRequestException {
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        oldContract.setIsActive(false);
        contractRepository.save(oldContract);
        List<Tenant> listTenantInRoom = tenantRepository.findAllByRoom_IdAndIsStayTrue(oldContract.getTenant().getRoom().getId());
        for(Tenant tenant : listTenantInRoom){
            tenant.setIsStay(false);
            tenantRepository.save(tenant);
        }
        return oldContract;
    }
    public Page<Contract> getContracts(Long houseId,
                                   Long roomId,
                                   Boolean isActive,
                                   Pageable page) {
        if (roomId != null) {
            return contractRepository.findAllByTenant_Room_IdAndIsActive(roomId,isActive, page);
        }

        if (houseId != null) {
            return contractRepository.findAllByTenant_Room_House_IdAndIsActive(houseId,isActive, page);
        }

        return Page.empty(page);
    }


}
