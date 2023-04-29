package tenant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.tenant.controller.TenantController;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.model.TenantRequest;
import com.fu.lhm.tenant.service.TenantService;
import com.fu.lhm.tenant.validate.TenantValidate;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class TenantControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private TenantService tenantService;
    @Mock
    private TenantValidate tenantValidate;

    @InjectMocks
    private TenantController tenantController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tenantController).build();
    }

    @Test
    public void testCreateTenant() throws Exception {
        // Given
        Room room = new Room();
        Long roomId = 1L;
        House house = new House();

        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setName("test");
        tenantRequest.setPhone("1234567890");
        tenantRequest.setEmail("john.doe@example.com");
        tenantRequest.setBirth(new Date(2002/12/12));
        tenantRequest.setIdentifyNumber("123456789");
        tenantRequest.setAddress("123 Main St");
        tenantRequest.setIsStay(true);
        tenantRequest.setIsContractHolder(false);
        tenantRequest.setRoom(room);
        tenantRequest.setHouse(house);
        // Create a new Tenant object from the TenantRequest
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequest.getName());
        tenant.setPhone(tenantRequest.getPhone());
        tenant.setEmail(tenantRequest.getEmail());
        tenant.setBirth(tenantRequest.getBirth());
        tenant.setIdentifyNumber(tenantRequest.getIdentifyNumber());
        tenant.setAddress(tenantRequest.getAddress());
        tenant.setIsStay(tenantRequest.getIsStay());
        tenant.setIsContractHolder(tenantRequest.getIsContractHolder());
        tenant.setRID(tenantRequest.getRoom().getId());
        tenant.setHID(tenantRequest.getHouse().getId());

        // When
        when(tenantService.createTenant(roomId, tenant)).thenReturn(tenant);

        // Then
        mockMvc.perform(post("/api/v1/tenants")
                        .param("roomId", roomId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tenant)))
                        .andExpect(status().isOk());

        // Verify service method calls
        verify(tenantValidate, times(1)).validateForCreateTenant(roomId, tenant);
        verify(tenantService, times(1)).createTenant(roomId, tenant);
    }

    @Test
    public void testCreateTenant_NullName() throws Exception {
        // Given
        Room room = new Room();
        Long roomId = 1L;
        House house = new House();

        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setName("");
        tenantRequest.setPhone("1234567890");
        tenantRequest.setEmail("john.doe@example.com");
        tenantRequest.setBirth(new Date(2002/12/12));
        tenantRequest.setIdentifyNumber("123456789");
        tenantRequest.setAddress("123 Main St");
        tenantRequest.setIsStay(true);
        tenantRequest.setIsContractHolder(false);
        tenantRequest.setRoom(room);
        tenantRequest.setHouse(house);
        // Create a new Tenant object from the TenantRequest
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequest.getName());
        tenant.setPhone(tenantRequest.getPhone());
        tenant.setEmail(tenantRequest.getEmail());
        tenant.setBirth(tenantRequest.getBirth());
        tenant.setIdentifyNumber(tenantRequest.getIdentifyNumber());
        tenant.setAddress(tenantRequest.getAddress());
        tenant.setIsStay(tenantRequest.getIsStay());
        tenant.setIsContractHolder(tenantRequest.getIsContractHolder());
        tenant.setRID(tenantRequest.getRoom().getId());
        tenant.setHID(tenantRequest.getHouse().getId());

        // Then
        doThrow(new BadRequestException("Vui lòng nhập họ tên")).when(tenantValidate).validateForCreateTenant(anyLong(), any(Tenant.class));

        // Perform
        mockMvc.perform(post("/api/v1/tenants")
                        .param("roomId", roomId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tenant)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(tenantValidate, times(1)).validateForCreateTenant(eq(1L), eq(tenant));
    }

    @Test
    public void testUpdateTenant() throws Exception {
        // Given
        Long tenantId = 1L;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setName("test");
        tenant.setEmail("test@gmail.com");
        tenant.setPhone("1234567890");
        tenant.setAddress("123 test");
        tenant.setIdentifyNumber("123456789");

        Tenant updatedTenant = new Tenant();
        updatedTenant.setId(tenantId);
        updatedTenant.setName("test 1");
        updatedTenant.setEmail("test1@gmail.com");
        updatedTenant.setPhone("987654321");
        updatedTenant.setAddress("321 test");
        updatedTenant.setIdentifyNumber("987654321");

        // Then
        when(tenantService.updateTenant(tenantId, tenant)).thenReturn(updatedTenant);

        // Perform
        mockMvc.perform(put("/api/v1/tenants/" + tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tenant)))
                        .andExpect(status().isOk());

        // Verify
        verify(tenantValidate, times(1)).validateForUpdateTenant(tenant);
        verify(tenantService, times(1)).updateTenant(tenantId, tenant);
    }

    @Test
    public void testUpdateTenant_NotFoundTenant() throws Exception {
        // Given
        Long tenantId = 1L;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        tenant.setName("test");
        tenant.setEmail("test@gmail.com");
        tenant.setPhone("1234567890");
        tenant.setAddress("123 test");
        tenant.setIdentifyNumber("123456789");

        // Then
        doThrow(new BadRequestException("Người thuê không tồn tại!")).when(tenantValidate).validateForUpdateTenant(any(Tenant.class));

        // Perform
        mockMvc.perform(put("/api/v1/tenants/" + tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tenant)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(tenantValidate, times(1)).validateForUpdateTenant(eq(tenant));
    }

    @Test
    public void testGetTenantById() throws Exception {
        // Given
        Long tenantId = 1L;
        TenantRequest tenantRequest = new TenantRequest();
        tenantRequest.setId(tenantId);
        tenantRequest.setName("");
        tenantRequest.setEmail("john.doe@example.com");
        tenantRequest.setPhone("1234567890");
        tenantRequest.setAddress("123 Main St");
        tenantRequest.setIdentifyNumber("123456789");
        // Create a new Tenant object from the TenantRequest
        Tenant tenant = new Tenant();
        tenant.setId(tenantRequest.getId());
        tenant.setName(tenantRequest.getName());
        tenant.setPhone(tenantRequest.getPhone());
        tenant.setEmail(tenantRequest.getEmail());
        tenant.setAddress(tenantRequest.getAddress());
        tenant.setIdentifyNumber(tenantRequest.getIdentifyNumber());

        // When
        when(tenantService.getTenantById(tenantId)).thenReturn(tenant);

        // Perform
        mockMvc.perform(get("/api/v1/tenants/" + tenantId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(tenantId.intValue())))
                        .andExpect(jsonPath("$.name", is(tenant.getName())))
                        .andExpect(jsonPath("$.phone", is(tenant.getPhone())))
                        .andExpect(jsonPath("$.email", is(tenant.getEmail())))
                        .andExpect(jsonPath("$.address", is(tenant.getAddress())))
                        .andExpect(jsonPath("$.identifyNumber", is(tenant.getIdentifyNumber())));

        // Verify
        verify(tenantService, times(1)).getTenantById(tenantId);
    }

    @Test
    public void testGetTenantById_InvalidTenantID() throws Exception {
        Long tenantID = 100L;

        when(tenantService.getTenantById(tenantID)).thenThrow(new BadRequestException("Người thuê không tồn tại!"));

        mockMvc.perform(get("/api/v1/tenants/" + tenantID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(tenantService, times(1)).getTenantById(tenantID);
    }

    @Test
    public void testDeleteTenant() throws Exception {
        Long tenantID = 1L;

        mockMvc.perform(delete("/api/v1/tenants/" + tenantID))
                        .andExpect(status().isOk());

        verify(tenantService, times(1)).deleteTenant(tenantID);
    }

    @Test
    public void testDeleteTenant_InvalidTenantID() throws Exception {
        Long tenantID = 100L;

        doThrow(new BadRequestException("Người thuê không tồn tại!")).when(tenantService).deleteTenant(tenantID);

        mockMvc.perform(delete("/api/v1/tenants/" + tenantID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(tenantService, times(1)).deleteTenant(tenantID);
    }

    @Test
    public void testLeaveRoom() throws Exception {
        Long tenantID = 1L;
        Tenant tenant = new Tenant();
        tenant.setId(tenantID);

        when(tenantService.leaveRoom(tenantID)).thenReturn(tenant);

        mockMvc.perform(put("/api/v1/tenants/leave/" + tenantID))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(tenantID));

        verify(tenantService, times(1)).leaveRoom(tenantID);
    }

    @Test
    public void testLeaveRoom_InvalidTenantID() throws Exception {
        Long tenantID = 100L;
        Tenant tenant = new Tenant();
        tenant.setId(tenantID);

        doThrow(new BadRequestException("Người thuê không tồn tại!")).when(tenantService).leaveRoom(tenantID);

        mockMvc.perform(put("/api/v1/tenants/leave/" + tenantID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(tenantService, times(1)).leaveRoom(tenantID);
    }

    @Test
    public void testGetListTenantByRoomId() throws Exception {
        Long roomID = 1L;

        Page<Tenant> tenants = new PageImpl<>(Collections.singletonList(new Tenant()));
        when(tenantService.getListTenantByRoomId(roomID, PageRequest.of(0, 10))).thenReturn(tenants);

        mockMvc.perform(get("/api/v1/tenants/rooms/" + roomID)
                        .param("page", "0")
                        .param("pageSize", "10"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetListTenantByRoomId_InvalidRoomID() throws Exception {
        Long roomID = 100L;

        when(tenantService.getListTenantByRoomId(roomID, PageRequest.of(0, 10))).thenReturn(null);

        String responseContent = mockMvc.perform(get("/api/v1/tenants/rooms/" + roomID)
                        .param("page", "0")
                        .param("pageSize", "10"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // Assert that the response content is empty
        assertEquals("", responseContent);
    }

    @Test
    public void testGetTenants() throws Exception {
        Long roomID = 1L;
        Long houseID = 2L;

        Page<Tenant> tenants = new PageImpl<>(Collections.singletonList(new Tenant()));
        when(tenantService.getListTenants(houseID, roomID, true, PageRequest.of(0, 10))).thenReturn(tenants);

        mockMvc.perform(get("/api/v1/tenants")
                        .param("houseId", houseID.toString())
                        .param("roomId", roomID.toString())
                        .param("isStay", "1")
                        .param("page", "0")
                        .param("pageSize", "10"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetTenants_Null() throws Exception {
        Long roomID = 1L;
        Long houseID = 2L;

        when(tenantService.getListTenants(houseID, roomID, true, PageRequest.of(0, 10))).thenReturn(null);

        String responseContent = mockMvc.perform(get("/api/v1/tenants")
                        .param("houseId", houseID.toString())
                        .param("roomId", roomID.toString())
                        .param("isStay", "1")
                        .param("page", "0")
                        .param("pageSize", "10"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // Assert that the response content is empty
        assertEquals("", responseContent);
    }
}