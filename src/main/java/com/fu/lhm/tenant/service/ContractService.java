package com.fu.lhm.tenant.service;

import com.fu.lhm.tenant.modal.CreateContractFromBooking;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.ContractRepository;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final TenantRepository tenantRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomrepository;


    public Contract createContract(Long roomId, CreateContractRequest createContractRequest){
        int randomNumber = (int)(Math.random()*(99999-10000+1)+10000);

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

        //create tenant
        Tenant tenant = new Tenant();
        tenant.setName(createContractRequest.getTenantName());
        tenant.setEmail(createContractRequest.getEmail());
        tenant.setPhone(createContractRequest.getPhone()+"");
        tenant.setAddress(createContractRequest.getAddress());
        tenant.setContractHolder(true);
        tenant.setRoom(room);
//        tenant.setHouse(room.getHouse());

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ"+randomNumber);
        contract.setActive(true);
        contract.setDeposit(createContractRequest.getDeposit());
        contract.setFromDate(createContractRequest.getFromDate());
        contract.setToDate(createContractRequest.getFromDate());
        contract.setTenant(tenantRepository.save(tenant));
//        contract.setRoom(room);

        return contractRepository.save(contract);
    }

    public Contract createContractFromBooking(Long roomId, CreateContractFromBooking createContractFromBooking){
        int randomNumber = (int)(Math.random()*(99999-10000+1)+10000);

        //get room and change status book
        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        room.setHaveBookRoom(false);
        roomrepository.save(room);

        //create tenant
        Tenant tenant = tenantRepository.findById(createContractFromBooking.getTenantId()).orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại!"));
        tenant.setBookRoom(false);
        tenantRepository.save(tenant);

        //create contract
        Contract contract = new Contract();
        contract.setContractCode("HĐ"+randomNumber);
        contract.setActive(true);
        contract.setDeposit(createContractFromBooking.getDeposit());
        contract.setFromDate(createContractFromBooking.getFromDate());
        contract.setToDate(createContractFromBooking.getFromDate());
        contract.setTenant(tenant);
//        contract.setRoom(room);

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



}
