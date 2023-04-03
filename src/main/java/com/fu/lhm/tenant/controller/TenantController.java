package com.fu.lhm.tenant.controller;


import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.tenant.service.TenantService;
import com.fu.lhm.tenant.validate.TenantValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final TenantValidate tenantValidate;

    private final TenantRepository tenantRepository;

    @PostMapping("")
    public ResponseEntity<Tenant> createTenant(@PathVariable("roomId") Long roomId, @RequestBody Tenant tenant) {
        tenantValidate.validateForCreateTenant(roomId,tenant);
        return ResponseEntity.ok(tenantService.createTenant(roomId, tenant));
    }

    @PutMapping({"/{tenantId}"})
    public ResponseEntity<Tenant> updateTenant(@PathVariable("tenantId") Long id, @RequestBody Tenant tenant) {
        tenantValidate.validateForUpdateTenant(tenant);
        return ResponseEntity.ok(tenantService.updateTenant(id,tenant));
    }

    @PostMapping({"/bookRoom"})
    public ResponseEntity<Tenant> bookRoom(@PathVariable("roomId") Long id, @RequestBody Tenant tenant) {

        return ResponseEntity.ok(tenantService.bookRoom(id,tenant));
    }

    @GetMapping("")
    public ResponseEntity<Page<Tenant>> getListTenantByRoomId(@PathVariable("roomId") Long id){

        Page<Tenant> listTenant = (Page<Tenant>) tenantRepository.getReferenceById(id);

        return ResponseEntity.ok(listTenant);
    }
    @GetMapping("/{tenantId}")
    public ResponseEntity<Tenant> getListTenantById(@PathVariable("tenantId") Long id){

        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @DeleteMapping("/{tenantId}")
    public void deleteTenant(@PathVariable("tenantId") Long id){

         tenantService.deleteTenant(id);
    }



}
