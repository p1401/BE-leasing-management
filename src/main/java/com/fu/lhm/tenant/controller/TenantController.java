package com.fu.lhm.tenant.controller;


import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.model.TenantRequest;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.tenant.service.TenantService;
import com.fu.lhm.tenant.validate.TenantValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    private final TenantValidate tenantValidate;
    private final TenantRepository tenantRepository;

    @PostMapping("")
    public ResponseEntity<Tenant> createTenant(@RequestParam(name = "roomId") Long roomId, @RequestBody Tenant tenant) throws BadRequestException {
        tenantValidate.validateForCreateTenant(roomId, tenant);
        return ResponseEntity.ok(tenantService.createTenant(roomId, tenant));
    }

    @PutMapping({"/{tenantId}"})
    public ResponseEntity<Tenant> updateTenant(@PathVariable("tenantId") Long id, @RequestBody Tenant tenant) throws BadRequestException {
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

    @PutMapping("/leave/{tenantId}")
    public Tenant leaveRoom(@PathVariable("tenantId") Long id) {

        return tenantService.leaveRoom(id);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<Tenant>> getListTenantByRoomId(@PathVariable("roomId") Long id,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Tenant> listTenant = tenantService.getListTenantByRoomId(id, PageRequest.of(page, pageSize, Sort.by("name")));

        return ResponseEntity.ok(listTenant);
    }

    @GetMapping("")
    public ResponseEntity<Page<Tenant>> getTenants(@RequestParam(name = "houseId", required = false) Long houseId,
                                                   @RequestParam(name = "roomId", required = false) Long roomId,
                                                   @RequestParam(name = "isStay", required = false,defaultValue = "1") Boolean isStay,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Tenant> listTenant = tenantService.getListTenants(houseId, roomId, isStay, PageRequest.of(page, pageSize,Sort.by("name")));

        return ResponseEntity.ok(listTenant);
    }

}
