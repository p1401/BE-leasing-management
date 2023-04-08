package com.fu.lhm.tenant.service;

import com.fu.lhm.room.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.model.ContractBookingRequest;
import com.fu.lhm.tenant.model.ContractRequest;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;

    public Contract getContractById(Long contractId) {

        return contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
    }

    public Contract createContract(ContractRequest contractRequest) {
        int randomNumber = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        long roomId = contractRequest.getRoomId();
        Date fromDate = contractRequest.getFromDate();
        Date toDate = contractRequest.getToDate();

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()+1);
        roomrepository.save(room);
        //create tenant
        Tenant tenant = contractRequest.getTenant();
        tenant.setIsContractHolder(true);
        tenant.setRoom(room);
//        tenant.setHouse(room.getHouse());

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ" + randomNumber);
        contract.setIsActive(true);
        contract.setDeposit(contractRequest.getDeposit());
        contract.setFromDate(fromDate);
        contract.setToDate(toDate);
        contract.setTenant(tenantRepository.save(tenant));

        return contractRepository.save(contract);
    }

    public Contract createContractFromBooking(Long roomId, @NotNull ContractBookingRequest contractBookingRequest) {
        int randomNumber = (int) (Math.random() * (99999 - 10000 + 1) + 10000);

        Date fromDate = contractBookingRequest.getFromDate();
        Date toDate = contractBookingRequest.getToDate();

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        room.setHaveBookRoom(false);
        roomrepository.save(room);

        //create tenant
        Tenant tenant = tenantRepository.findById(contractBookingRequest.getTenantId()).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));
        tenant.setIsContractHolder(true);
        tenantRepository.save(tenant);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ" + randomNumber);
        contract.setIsActive(true);
        contract.setDeposit(contractBookingRequest.getDeposit());
        contract.setFromDate(fromDate);
        contract.setToDate(toDate);
        contract.setTenant(tenant);
        return contractRepository.save(contract);
    }

    public Contract changeHolder(Long contractId, Long oldTenantId, Long newTenantId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        Tenant oldTenant = tenantRepository.findById(oldTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));
        Tenant newTenant = tenantRepository.findById(newTenantId).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));

        oldTenant.setIsContractHolder(false);
        tenantRepository.save(oldTenant);
        newTenant.setIsContractHolder(true);

        contract.setTenant(tenantRepository.save(newTenant));

        return contractRepository.save(contract);
    }

    public Contract updateContract(Long contractId, Contract newContract) {
        Contract oldContract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException("Hợp đồng không tồn tại!"));
        oldContract.setToDate(newContract.getToDate());
        contractRepository.save(oldContract);
        return oldContract;
    }


}
