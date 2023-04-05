package com.fu.lhm.tenant.service;

import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.room.Room;

import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

        private final TenantRepository tenantRepository;

        private final RoomRepository roomrepository;

        public Tenant createTenant(Long roomId, Tenant tenant){

            Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

            room.setCurrentTenant(room.getCurrentTenant()+1);
            tenant.setRoomName(room.getName());
            tenant.setHouseName(room.getHouse().getName());
            tenant.setRoom(room);
            tenant.setContractHolder(false);

            return tenantRepository.save(tenant);

        }

    public Tenant updateTenant(Long tenantId, Tenant newTenant){

        Tenant oldTenant = tenantRepository.findById(tenantId).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));

        oldTenant.setName(newTenant.getName());
        oldTenant.setEmail(newTenant.getEmail());
        oldTenant.setAddress(newTenant.getEmail());
        oldTenant.setPhone(newTenant.getPhone());

        return tenantRepository.save(oldTenant);
    }

    public Page<Tenant> getListTenantByRoomId(Long id, Pageable page){

            return tenantRepository.findAllByRoom_Id(id, page);
    }

    public Page<Tenant> getListTenantByHouseId(Long id, Pageable page){

        return tenantRepository.findAllByRoom_House_Id(id, page);
    }

    public Tenant getTenantById(Long id){

            return tenantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Người thuê không tồn tại!"));
    }


    public void deleteTenant(Long tenantId) {
        tenantRepository.deleteById(tenantId);
    }








}
