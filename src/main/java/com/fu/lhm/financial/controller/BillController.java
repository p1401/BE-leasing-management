package com.fu.lhm.financial.controller;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.service.BillService;
import com.fu.lhm.financial.validate.BillValidate;
import com.fu.lhm.house.House;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    private final BillValidate billValidate;

    @PostMapping("/createAllbill")
    public ResponseEntity<List<Bill>> createAllTienPhong(){

        return ResponseEntity.ok(billService.createAllBill());
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Bill> createBill(@PathVariable("roomId") Long roomId, @RequestBody Bill bill){

        if(bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")){
            billValidate.validateForCreateBillTienPhong(roomId, bill);
        }else if(bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI")){
            billValidate.validateForCreateBillTienPhuTroi(roomId, bill);
        }



        return ResponseEntity.ok(billService.createBillTienPhong(roomId,bill));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Bill>> getListBillByRoomId(@PathVariable("roomId") Long roomId){

        return ResponseEntity.ok(billService.getListBillByRoomId(roomId));
    }
}

