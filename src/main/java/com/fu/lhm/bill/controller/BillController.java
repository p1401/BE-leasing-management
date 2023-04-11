package com.fu.lhm.bill.controller;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.modal.BillReceiveRequest;
import com.fu.lhm.bill.modal.BillSpendRequest;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.bill.validate.BillValidate;
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
    public ResponseEntity<List<Bill>> createAllTienPhong() {

        return ResponseEntity.ok(billService.createAllBill());
    }
    @GetMapping("/detail/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable("billId") Long billId) {

        return ResponseEntity.ok(billService.getBillById(billId));
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Bill> createReceiveBill(@PathVariable("roomId") Long roomId, @RequestBody BillReceiveRequest bill) {
        if (bill.getBillContent().name().equalsIgnoreCase("TIENPHONG") && bill.getBillType().name().equalsIgnoreCase("RECEIVE")) {
            billValidate.validateForCreateBillTienPhong(roomId, bill);
        } else if (bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI") && bill.getBillType().name().equalsIgnoreCase("RECEIVE")) {
            billValidate.validateForCreateBillTienPhuTroi(roomId, bill);
        }
        return ResponseEntity.ok(billService.createBillTienPhong(roomId, bill));
    }

    @PostMapping("/spend/{roomId}")
    public ResponseEntity<Bill> createSpendBill(@PathVariable("roomId") Long roomId, @RequestBody BillSpendRequest bill) {
        billValidate.validateforCreateBillSpend(bill);
        return ResponseEntity.ok(billService.createBillSpend(roomId, bill));
    }

    @PutMapping("/pay/{billId}")
    public ResponseEntity<Bill> payBill(@PathVariable("billId") Long billId) {

        return ResponseEntity.ok(billService.payBill(billId));
    }



    @GetMapping("/{roomId}")
    public ResponseEntity<Page<Bill>> getListBillByRoomId(@PathVariable("roomId") Long roomId,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        return ResponseEntity.ok(billService.getListBillByRoomId(roomId, PageRequest.of(page, pageSize)));
    }

    @DeleteMapping ("/{billId}")
    public void deleteBillById(@PathVariable("billId") Long billId) {

         billService.deleteBill(billId);
    }
}

