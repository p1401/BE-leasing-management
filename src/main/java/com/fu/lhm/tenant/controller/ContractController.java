package com.fu.lhm.tenant.controller;

import com.fu.lhm.tenant.service.ContractService;
import com.fu.lhm.tenant.CreateContractFromBooking;
import com.fu.lhm.tenant.CreateContractRequest;
import com.fu.lhm.tenant.Contract;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("")
    public ResponseEntity<Contract> createContract(@PathVariable("roomId") Long roomId, @RequestBody CreateContractRequest createContractRequest) {

        return ResponseEntity.ok(contractService.createContract(roomId,createContractRequest));
    }

    @PostMapping("/createFromBooking")
    public ResponseEntity<Contract> createContractFromBooking(@PathVariable("roomId") Long roomId, @RequestBody CreateContractFromBooking createContractFromBooking) {
        return ResponseEntity.ok(contractService.createContractFromBooking(roomId,createContractFromBooking));
    }

}
