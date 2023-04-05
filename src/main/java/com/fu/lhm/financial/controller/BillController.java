package com.fu.lhm.financial.controller;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.service.BillService;
import com.fu.lhm.financial.validate.BillValidate;
import com.fu.lhm.house.House;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @PostMapping("/receive/{roomId}")
    public ResponseEntity<Bill> createBill(@PathVariable("roomId") Long roomId, @RequestBody Bill bill){

        if(bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")){
            billValidate.validateForCreateBillTienPhong(roomId, bill);
        }else if(bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI")){
            billValidate.validateForCreateBillTienPhuTroi(roomId, bill);
        }
        return ResponseEntity.ok(billService.createBillTienPhong(roomId,bill));
    }

    @PutMapping("/pay/{billId}")
    public ResponseEntity<Bill> payBill(@PathVariable("billId") Long billId){

        return ResponseEntity.ok(billService.payBill(billId));
    }

    @GetMapping ("/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable("billId") Long billId){

        return ResponseEntity.ok(billService.getBillById(billId));
    }

    @DeleteMapping ("/{billId}")
    public void deleteBill(@PathVariable("billId") Long billId){
        billService.deleteBill(billId);
    }



    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<Bill>> getListBillByRoomId(@PathVariable("roomId") Long roomId,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        return ResponseEntity.ok(billService.getListBillByRoomId(roomId, PageRequest.of(page, pageSize)));
    }

    @GetMapping("/rooms/{roomId}/notPay")
    public ResponseEntity<Page<Bill>> getListBillByRoomIdNotPay(@PathVariable("roomId") Long roomId,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        return ResponseEntity.ok(billService.getListBillByRoomIdNotPay(roomId, PageRequest.of(page, pageSize)));
    }

    @GetMapping("/houses/{houseId}")
    public ResponseEntity<Page<Bill>> getListBillByHouseId(@PathVariable("houseId") Long roomId,
                                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        return ResponseEntity.ok(billService.getListBillByHouseId(roomId, PageRequest.of(page, pageSize)));
    }
}

