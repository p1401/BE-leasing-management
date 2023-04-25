package com.fu.lhm.bill.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.model.BillReceiveRequest;
import com.fu.lhm.bill.model.BillRequest;
import com.fu.lhm.bill.model.BillSpendRequest;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private BillService billService;

    @Test
    public void testcCreateBillReceive2() throws BadRequestException {
        User user = new User();
        user.setId(1L);

        Long houseId = 1L;
        Long roomId = 1L;

        BillReceiveRequest billRequest = BillReceiveRequest.builder()
                .roomMoney(1000)
                .chiSoDauDien(50)
                .chiSoDauNuoc(20)
                .chiSoCuoiDien(70)
                .chiSoCuoiNuoc(30)
                .electricNumber(20)
                .waterNumber(10)
                .electricMoney(2000)
                .waterMoney(1000)
                .payer("John Doe")
                .isPay(false)
                .dateCreate(new Date())
                .description("Monthly bill")
                .totalMoney(3000)
                .billType(BillType.RECEIVE)
                .billContent(BillContent.TIENPHONG)
                .build();

        Bill bill = new Bill();

        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill result = billService.createBillReceive2(user, houseId, roomId, billRequest);

        assertEquals(bill.getId(), result.getId());
        assertEquals(bill.getRoomMoney(), result.getRoomMoney());
        assertEquals(bill.getElectricNumber(), result.getElectricNumber());
        assertEquals(bill.getWaterNumber(), result.getWaterNumber());

        verify(billRepository, times(1)).save(any(Bill.class));



    }

    @Test
    public void createBillReceive() throws BadRequestException {
        // create a mock Room entity
        House house = new House();
        house.setId(1L);
        Room room = new Room();
        room.setId(1L);
        room.setName("101");
        room.setHouse(house);
        when(roomRepository.findById(anyLong())).thenReturn(Optional.of(room));

        // create a mock Contract entity
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("John Doe");
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setTenant(tenant);
        contract.setTenantName(tenant.getName());
        contract.setIsActive(true);
        contract.setRoomName(room.getName());
        when(contractRepository.findByTenant_Room_IdAndIsActiveTrue(anyLong())).thenReturn(contract);

        // create a mock Bill entity
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setPayer(contract.getTenantName());
        bill.setContract(contract);
        bill.setRoomId(room.getId());
        bill.setHouseId(room.getHouse().getId());
        bill.setUserId(1L);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        // create a BillReceiveRequest object
        BillReceiveRequest billRequest = BillReceiveRequest.builder()
                .chiSoDauDien(100)
                .chiSoDauNuoc(50)
                .chiSoCuoiDien(200)
                .chiSoCuoiNuoc(100)
                .dateCreate(new Date())
                .electricMoney(500000)
                .electricNumber(100)
                .waterMoney(300000)
                .waterNumber(50)
                .description("test")
                .roomMoney(2000000)
                .build();

        // call the createBillReceive method
        User user = new User();
        user.setId(1L);
        Bill result = billService.createBillReceive(user, room.getId(), billRequest);

        // assert that the result has the expected values
        assertEquals(bill.getId(), result.getId());
        assertEquals(bill.getPayer(), result.getPayer());
        assertEquals(bill.getContract(), result.getContract());
        assertEquals(bill.getRoomId(), result.getRoomId());
        assertEquals(bill.getHouseId(), result.getHouseId());
        assertEquals(bill.getUserId(), result.getUserId());
    }


    @Test
    public void createBillSpend() throws BadRequestException {
        // Mock data
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setName("101");
        room.setHouse(house);

        BillSpendRequest billRequest = BillSpendRequest.builder()
                .roomMoney(100000)
                .totalMoney(50000)
                .description("Test Bill Spend")
                .billType(BillType.SPEND)
                .dateCreate(new Date())
                .build();

        // Mock repository calls
        Mockito.when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(billRepository.save(Mockito.any(Bill.class))).thenAnswer(i -> i.getArguments()[0]);

        // Call service method
        Bill bill = billService.createBillSpend(user, room.getId(), billRequest);

        // Verify repository calls
        Mockito.verify(roomRepository, Mockito.times(1)).findById(room.getId());
        Mockito.verify(billRepository, Mockito.times(1)).save(Mockito.any(Bill.class));

        // Verify returned data
        assertNotNull(bill);
        assertEquals(user.getId(), bill.getUserId());
        assertEquals(room.getId(), bill.getRoomId());
        assertEquals(house.getId(), bill.getHouseId());
        assertEquals(BillContent.TIENPHUTROI, bill.getBillContent());
        assertEquals(BillType.SPEND, bill.getBillType());
        assertNotNull(bill.getBillCode());
        assertTrue(bill.getIsPay());
        assertEquals(billRequest.getTotalMoney(), bill.getTotalMoney());
        assertEquals(billRequest.getDescription(), bill.getDescription());
    }


    @Test
    public void getListBillByRoomId() {
        Long roomId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<Bill> billList = new ArrayList<>();
        Bill bill1 = Bill.builder()
                .billCode("PT2")
                .roomMoney(1000)
                .electricNumber(50)
                .waterNumber(20)
                .payer("John Doe")
                .isPay(false)
                .dateCreate(LocalDate.now())
                .description("Monthly bill")
                .totalMoney(1200)
                .roomId(1L)
                .houseId(1L)
                .userId(1L)
                .billType(BillType.RECEIVE)
                .billContent(BillContent.TIENPHONG)
                .build();
        billList.add(bill1);

        Bill bill2 = Bill.builder()
                .billCode("PT1")
                .roomMoney(1000)
                .electricNumber(50)
                .waterNumber(20)
                .payer("John Doe")
                .isPay(false)
                .dateCreate(LocalDate.now())
                .description("Monthly bill")
                .totalMoney(1200)
                .roomId(1L)
                .houseId(1L)
                .userId(1L)
                .billType(BillType.RECEIVE)
                .billContent(BillContent.TIENPHONG)
                .build();
        billList.add(bill2);

        Page<Bill> billPage = new PageImpl<>(billList);

        when(billRepository.findAllByRoomId(roomId, pageable)).thenReturn(billPage);

        Page<Bill> result = billService.getListBillByRoomId(roomId, pageable);

        assertEquals(result.getContent().size(), billList.size());
        assertEquals(result.getContent().get(0).getId(), bill1.getId());
        assertEquals(result.getContent().get(0).getRoomId(), bill1.getRoomId());
        assertEquals(result.getContent().get(1).getId(), bill2.getId());
        assertEquals(result.getContent().get(1).getRoomId(), bill2.getRoomId());
    }

    @Test
    public void deleteBill() {
        // Tạo mới một Bill
        Bill bill = Bill.builder()
                .billCode("ABC123")
                .roomMoney(1000)
                .electricNumber(100)
                .waterNumber(200)
                .electricMoney(10000)
                .waterMoney(20000)
                .payer("John")
                .isPay(false)
                .dateCreate(LocalDate.now())
                .description("Test Bill")
                .totalMoney(30000)
                .roomId(1L)
                .houseId(1L)
                .userId(1L)
                .billType(BillType.RECEIVE)
                .billContent(BillContent.TIENPHONG)
                .build();

        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill savedBill = billRepository.save(bill);

        // Gọi hàm deleteBill() với billId của bill vừa tạo
        billService.deleteBill(savedBill.getId());

        // Kiểm tra xem bill còn tồn tại trong database hay không
        Optional<Bill> deletedBill = billRepository.findById(savedBill.getId());
        assertFalse(deletedBill.isPresent());
    }


    @Test
    public void testGetBillById() throws BadRequestException {
        // Tạo một đối tượng Bill để trả về khi gọi phương thức findById()
        Bill expectedBill = Bill.builder()
                .id(1L)
                .billCode("ABC123")
                .roomMoney(1000)
                .electricNumber(100)
                .waterNumber(200)
                .electricMoney(10000)
                .waterMoney(20000)
                .payer("John")
                .isPay(false)
                .dateCreate(LocalDate.now())
                .description("Test Bill")
                .totalMoney(30000)
                .roomId(1L)
                .houseId(1L)
                .userId(1L)
                .billType(BillType.RECEIVE)
                .billContent(BillContent.TIENPHONG)
                .build();

        // Mock phương thức findById() của repository để trả về đối tượng Bill tạo ở trên
        when(billRepository.findById(1L)).thenReturn(Optional.of(expectedBill));

        // Gọi phương thức getBillById() của service
        Bill actualBill = billService.getBillById(1L);

        // Kiểm tra xem đối tượng Bill trả về có đúng với đối tượng Bill tạo ở trên hay không
        assertEquals(expectedBill, actualBill);
    }
}