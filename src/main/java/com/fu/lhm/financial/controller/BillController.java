package com.fu.lhm.financial.controller;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.service.BillService;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final ContractRepository contractRepository;

    private final BillService billService;

    @PostMapping("")
    public ResponseEntity<List<Bill>> createAllTienPhong(){

        return ResponseEntity.ok(billService.createAllTienPhong());
    }
}
