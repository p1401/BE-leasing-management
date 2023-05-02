package com.fu.lhm.contract.service;

import com.fu.lhm.bill.model.BillReceiveRequest;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.model.CreateContractRequest;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.contract.service.ContractService;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private BillService billService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JwtService jwtService;

    @Before
    public void setUp() throws Exception {
        when(jwtService.getUser(any(HttpServletRequest.class))).thenReturn(new User());
    }

    @Test
    public void getContractById() throws BadRequestException {
        // Arrange
        long contractId = 1L;
        Contract contract = new Contract();
        contract.setId(contractId);
        contract.setContractCode("HĐ123456");
        contract.setFromDate(new Date());
        contract.setToDate(new Date());
        contract.setRoomName("Room A");
        contract.setHouseName("House 1");
        contract.setIsActive(true);
        contract.setDeposit(1000000L);
        contract.setAutoBillDate(1);

        Room room = new Room();
        room.setName("Room 1");
        Tenant tenant = new Tenant();
        tenant.setRoom(room);
        contract.setTenant(tenant);
        when(contractRepository.findById(contractId)).thenReturn(java.util.Optional.of(contract));

        // Act
        ContractRequest result = contractService.getContractById(contractId);

        // Assert
        Assert.assertEquals(contract.getId(), result.getId());
        Assert.assertEquals(contract.getContractCode(), result.getContractCode());
        Assert.assertEquals(contract.getFromDate(), result.getFromDate());
        Assert.assertEquals(contract.getToDate(), result.getToDate());
        Assert.assertEquals(contract.getRoomName(), result.getRoomName());
        Assert.assertEquals(contract.getHouseName(), result.getHouseName());
        Assert.assertEquals(contract.getIsActive(), result.getIsActive());
        Assert.assertEquals(contract.getDeposit(), result.getDeposit());
        Assert.assertEquals(contract.getAutoBillDate(), result.getAutoBillDate());

    }

    @Test
    public void createContract() throws BadRequestException {
        // Mock data
        Tenant tenant = new Tenant();
        tenant.setName("Test Tenant");
        CreateContractRequest contractRequest = new CreateContractRequest();
        contractRequest.setRoomId(1L);
        contractRequest.setFromDate(new Date());
        contractRequest.setToDate(new Date());
        contractRequest.setDeposit(1000L);
        contractRequest.setAutoBillDate(15);
        contractRequest.setTenant(tenant);


        Room room = new Room();
        room.setId(1L);
        room.setName("Test Room");
        House house = new House();
        house.setName("Test House");
        room.setHouse(house);
        room.setCurrentTenant(0);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        // Call the method
        Contract contract = contractService.createContract(contractRequest);

        // Assertions
        Assert.assertNotNull(contract);
        Assert.assertEquals("HĐ", contract.getContractCode().substring(0, 2));
        Assert.assertTrue(contract.getIsActive());
        Assert.assertEquals(contractRequest.getDeposit(), contract.getDeposit());
        Assert.assertEquals(contractRequest.getFromDate(), contract.getFromDate());
        Assert.assertEquals(contractRequest.getToDate(), contract.getToDate());
        Assert.assertEquals(room.getName(), contract.getRoomName());
        Assert.assertEquals(house.getName(), contract.getHouseName());
        Assert.assertEquals(tenant.getName(), contract.getTenantName());
        Assert.assertEquals(contractRequest.getAutoBillDate(), contract.getAutoBillDate());
    }

    @Test
    public void changeHolder() {
    }

    @Test
    public void updateContract() throws BadRequestException {
        // Create a sample ContractRequest object
        ContractRequest newContract = new ContractRequest();
        newContract.setAutoBillDate(15);
        newContract.setDeposit(1000L);
        newContract.setFromDate(new Date());
        newContract.setToDate(new Date());

        // Create a sample Contract object
        Contract oldContract = new Contract();
        oldContract.setId(1L);
        oldContract.setAutoBillDate(15);
        oldContract.setDeposit(500L);
        oldContract.setFromDate(new Date());
        oldContract.setToDate(new Date());

        // Set up mock behavior for findById method
        when(contractRepository.findById(1L)).thenReturn(Optional.of(oldContract));

        // Call the updateContract method
        Contract updatedContract = contractService.updateContract(1L, newContract);

        // Verify that the oldContract object was updated correctly
        Assert.assertEquals(newContract.getAutoBillDate(), oldContract.getAutoBillDate());
        Assert.assertEquals(newContract.getDeposit(), oldContract.getDeposit(), 0.0);
        Assert.assertEquals(newContract.getFromDate(), oldContract.getFromDate());
        Assert.assertEquals(newContract.getToDate(), oldContract.getToDate());

        // Verify that the contractRepository.save method was called with the updated Contract object
        verify(contractRepository).save(oldContract);

        // Verify that the updatedContract object returned by the updateContract method is the same as the oldContract object
        Assert.assertEquals(updatedContract, oldContract);
    }

    @Test
    public void liquidationContract() throws BadRequestException {
        // create a sample contract and tenant
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setIsActive(true);
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        Room room = new Room();
        room.setId(1L);
        room.setCurrentTenant(1);
        tenant.setRoom(room);
        contract.setTenant(tenant);

        // mock the contract and room repositories to return the sample data
        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // call the service method
        Contract result = contractService.liquidationContract(contract.getId());

        // verify that the contract is deactivated and the room has no tenant
        Assert.assertFalse(result.getIsActive());
        Assert.assertEquals(0, room.getCurrentTenant());

    }

    @Test
    public void getContracts() {
        // create test data
        Long houseId = 1L;
        Long roomId = 2L;
        Boolean isActive = true;
        Pageable page = PageRequest.of(0, 10);
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract());
        contracts.add(new Contract());

        // set up mock behavior
        when(contractRepository.findAllByTenant_Room_IdAndIsActive(eq(roomId), eq(isActive), any(Pageable.class)))
                .thenReturn(new PageImpl<>(contracts));
        when(contractRepository.findAllByTenant_Room_House_IdAndIsActive(eq(houseId), eq(isActive), any(Pageable.class)))
                .thenReturn(new PageImpl<>(contracts));

        // test with roomId
        Page<Contract> result = contractService.getContracts(null, roomId, isActive, page);
        Assert.assertEquals(2, result.getContent().size());


        // test with houseId
        result = contractService.getContracts(houseId, null, isActive, page);
        Assert.assertEquals(2, result.getContent().size());


        // test with null parameters
        result = contractService.getContracts(null, null, isActive, page);
        Assert.assertEquals(0, result.getContent().size());

    }
}