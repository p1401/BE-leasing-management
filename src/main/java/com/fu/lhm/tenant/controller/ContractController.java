package com.fu.lhm.tenant.controller;

import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.validate.ContractValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    private final ContractValidate contractValidate;

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable("contractId") Long contractId){


        return ResponseEntity.ok(contractService.getContractById(contractId));
    }


    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<Contract> createContract(@PathVariable("roomId") Long roomId, @RequestBody CreateContractRequest createContractRequest) throws ParseException {

        contractValidate.validateForCreateContract(roomId,createContractRequest);
        return ResponseEntity.ok(contractService.createContract(roomId,createContractRequest));
    }


    @PutMapping("{contractId}/changeHolder/{oldTenantId}/{newTenantId}")
    public ResponseEntity<Contract> changeHolder(@PathVariable("contractId") Long contractId, @PathVariable("oldTenantId") Long oldTenantId,@PathVariable("newTenantId") Long newTenantId ) {

        return ResponseEntity.ok(contractService.changeHolder(contractId,oldTenantId,newTenantId));
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable("contractId") Long contractId, @RequestBody Contract contract) {

        return ResponseEntity.ok(contractService.updateContract(contractId,contract));
    }


}
