package com.fu.lhm.waterElectric.service;

import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.repository.WaterElectricRepositoy;
import com.fu.lhm.waterElectric.service.WaterElectricSerice;
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
public class WaterElectricSericeTest {


    @InjectMocks
    private WaterElectricSerice waterElectricService;

    @Mock
    private WaterElectricRepositoy waterElectricRepository;

    @Mock
    private RoomRepository roomRepository;

    @Test
    public void updateWaterElectric() {
        // Create a new WaterElectric object
        WaterElectric newWaterElectric = new WaterElectric();
        newWaterElectric.setChiSoDauNuoc(100);
        newWaterElectric.setChiSoDauDien(200);
        newWaterElectric.setChiSoCuoiNuoc(150);
        newWaterElectric.setChiSoCuoiDien(250);
        newWaterElectric.setNumberElectric(50);
        newWaterElectric.setNumberWater(50);
        newWaterElectric.setDateUpdate(new Date());

        // Create a mock Room object
        Room room = new Room();
        room.setId(1L);

        // Mock the dependencies
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(waterElectricRepository.findByRoom_Id(room.getId())).thenReturn(new WaterElectric());
        when(waterElectricRepository.save(any(WaterElectric.class))).thenReturn(newWaterElectric);

        // Call the method being tested
        WaterElectric updatedWaterElectric = waterElectricService.updateWaterElectric(room.getId(), newWaterElectric);

        // Verify that the returned WaterElectric object has the updated values
        assertEquals(newWaterElectric.getChiSoDauNuoc(), updatedWaterElectric.getChiSoDauNuoc());
        assertEquals(newWaterElectric.getChiSoDauDien(), updatedWaterElectric.getChiSoDauDien());
        assertEquals(newWaterElectric.getChiSoCuoiNuoc(), updatedWaterElectric.getChiSoCuoiNuoc());
        assertEquals(newWaterElectric.getChiSoCuoiDien(), updatedWaterElectric.getChiSoCuoiDien());
        assertEquals(newWaterElectric.getNumberElectric(), updatedWaterElectric.getNumberElectric());
        assertEquals(newWaterElectric.getNumberWater(), updatedWaterElectric.getNumberWater());
        assertEquals(newWaterElectric.getDateUpdate(), updatedWaterElectric.getDateUpdate());
    }

    @Test
    public void getWaterElectrictByRoomId() {
        Long roomId = 1L;

        // Create a dummy water electric object
        WaterElectric waterElectric = new WaterElectric();
        waterElectric.setId(1L);
        waterElectric.setChiSoDauDien(100);
        waterElectric.setChiSoDauNuoc(50);
        waterElectric.setChiSoCuoiDien(200);
        waterElectric.setChiSoCuoiNuoc(100);
        waterElectric.setNumberElectric(100);
        waterElectric.setNumberWater(50);

        // Set up the mock repository to return the dummy object when called with the given roomId
        when(waterElectricRepository.findByRoom_Id(roomId)).thenReturn(waterElectric);

        // Call the method being tested
        WaterElectric result = waterElectricService.getWaterElectrictByRoomId(roomId);

        // Verify the result
        assertEquals(waterElectric.getId(), result.getId());
        assertEquals(waterElectric.getChiSoDauDien(), result.getChiSoDauDien());
        assertEquals(waterElectric.getChiSoDauNuoc(), result.getChiSoDauNuoc());
        assertEquals(waterElectric.getChiSoCuoiDien(), result.getChiSoCuoiDien());
        assertEquals(waterElectric.getChiSoCuoiNuoc(), result.getChiSoCuoiNuoc());
        assertEquals(waterElectric.getNumberElectric(), result.getNumberElectric());
        assertEquals(waterElectric.getNumberWater(), result.getNumberWater());
    }
}