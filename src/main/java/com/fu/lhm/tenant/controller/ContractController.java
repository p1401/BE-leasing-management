package com.fu.lhm.tenant.controller;

import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.model.ContractBookingRequest;
import com.fu.lhm.tenant.model.ContractRequest;
import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.tenant.validate.ContractValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private final ContractValidate contractValidate;

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") Long contractId) {


        return ResponseEntity.ok(contractService.getContractById(contractId));
    }


    @PostMapping("")
    public ResponseEntity<Contract> createContract(@RequestBody ContractRequest contractRequest) {

        contractValidate.validateForCreateContract(contractRequest);

        return ResponseEntity.ok(contractService.createContract(contractRequest));
    }

    @PostMapping("/booking")
    public ResponseEntity<Contract> createContractFromBooking(@RequestParam(name = "roomId") Long roomId,
                                                              @RequestBody ContractBookingRequest contractBookingRequest) {
        return ResponseEntity.ok(contractService.createContractFromBooking(roomId, contractBookingRequest));
    }

    @PutMapping("{contractId}/change-holder")
    public ResponseEntity<Contract> changeHolder(@PathVariable("contractId") Long contractId,
                                                 @RequestParam(name = "oldTenantId") Long oldTenantId,
                                                 @RequestParam(name = "newTenantId") Long newTenantId) {

        return ResponseEntity.ok(contractService.changeHolder(contractId, oldTenantId, newTenantId));
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable("contractId") Long contractId,
                                                   @RequestBody Contract contract) {

        return ResponseEntity.ok(contractService.updateContract(contractId, contract));
    }


}
