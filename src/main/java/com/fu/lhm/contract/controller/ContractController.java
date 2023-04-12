package com.fu.lhm.contract.controller;

import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.service.ContractService;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.CreateContractRequest;
import com.fu.lhm.contract.validate.ContractValidate;
import com.fu.lhm.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<Contract> createContract(@RequestBody CreateContractRequest contractRequest) throws BadRequestException {

        contractValidate.validateForCreateContract(contractRequest);

        return ResponseEntity.ok(contractService.createContract(contractRequest));
    }

    @PutMapping("{contractId}/change-holder")
    public ResponseEntity<Contract> changeHolder(@PathVariable("contractId") Long contractId,
                                                 @RequestParam(name = "oldTenantId") Long oldTenantId,
                                                 @RequestParam(name = "newTenantId") Long newTenantId) throws BadRequestException {

        return ResponseEntity.ok(contractService.changeHolder(contractId, oldTenantId, newTenantId));
    }

    @PutMapping("/{contractId}")
    public ResponseEntity<Contract> updateContract(@PathVariable("contractId") Long contractId,
                                                   @RequestBody ContractRequest contract) throws BadRequestException {
        contractValidate.validateForUpdateContract(contractId,contract);
        return ResponseEntity.ok(contractService.updateContract(contractId, contract));
    }

    @PutMapping("/liquidation/{contractId}")
    public ResponseEntity<Contract> liquidationContract(@PathVariable("contractId") Long contractId) throws BadRequestException {

        return ResponseEntity.ok(contractService.liquidationContract(contractId));
    }

    @GetMapping("")
    public ResponseEntity<Page<Contract>> getContracts(@RequestParam(name = "houseId", required = false) Long houseId,
                                                   @RequestParam(name = "roomId", required = false) Long roomId,
                                                  @RequestParam(name = "isActive", required = false, defaultValue = "1") Boolean isActive,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Contract> listContract = contractService.getContracts(houseId, roomId, isActive,PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listContract);
    }


}
