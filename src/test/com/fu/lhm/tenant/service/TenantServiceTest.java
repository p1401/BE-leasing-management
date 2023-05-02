package com.fu.lhm.tenant.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import com.fu.lhm.tenant.service.TenantService;
import com.fu.lhm.tenant.validate.TenantValidate;
import com.fu.lhm.user.entity.User;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {
    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private TenantService tenantService;

    @InjectMocks
    private TenantValidate tenantValidate;

    @Test
    public void testCreateTenant_1() {
        // Given
        House house = new House();
        house.setName("Nhà trọ 1");
        Long roomId = 1L;
        Room room = new Room();
        room.setId(roomId);
        room.setCurrentTenant(1);
        room.setHouse(house);
        Tenant tenant = Tenant.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123456789")
                .identifyNumber("0123456789")
                .birth(new Date())
                .address("123 Main St")
                .isStay(true)
                .isContractHolder(false)
                .roomName("Room 101")
                .houseName("House A")
                .build();

        // When
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);


        Tenant result = tenantService.createTenant(roomId, tenant);

        //Assert
        Assert.assertEquals(result.getName(), tenant.getName());
        Assert.assertEquals(result.getEmail(), tenant.getEmail());
        Assert.assertEquals(result.getPhone(), tenant.getPhone());
        Assert.assertEquals(result.getIdentifyNumber(), tenant.getIdentifyNumber());
        Assert.assertEquals(result.getBirth(), tenant.getBirth());
        Assert.assertEquals(result.getAddress(), tenant.getAddress());
        Assert.assertEquals(result.getIsStay(), tenant.getIsStay());
        Assert.assertEquals(result.getIsContractHolder(), tenant.getIsContractHolder());
        Assert.assertEquals(result.getRoomName(), tenant.getRoomName());
        Assert.assertEquals(result.getHouseName(), tenant.getHouseName());
        Assert.assertEquals(result.getRoom(), room);
        Assert.assertEquals(room.getCurrentTenant(), 2);
    }

    @Test
    public void testCreateTenant_2() {
        // Given
        House house = new House();
        house.setName("Nhà trọ 2");
        Long roomId = 2L;
        Room room = new Room();
        room.setId(roomId);
        room.setCurrentTenant(1);
        room.setHouse(house);
        Tenant tenant = Tenant.builder()
                .name("Hello")
                .email("john.doe@example.com")
                .phone("123456789")
                .identifyNumber("0123456789")
                .birth(new Date())
                .address("123 Main St")
                .isStay(true)
                .isContractHolder(false)
                .roomName("Room 101")
                .houseName("House A")
                .build();

        // When
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);
        Tenant result = tenantService.createTenant(roomId, tenant);

        // Assert
        Assert.assertEquals(result.getName(), tenant.getName());
        Assert.assertEquals(result.getEmail(), tenant.getEmail());
        Assert.assertEquals(result.getPhone(), tenant.getPhone());
        Assert.assertEquals(result.getIdentifyNumber(), tenant.getIdentifyNumber());
        Assert.assertEquals(result.getBirth(), tenant.getBirth());
        Assert.assertEquals(result.getAddress(), tenant.getAddress());
        Assert.assertEquals(result.getIsStay(), tenant.getIsStay());
        Assert.assertEquals(result.getIsContractHolder(), tenant.getIsContractHolder());
        Assert.assertEquals(result.getRoomName(), tenant.getRoomName());
        Assert.assertEquals(result.getHouseName(), tenant.getHouseName());
        Assert.assertEquals(result.getRoom(), room);
        Assert.assertEquals(room.getCurrentTenant(), 2);
    }

    @Test
    public void testCreateTenant_3() {
        // Given
        House house = new House();
        house.setName("Nhà trọ Lan Anh");
        Long roomId = 3L;
        Room room = new Room();
        room.setId(roomId);
        room.setCurrentTenant(1);
        room.setHouse(house);
        Tenant tenant = Tenant.builder()
                .name("Tenant Test")
                .email("john.doe@example.com")
                .phone("123456789")
                .identifyNumber("0123456789")
                .birth(new Date())
                .address("123 Main St")
                .isStay(true)
                .isContractHolder(false)
                .roomName("Room 101")
                .houseName("House A")
                .build();

        // When
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(tenant);

        Tenant result = tenantService.createTenant(roomId, tenant);

        // Assert
        Assert.assertEquals(result.getName(), tenant.getName());
        Assert.assertEquals(result.getEmail(), tenant.getEmail());
        Assert.assertEquals(result.getPhone(), tenant.getPhone());
        Assert.assertEquals(result.getIdentifyNumber(), tenant.getIdentifyNumber());
        Assert.assertEquals(result.getBirth(), tenant.getBirth());
        Assert.assertEquals(result.getAddress(), tenant.getAddress());
        Assert.assertEquals(result.getIsStay(), tenant.getIsStay());
        Assert.assertEquals(result.getIsContractHolder(), tenant.getIsContractHolder());
        Assert.assertEquals(result.getRoomName(), tenant.getRoomName());
        Assert.assertEquals(result.getHouseName(), tenant.getHouseName());
        Assert.assertEquals(result.getRoom(), room);
        Assert.assertEquals(room.getCurrentTenant(), 2);
    }

    @Test()
    public void testCreateTenant_InvalidData_Name() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenant = Tenant.builder()
                .name("")
                .email("john@example.com")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("123 Main St, Anytown USA")
                .isStay(true)
                .build();

        //when
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForCreateTenant(1L,tenant);
        });
        Assert.assertEquals("Vui lòng nhập họ tên", exception.getMessage());
    }

    @Test()
    public void testCreateTenant_InvalidData_Phone() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenant = Tenant.builder()
                .name("tét")
                .email("john@example.com")
                .phone("ádc")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("123 Main St, Anytown USA")
                .isStay(true)
                .build();

        //when
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForCreateTenant(1L,tenant);
        });
        Assert.assertEquals("Số điện thoại không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testCreateTenant_InvalidData_Email() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenant = Tenant.builder()
                .name("tét")
                .email("john@examcom")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("123 Main St, Anytown USA")
                .isStay(true)
                .build();

        //when
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForCreateTenant(1L,tenant);
        });
        Assert.assertEquals("Email không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testCreateTenant_InvalidData_Address() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenant = Tenant.builder()
                .name("tét")
                .email("john@example.com")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("")
                .isStay(true)
                .build();

        //when
        when(roomRepository.findById(1L)).thenReturn(java.util.Optional.of(room));
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForCreateTenant(1L,tenant);
        });
        Assert.assertEquals("Vui lòng nhập địa chỉ", exception.getMessage());
    }

    @Test
    public void testUpdateTenant_1() {
        // given
        Tenant oldTenant = new Tenant();
        oldTenant.setId(1L);

        Tenant newTenant = new Tenant();
        newTenant.setName("John Smith");
        newTenant.setEmail("john@example.com");
        newTenant.setAddress("123 Main Street");
        newTenant.setPhone("555-1234");
        newTenant.setBirth(new Date());
        newTenant.setIdentifyNumber("123456789");

        //when
        when(tenantRepository.findById(1L)).thenReturn(Optional.of(oldTenant));
        when(tenantRepository.save(oldTenant)).thenReturn(oldTenant);
        Tenant updatedTenant = tenantService.updateTenant(1L, newTenant);

        // assert
        Assert.assertEquals(newTenant.getName(), updatedTenant.getName());
        Assert.assertEquals(newTenant.getEmail(), updatedTenant.getEmail());
        Assert.assertEquals(newTenant.getAddress(), updatedTenant.getAddress());
        Assert.assertEquals(newTenant.getPhone(), updatedTenant.getPhone());
        Assert.assertEquals(newTenant.getBirth(), updatedTenant.getBirth());
        Assert.assertEquals(newTenant.getIdentifyNumber(), updatedTenant.getIdentifyNumber());
    }

    @Test()
    public void testUpdateTenant_InvalidData_Name() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenantUpdate = Tenant.builder()
                .name("")
                .email("john@example.com")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("test")
                .isStay(true)
                .build();

        //when

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForUpdateTenant(tenantUpdate);
        });
        Assert.assertEquals("Vui lòng nhập họ tên", exception.getMessage());
    }

    @Test()
    public void testUpdateTenant_InvalidData_Email() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenantUpdate = Tenant.builder()
                .name("test")
                .email("john@examplecom")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("test")
                .isStay(true)
                .build();

        //when

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForUpdateTenant(tenantUpdate);
        });
        Assert.assertEquals("Email không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testUpdateTenant_InvalidData_Phone() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenantUpdate = Tenant.builder()
                .name("test")
                .email("john@example.com")
                .phone("ád")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("test")
                .isStay(true)
                .build();

        //when

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForUpdateTenant(tenantUpdate);
        });
        Assert.assertEquals("Số điện thoại không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testUpdateTenant_InvalidData_Address() {
        // given
        User user = new User();
        user.setId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setMaxTenant(2);

        Tenant tenantUpdate = Tenant.builder()
                .name("test")
                .email("john@example.com")
                .phone("1234567890")
                .identifyNumber("ABC123")
                .birth(new Date())
                .address("")
                .isStay(true)
                .build();

        //when

        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            tenantValidate.validateForUpdateTenant(tenantUpdate);
        });
        Assert.assertEquals("Vui lòng nhập địa chỉ", exception.getMessage());
    }

    @Test
    public void testUpdateTenant_2() {
        // given
        Tenant oldTenant = new Tenant();
        oldTenant.setId(2L);

        Tenant newTenant = new Tenant();
        newTenant.setName("John Smith");
        newTenant.setEmail("john@example.com");
        newTenant.setAddress("123 Main Street");
        newTenant.setPhone("555-1234");
        newTenant.setBirth(new Date());
        newTenant.setIdentifyNumber("123456789");

        //when
        when(tenantRepository.findById(2L)).thenReturn(Optional.of(oldTenant));
        when(tenantRepository.save(oldTenant)).thenReturn(oldTenant);
        Tenant updatedTenant = tenantService.updateTenant(2L, newTenant);


        // assert
        Assert.assertEquals(newTenant.getName(), updatedTenant.getName());
        Assert.assertEquals(newTenant.getEmail(), updatedTenant.getEmail());
        Assert.assertEquals(newTenant.getAddress(), updatedTenant.getAddress());
        Assert.assertEquals(newTenant.getPhone(), updatedTenant.getPhone());
        Assert.assertEquals(newTenant.getBirth(), updatedTenant.getBirth());
        Assert.assertEquals(newTenant.getIdentifyNumber(), updatedTenant.getIdentifyNumber());
    }

    @Test
    public void testUpdateTenant_3() {
        // given
        Tenant oldTenant = new Tenant();
        oldTenant.setId(3L);

        Tenant newTenant = new Tenant();
        newTenant.setName("Hoang 2");
        newTenant.setEmail("ngominhhoang@gmail.com");
        newTenant.setAddress("123 Main Street");
        newTenant.setPhone("0905551234");
        newTenant.setBirth(new Date());
        newTenant.setIdentifyNumber("123456789");

        //when
        when(tenantRepository.findById(3L)).thenReturn(Optional.of(oldTenant));
        when(tenantRepository.save(oldTenant)).thenReturn(oldTenant);
        Tenant updatedTenant = tenantService.updateTenant(3L, newTenant);


        // assert
        Assert.assertEquals(newTenant.getName(), updatedTenant.getName());
        Assert.assertEquals(newTenant.getEmail(), updatedTenant.getEmail());
        Assert.assertEquals(newTenant.getAddress(), updatedTenant.getAddress());
        Assert.assertEquals(newTenant.getPhone(), updatedTenant.getPhone());
        Assert.assertEquals(newTenant.getBirth(), updatedTenant.getBirth());
        Assert.assertEquals(newTenant.getIdentifyNumber(), updatedTenant.getIdentifyNumber());
    }

    @Test
    public void testGetListTenantByRoomId() {
        // Setup
        Long roomId = 1L;
        Pageable page = PageRequest.of(0, 10);
        List<Tenant> tenants = new ArrayList<>();
        Tenant tenant = new Tenant();
        tenant.setName("x");
        tenant.setIsStay(true);
        tenants.add(tenant);
        Page<Tenant> expectedPage = new PageImpl<>(tenants, page, tenants.size());
        when(tenantRepository.findAllByRoom_IdAndIsStayAndNameContainingIgnoreCase(roomId, true,"", page)).thenReturn(expectedPage);

        // Execution
        Page<Tenant> actualPage = tenantService.getListTenantByRoomId(roomId, page);

        // Verification
        assertEquals(expectedPage, actualPage);
    }


    @Test
    public void testGetListTenants() {
        // Create test tenants
        Tenant tenant1 = new Tenant();
        tenant1.setName("John");
        tenant1.setIsStay(true);

        Tenant tenant2 = new Tenant();
        tenant2.setName("Mary");
        tenant2.setIsStay(true);

        Tenant tenant3 = new Tenant();
        tenant3.setName("Bob");
        tenant3.setIsStay(false);

        // Create test rooms
        Room room1 = new Room();
        room1.setName("Room1");
        room1.setCurrentTenant(2);
        room1.setHouse(new House());
        room1.setId(1L);

        Room room2 = new Room();
        room2.setName("Room2");
        room2.setCurrentTenant(1);
        room2.setHouse(new House());
        room2.setId(2L);

        // Assign rooms to tenants
        tenant1.setRoom(room1);
        tenant2.setRoom(room1);
        tenant3.setRoom(room2);

        // Mock the tenantRepository
        Pageable pageable = PageRequest.of(0, 10);
        when(tenantRepository.findAllByRoom_IdAndIsStayAndNameContainingIgnoreCase(eq(1L), eq(true),"", eq(pageable)))
                .thenReturn(new PageImpl<>(Arrays.asList(tenant1, tenant2), pageable, 2));

        when(tenantRepository.findAllByRoom_House_IdAndIsStayAndNameContainingIgnoreCase(eq(2L), eq(false),"", eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(tenant3), pageable, 1));

        // Call the getListTenants method and verify the result
        Page<Tenant> tenants1 = tenantService.getListTenants(null, 1L, true,"", pageable);
        assertEquals(2, tenants1.getTotalElements());

        Page<Tenant> tenants2 = tenantService.getListTenants(2L, null, false,"", pageable);
        assertEquals(1, tenants2.getTotalElements());
        assertEquals("Bob", tenants2.getContent().get(0).getName());
    }

    @Test
    public void testGetTenantById() throws BadRequestException {
        // Create a new tenant
        Tenant tenant = new Tenant();
        tenant.setName("John Doe");
        tenant.setEmail("john.doe@example.com");
        tenant.setPhone("1234567890");
        tenant.setIsContractHolder(true);
        tenant.setIsStay(true);
        tenantRepository.save(tenant);

        // Mock the entity manager to return the tenant when findById is called
        when(tenantRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));

        // Call the getTenantById method
        Tenant retrievedTenant = tenantService.getTenantById(tenant.getId());

        // Assert that the retrieved tenant matches the expected tenant
        assertEquals(tenant.getId(), retrievedTenant.getId());
        assertEquals(tenant.getName(), retrievedTenant.getName());
        assertEquals(tenant.getEmail(), retrievedTenant.getEmail());
        assertEquals(tenant.getPhone(), retrievedTenant.getPhone());
        assertTrue(retrievedTenant.getIsContractHolder());
        assertTrue(retrievedTenant.getIsStay());
    }

    @Test
    public void leaveRoom() throws BadRequestException {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("John Doe");
        tenant.setIsStay(true);
        Room room = new Room();
        room.setId(2L);
        room.setName("Room 1");
        room.setHouse(new House());
        room.setCurrentTenant(1);
        tenant.setRoom(room);

        when(tenantRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(tenantRepository.save(any(Tenant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Tenant result = tenantService.leaveRoom(tenant.getId());

        // Assert
        assertFalse(result.getIsStay());
        assertEquals(0, room.getCurrentTenant());
    }

    @Test
    public void deleteTenant() throws BadRequestException {
        Room room = new Room();
        room.setId(1L);

        Tenant newTenant = new Tenant();
        newTenant.setId(11L);
        newTenant.setName("Hoang 2");
        newTenant.setEmail("ngominhhoang@gmail.com");
        newTenant.setAddress("123 Main Street");
        newTenant.setPhone("0905551234");
        newTenant.setBirth(new Date());
        newTenant.setIsStay(true);
        newTenant.setIdentifyNumber("123456789");
        newTenant.setRoom(room);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(newTenant);
        Tenant savedTenant = tenantRepository.save(newTenant);


        when(tenantRepository.findById(anyLong())).thenReturn(Optional.of(savedTenant));


        tenantService.deleteTenant(savedTenant.getId());


        Optional<Tenant> deletedTenant = tenantRepository.findById(savedTenant.getId());
        assertTrue(deletedTenant.isPresent());
    }
}