package com.fu.lhm.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.contract.controller.ContractController;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.model.CreateContractRequest;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.contract.service.ContractService;
import com.fu.lhm.contract.validate.ContractValidate;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.tenant.entity.Tenant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ContractControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private ContractService contractService;
    @Mock
    private ContractValidate contractValidate;
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractController contractController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contractController).build();
    }

    @Test
    public void testGetContractById() throws Exception {
        //Given
        Long contractId = 1L;

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setId(contractId);
        contractRequest.setContractCode("Test code");
        contractRequest.setRoomName("Test room");
        contractRequest.setHouseName("Test house");
        contractRequest.setFloor(5);
        contractRequest.setArea(25);
        contractRequest.setDeposit(100L);
        contractRequest.setAutoBillDate(2);
        contractRequest.setTenantName("Test name");

        // When
        when(contractService.getContractById(contractId)).thenReturn(contractRequest);

        // Perform
        mockMvc.perform(get("/api/v1/contracts/" + contractId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(contractId.intValue())))
                .andExpect(jsonPath("$.contractCode", is(contractRequest.getContractCode())))
                .andExpect(jsonPath("$.roomName", is(contractRequest.getRoomName())))
                .andExpect(jsonPath("$.houseName", is(contractRequest.getHouseName())))
                .andExpect(jsonPath("$.floor", is(contractRequest.getFloor())))
                .andExpect(jsonPath("$.area", is(contractRequest.getArea())))
                .andExpect(jsonPath("$.deposit", is(contractRequest.getDeposit().intValue())))
                .andExpect(jsonPath("$.autoBillDate", is(contractRequest.getAutoBillDate())))
                .andExpect(jsonPath("$.tenantName", is(contractRequest.getTenantName())));

        // Verify
        verify(contractService, times(1)).getContractById(contractId);
    }

    @Test
    public void testGetContractById_InvalidContractID() throws Exception {
        //Given
        Long contractId = 100L;

        when(contractService.getContractById(contractId)).thenThrow(new BadRequestException("Hợp đồng không tồn tại"));

        mockMvc.perform(get("/api/v1/contracts/" + contractId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(contractService, times(1)).getContractById(contractId);
    }

    @Test
    public void testGetContractByRoomId() throws Exception {
        //Given
        Long roomId = 1L;

        Room room = new Room();
        room.setId(roomId);
        room.setName("Test room");

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setDeposit(100L);
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setRoomId(roomId);

        Contract contract = new Contract();
        contract.setDeposit(createContractRequest.getDeposit());
        contract.setAutoBillDate(createContractRequest.getAutoBillDate());
        contract.setRoomName(room.getName());

        // When
        when(contractService.getContractByRoomId(roomId)).thenReturn(contract);

        // Perform
        mockMvc.perform(get("/api/v1/contracts/rooms/" + roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit", is(contract.getDeposit().intValue())))
                .andExpect(jsonPath("$.autoBillDate", is(contract.getAutoBillDate())))
                .andExpect(jsonPath("$.roomName", is(contract.getRoomName())));

        // Verify
        verify(contractService, times(1)).getContractByRoomId(roomId);
    }

    @Test
    public void testCreateContract() throws Exception {
        //Given
        Long roomId = 0L;

        Room room = new Room();
        room.setId(roomId);

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setFromDate(new Date());
        createContractRequest.setToDate(new Date());
        createContractRequest.setDeposit(100L);
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setRoomId(roomId);

        Contract contract = new Contract();
        contract.setFromDate(createContractRequest.getFromDate());
        contract.setToDate(createContractRequest.getToDate());
        contract.setDeposit(createContractRequest.getDeposit());
        contract.setAutoBillDate(createContractRequest.getAutoBillDate());
        contract.setRoomName(room.getName());

        // When
        when(contractService.createContract(createContractRequest)).thenReturn(contract);

        // Then
        mockMvc.perform(post("/api/v1/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isOk());

        // Verify service method calls
        verify(contractValidate, times(1)).validateForCreateContract(createContractRequest);
        verify(contractService, times(1)).createContract(createContractRequest);
    }

    @Test
    public void testCreateContract_NotFoundRoom() throws Exception {
        //Given
        Long roomId = 0L;

        Room room = new Room();
        room.setId(roomId);

        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setFromDate(new Date());
        createContractRequest.setToDate(new Date());
        createContractRequest.setDeposit(100L);
        createContractRequest.setAutoBillDate(1);
        createContractRequest.setRoomId(roomId);

        // When
        doThrow(new BadRequestException("Phòng không tồn tại!")).when(contractValidate).validateForCreateContract(any());


        // Then
        mockMvc.perform(post("/api/v1/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContractRequest)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(contractValidate, times(1)).validateForCreateContract(eq(createContractRequest));
    }

    @Test
    public void testChangeHolder() throws Exception {
        // Given
        Long contractId = 1L;
        Long oldTenantId = 1L;
        Long newTenantId = 2L;

        Contract contract = new Contract();
        contract.setId(contractId);

        Tenant tenant = new Tenant();
        tenant.setId(oldTenantId);
        tenant.setName("test");
        tenant.setEmail("test@gmail.com");
        tenant.setPhone("1234567890");
        tenant.setAddress("123 test");
        tenant.setIdentifyNumber("123456789");
        tenant.setIsContractHolder(true);

        Tenant newTenant = new Tenant();
        newTenant.setId(newTenantId);
        newTenant.setName("test 1");
        newTenant.setEmail("test1@gmail.com");
        newTenant.setPhone("987654321");
        newTenant.setAddress("321 test");
        newTenant.setIdentifyNumber("987654321");
        newTenant.setIsContractHolder(false);

        // Then
        when(contractService.changeHolder(contractId, oldTenantId, newTenantId)).thenReturn(contract);

        // Perform
        mockMvc.perform(put("/api/v1/contracts/{contractId}/change-holder", contractId)
                        .param("oldTenantId", oldTenantId.toString())
                        .param("newTenantId", newTenantId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isOk());

        // Verify
        verify(contractService, times(1)).changeHolder(contractId, oldTenantId, newTenantId);
    }

    @Test
    public void testChangeHolder_InvalidContractID() throws Exception {
        //Given
        Long contractId = 100L;
        Long oldTenantId = 1L;
        Long newTenantId = 2L;

        Contract contract = new Contract();
        contract.setId(contractId);

        Tenant tenant = new Tenant();
        tenant.setId(oldTenantId);

        Tenant newTenant = new Tenant();
        newTenant.setId(newTenantId);

        when(contractService.changeHolder(contractId, oldTenantId, newTenantId)).thenThrow(new BadRequestException("Hợp đồng không tồn tại"));

        mockMvc.perform(put("/api/v1/contracts/{contractId}/change-holder", contractId)
                        .param("oldTenantId", oldTenantId.toString())
                        .param("newTenantId", newTenantId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(contractService, times(1)).changeHolder(contractId, oldTenantId, newTenantId);
    }

    @Test
    public void testUpdateContract() throws Exception {
        //Given
        Long contractId = 1L;

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setId(contractId);
        contractRequest.setContractCode("Test code");
        contractRequest.setRoomName("Test room");
        contractRequest.setHouseName("Test house");
        contractRequest.setDeposit(100L);
        contractRequest.setAutoBillDate(1);
        contractRequest.setTenantName("Test name");

        Contract contract = new Contract();
        contract.setId(contractRequest.getId());
        contract.setContractCode(contractRequest.getContractCode());
        contract.setRoomName(contractRequest.getRoomName());
        contract.setHouseName(contractRequest.getHouseName());
        contract.setDeposit(contractRequest.getDeposit());
        contract.setAutoBillDate(contractRequest.getAutoBillDate());
        contract.setTenantName(contractRequest.getTenantName());

        Contract updatedContract = new Contract();
        updatedContract.setId(contractId);
        updatedContract.setContractCode("Test code 1");
        updatedContract.setRoomName("Test room 1");
        updatedContract.setHouseName("Test house 1");
        updatedContract.setDeposit(200L);
        updatedContract.setAutoBillDate(2);
        updatedContract.setTenantName("Test name 1");

        // Then
        when(contractService.updateContract(contractId, contractRequest)).thenReturn(updatedContract);

        // Perform
        mockMvc.perform(put("/api/v1/contracts/" + contractId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isOk());

        // Verify
        verify(contractValidate, times(1)).validateForUpdateContract(contractId, contractRequest);
        verify(contractService, times(1)).updateContract(contractId, contractRequest);
    }

    @Test
    public void testUpdateContract_NotFoundContract() throws Exception {
        // Given
        Long contractId = 1L;

        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setId(contractId);
        contractRequest.setContractCode("Test code");
        contractRequest.setRoomName("Test room");
        contractRequest.setHouseName("Test house");
        contractRequest.setDeposit(100L);
        contractRequest.setAutoBillDate(1);
        contractRequest.setTenantName("Test name");

        Contract contract = new Contract();
        contract.setId(contractRequest.getId());
        contract.setContractCode(contractRequest.getContractCode());
        contract.setRoomName(contractRequest.getRoomName());
        contract.setHouseName(contractRequest.getHouseName());
        contract.setDeposit(contractRequest.getDeposit());
        contract.setAutoBillDate(contractRequest.getAutoBillDate());
        contract.setTenantName(contractRequest.getTenantName());

        // Then
        doThrow(new BadRequestException("Hợp đồng không tồn tại!")).when(contractValidate).validateForUpdateContract(anyLong(), any());

        // Perform
        mockMvc.perform(put("/api/v1/contracts/" + contractId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(contractValidate, times(1)).validateForUpdateContract(eq(contractId), eq(contractRequest));
    }

    @Test
    public void testLiquidationContract() throws Exception {
        //Given
        Long contractId = 1L;

        Contract contract = new Contract();
        contract.setId(contractId);
        contract.setContractCode("Test code");
        contract.setRoomName("Test room");
        contract.setHouseName("Test house");
        contract.setDeposit(100L);
        contract.setAutoBillDate(1);
        contract.setTenantName("Test name");

        // Then
        when(contractService.liquidationContract(contractId)).thenReturn(contract);

        // Perform
        mockMvc.perform(put("/api/v1/contracts/liquidation/" + contractId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isOk());

        // Verify
        verify(contractService, times(1)).liquidationContract(contractId);
    }

    @Test
    public void testLiquidationContract_NotFoundContract() throws Exception {
        // Given
        Long contractId = 1L;

        Contract contract = new Contract();
        contract.setId(contractId);
        contract.setContractCode("Test code");
        contract.setRoomName("Test room");
        contract.setHouseName("Test house");
        contract.setDeposit(100L);
        contract.setAutoBillDate(1);
        contract.setTenantName("Test name");

        // Then
        when(contractService.liquidationContract(contractId)).thenThrow(new BadRequestException("Hợp đồng không tồn tại!"));

        // Perform
        mockMvc.perform(put("/api/v1/contracts/liquidation/" + contractId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contract)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(contractService, times(1)).liquidationContract(contractId);
    }

    @Test
    public void testGetContracts() throws Exception {
        // prepare test data
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract());
        contracts.add(new Contract());
        contracts.add(new Contract());

        Page<Contract> pageContracts = new PageImpl<>(contracts);
        when(contractService.getContracts(null, null, true, PageRequest.of(0, 10, Sort.by("toDate").descending())))
                .thenReturn(pageContracts);

        // perform the test
        mockMvc.perform(get("/api/v1/contracts")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0]", is(notNullValue())))
                .andExpect(jsonPath("$.content[1]", is(notNullValue())))
                .andExpect(jsonPath("$.content[2]", is(notNullValue())));
    }

//    @Test
//    public void testGenerateDocContract() throws Exception {
//        // Create a mock Contract object and save it to the repository
//        Contract contract = new Contract();
//        contract.setId(1L);
//        // Set other properties of the contract object
//        contractRepository.save(contract);
//
//        // Create input and output files
//        File inputFile = new File("src/main/resources/contract_template.docx");
//        inputFile.createNewFile();
//        File outputFile = new File("src/main/resources/output.docx");
//        outputFile.createNewFile();
//
//        // Mock the service method to generate the output file
//        doNothing().when(contractService).replaceTextsInWordDocument(anyLong(), eq(inputFile.getAbsolutePath()), eq(outputFile.getAbsolutePath()));
//
//        // Mock the Files.readAllBytes() method to return the output file content
//        byte[] outputBytes = Files.readAllBytes(outputFile.toPath());
//        when(Files.readAllBytes(any())).thenReturn(outputBytes);
//
//        // Perform the GET request to the endpoint
//        MvcResult result = mockMvc.perform(get("/generateDoc/{contractId}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
//                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"CONTRACT_#1.docx\""))
//                .andReturn();
//
//        // Verify that the response contains the expected byte array
//        byte[] responseBytes = result.getResponse().getContentAsByteArray();
//        assertEquals(outputBytes.length, responseBytes.length);
//        for (int i = 0; i < outputBytes.length; i++) {
//            assertEquals(outputBytes[i], responseBytes[i]);
//        }
//    }
}