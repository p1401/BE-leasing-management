package com.fu.lhm.bill.controller;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.model.BillReceiveRequest;
import com.fu.lhm.bill.model.BillRequest;
import com.fu.lhm.bill.model.BillRequest2;
import com.fu.lhm.bill.model.BillSpendRequest;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.bill.validate.BillValidate;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    private final BillValidate billValidate;

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }


    @GetMapping("/detail/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable("billId") Long billId) throws BadRequestException {

        return ResponseEntity.ok(billService.getBillById(billId));
    }
    
    @PostMapping("")
    public ResponseEntity<Bill> createBill(@RequestParam(name = "houseId") Long houseId,
                                                   @RequestParam(name = "roomId", required = false) Long roomId,
                                                   @RequestBody BillReceiveRequest bill) throws BadRequestException {
        billValidate.validateForCreateBill(houseId,bill);
        return ResponseEntity.ok(billService.createBillReceive2(getUserToken(),houseId,roomId, bill));
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Bill> createReceiveBill(@PathVariable("roomId") Long roomId, @RequestBody BillReceiveRequest bill) throws BadRequestException {
        if (bill.getBillContent().name().equalsIgnoreCase("TIENPHONG") && bill.getBillType().name().equalsIgnoreCase("RECEIVE")) {
            billValidate.validateForCreateBillTienPhong(roomId, bill);
        } else if (bill.getBillContent().name().equalsIgnoreCase("TIENPHUTROI") && bill.getBillType().name().equalsIgnoreCase("RECEIVE")) {
            billValidate.validateForCreateBillTienPhuTroi(bill);
        }
        return ResponseEntity.ok(billService.createBillReceive(getUserToken(),roomId, bill));
    }

    @PostMapping("/spend/{roomId}")
    public ResponseEntity<Bill> createSpendBill(@PathVariable("roomId") Long roomId, @RequestBody BillSpendRequest bill) throws BadRequestException {
        billValidate.validateforCreateBillSpend(bill);
        return ResponseEntity.ok(billService.createBillSpend(getUserToken(),roomId, bill));
    }

    @PutMapping("/pay/{billId}")
    public ResponseEntity<Bill> payBill(@PathVariable("billId") Long billId) throws BadRequestException {

        return ResponseEntity.ok(billService.payBill(billId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Page<Bill>> getListBillByRoomId(@PathVariable("roomId") Long roomId,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        return ResponseEntity.ok(billService.getListBillByRoomId(roomId, PageRequest.of(page, pageSize, Sort.by("dateCreate").descending())));
    }

    @DeleteMapping ("/{billId}")
    public void deleteBillById(@PathVariable("billId") Long billId) {

         billService.deleteBill(billId);
    }

    @GetMapping("")
    public ResponseEntity<BillRequest> getBills(@RequestParam(name = "houseId", required = false) Long houseId,
                                                      @RequestParam(name = "roomId", required = false) Long roomId,
                                                      @RequestParam(name = "fromDate", required = false) Date fromDate,
                                                      @RequestParam(name = "toDate", required = false) Date toDate,
                                                      @RequestParam(name = "billType", required = false) String billType,
                                                      @RequestParam(name = "isPay", required = false) Boolean isPay,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        BillRequest billRequest =  billService.getBills(getUserToken().getId(),houseId, roomId,fromDate,toDate,billType,isPay, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(billRequest);
    }

    @GetMapping("/reportRevenue")
    public ResponseEntity<BillRequest2> getBills2(@RequestParam(name = "houseId", required = false) Long houseId,
                                                @RequestParam(name = "roomId", required = false) Long roomId,
                                                @RequestParam(name = "fromDate", required = false) Date fromDate,
                                                @RequestParam(name = "toDate", required = false) Date toDate,
                                                @RequestParam(name = "billType", required = false) String billType,
                                                @RequestParam(name = "billContent", required = false) String billContent,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) throws BadRequestException {
        BillRequest2 billRequest =  billService.getBills2(getUserToken().getId(),houseId, roomId,fromDate,toDate,billType,billContent, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(billRequest);
    }

    @GetMapping(value = "/generateExcel")
    public ResponseEntity<InputStreamResource> generateBillsExcel() throws BadRequestException, IOException {
        Long userId = getUserToken().getId();

        List<Bill> bills = billService.getAllBill(userId);
        ByteArrayInputStream in = billService.generateExcel(userId, bills);
        // return IO ByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        // set filename in header
        headers.add("Content-Disposition", "attachment; filename=Bills.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }
}

