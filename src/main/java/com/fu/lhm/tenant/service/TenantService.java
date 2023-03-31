package com.fu.lhm.tenant.service;

import com.fu.lhm.room.Repository.Roomrepository;
import com.fu.lhm.room.Room;

import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

        private final TenantRepository tenantRepository;

        private final Roomrepository roomrepository;

        public Tenant createTenant(Long roomId, Tenant tenant){

            Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

            tenant.setRoom(room);
            tenant.setHouse(room.getHouse());
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

    public Tenant bookRoom(Long roomId, Tenant tenantBookRoom){

        Room room = roomrepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

        tenantBookRoom.setRoom(room);
        tenantBookRoom.setHouse(room.getHouse());
        tenantBookRoom.setBookRoom(true);

        return tenantRepository.save(tenantBookRoom);
    }

    public List<Tenant> getListTenantByRoomId(Long id){

            return tenantRepository.findAllByRoom_Id(id);
    }

    public void deleteTenant(Long tenantId) {
        tenantRepository.deleteById(tenantId);
    }








}
