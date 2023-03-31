package com.fu.lhm.tenant.controller;


import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final TenantService tenantRepository;

    @PostMapping("")
    public ResponseEntity<Tenant> createTenant(@PathVariable("roomId") Long roomId, @RequestBody Tenant tenant) {

        return ResponseEntity.ok(tenantService.createTenant(roomId, tenant));
    }

    @PutMapping({"/{tenantId}"})
    public ResponseEntity<Tenant> updateTenant(@PathVariable("tenantId") Long id, @RequestBody Tenant tenant) {

        return ResponseEntity.ok(tenantService.updateTenant(id,tenant));
    }

    @PostMapping({"/bookRoom"})
    public ResponseEntity<Tenant> bookRoom(@PathVariable("roomId") Long id, @RequestBody Tenant tenant) {

        return ResponseEntity.ok(tenantService.bookRoom(id,tenant));
    }

    @GetMapping("")
    public ResponseEntity<List<Tenant>> getListTenantByRoomId(@PathVariable("roomId") Long id){

        return ResponseEntity.ok(tenantService.getListTenantByRoomId(id));
    }


}
