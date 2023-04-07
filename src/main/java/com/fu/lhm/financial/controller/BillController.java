package com.fu.lhm.financial.controller;

import com.fu.lhm.financial.Bill;
import com.fu.lhm.financial.service.BillService;
import com.fu.lhm.financial.validate.BillValidate;
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
    public ResponseEntity<Bill> createBill(@PathVariable("roomId") Long roomId, @RequestBody Bill bill) {
        if (bill.getBillContent().name().equalsIgnoreCase("TIENPHONG")) {
            billValidate.validateForCreateBillTienPhong(roomId, bill);
        } else if (bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI")) {
            billValidate.validateForCreateBillTienPhuTroi(roomId, bill);
        }


        return ResponseEntity.ok(billService.createBillTienPhong(roomId, bill));
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

