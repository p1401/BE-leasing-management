package com.fu.lhm.tenant.service;

import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.model.TenantRequest;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    private final RoomRepository roomrepository;


    public Tenant createTenant(Long roomId, Tenant tenant) {

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

        room.setCurrentTenant(room.getCurrentTenant() + 1);
        tenant.setRoom(room);
        tenant.setRoomName(room.getName());
        tenant.setHouseName(room.getHouse().getName());
        tenant.setIsContractHolder(false);
        tenant.setIsStay(true);

        return tenantRepository.save(tenant);

    }

    public Tenant updateTenant(Long tenantId, Tenant newTenant) {

        Tenant oldTenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));

        oldTenant.setName(newTenant.getName());
        oldTenant.setEmail(newTenant.getEmail());
        oldTenant.setAddress(newTenant.getAddress());
        oldTenant.setPhone(newTenant.getPhone());
        oldTenant.setBirth(newTenant.getBirth());
        oldTenant.setIdentifyNumber(newTenant.getIdentifyNumber());

        return tenantRepository.save(oldTenant);
    }

    public Page<Tenant> getListTenantByRoomId(Long id, Pageable page) {

        return tenantRepository.findAllByRoom_IdAndIsStay(id,true, page);
    }


    public Page<Tenant> getListTenants(Long houseId,
                                   Long roomId,
                                   Boolean isStay,
                                   Pageable page) {
        if (roomId != null) {
            return tenantRepository.findAllByRoom_IdAndIsStay(roomId,isStay, page);
        }

        if (houseId != null) {
            return tenantRepository.findAllByRoom_House_IdAndIsStay(houseId,isStay, page);
        }

        return Page.empty(page);
    }

    public Tenant getTenantById(Long id) {

        return tenantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));
    }

    public Tenant leaveRoom(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));
        tenant.setIsStay(false);
        Room room = roomrepository.findById(tenant.getRoom().getId()).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        room.setCurrentTenant(room.getCurrentTenant()-1);
        roomrepository.save(room);
        return tenantRepository.save(tenant);
    }

    public void deleteTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));
        if(tenant.getIsStay()==true){
            Room room = tenant.getRoom();
            room.setCurrentTenant(room.getCurrentTenant()-1);
            roomrepository.save(room);
        }

        tenantRepository.deleteById(tenantId);
    }


}
