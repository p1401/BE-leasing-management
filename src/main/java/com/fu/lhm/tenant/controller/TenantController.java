package com.fu.lhm.tenant.controller;


import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.service.TenantService;
import com.fu.lhm.tenant.validate.TenantValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final TenantValidate tenantValidate;

    @PostMapping("")
    public ResponseEntity<Tenant> createTenant(@RequestParam(name = "roomId") Long roomId, @RequestBody Tenant tenant) {
        tenantValidate.validateForCreateTenant(roomId, tenant);
        return ResponseEntity.ok(tenantService.createTenant(roomId, tenant));
    }

    @PutMapping({"/{tenantId}"})
    public ResponseEntity<Tenant> updateTenant(@PathVariable("tenantId") Long id, @RequestBody Tenant tenant) {
        tenantValidate.validateForUpdateTenant(tenant);
        return ResponseEntity.ok(tenantService.updateTenant(id, tenant));
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable("tenantId") Long id) {

        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @DeleteMapping("/{tenantId}")
    public void deleteTenant(@PathVariable("tenantId") Long id) {

        tenantService.deleteTenant(id);
    }


    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<Tenant>> getListTenantByRoomId(@PathVariable("roomId") Long id,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Tenant> listTenant = tenantService.getListTenantByRoomId(id, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listTenant);
    }

    @GetMapping("/houses/{houseId}")
    public ResponseEntity<Page<Tenant>> getListTenantByHouseId(@PathVariable("houseId") Long id,
                                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Tenant> listTenant = tenantService.getListTenantByHouseId(id, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listTenant);
    }

    @GetMapping("")
    public ResponseEntity<Page<Tenant>> getTenants(@RequestParam(name = "houseId", required = false) Long houseId,
                                                   @RequestParam(name = "roomId", required = false) Long roomId,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Tenant> listTenant = tenantService.getTenants(houseId, roomId, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listTenant);
    }

}
