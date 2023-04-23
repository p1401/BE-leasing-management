package com.fu.lhm.tenant.service;

import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {
    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private TenantService tenantService;

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
    }

    @Test
    public void testGetListTenants() {
    }

    @Test
    public void testGetTenantById() {
    }

    @Test
    public void leaveRoom() {
    }

    @Test
    public void deleteTenant() {
    }
}