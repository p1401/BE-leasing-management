package com.fu.lhm.waterElectric.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.waterElectric.controller.WaterElecticController;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.service.WaterElectricSerice;
import com.fu.lhm.waterElectric.validate.WaterElectricValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class WaterElecticControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private WaterElectricSerice waterElectricSerice;
    @Mock
    private WaterElectricValidate waterElectricValidate;

    @InjectMocks
    private WaterElecticController waterElecticController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(waterElecticController).build();
    }

    @Test
    public void testUpdateWaterElectricByRoomId() throws Exception {
        // Given
        Long roomId = 1L;

        WaterElectric waterElectric = new WaterElectric();
        waterElectric.setChiSoDauDien(100);
        waterElectric.setChiSoDauNuoc(100);
        waterElectric.setChiSoCuoiDien(150);
        waterElectric.setChiSoCuoiNuoc(150);
        waterElectric.setNumberElectric(50);
        waterElectric.setNumberWater(50);
        waterElectric.setDateUpdate(new Date());

        WaterElectric updatedWaterElectric = new WaterElectric();
        updatedWaterElectric.setChiSoDauDien(200);
        updatedWaterElectric.setChiSoDauNuoc(200);
        updatedWaterElectric.setChiSoCuoiDien(300);
        updatedWaterElectric.setChiSoCuoiNuoc(300);
        updatedWaterElectric.setNumberElectric(100);
        updatedWaterElectric.setNumberWater(100);
        updatedWaterElectric.setDateUpdate(new Date());

        // Then
        when(waterElectricSerice.updateWaterElectric(roomId, waterElectric)).thenReturn(updatedWaterElectric);

        // Perform
        mockMvc.perform(put("/api/v1/rooms/{roomId}/service", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(waterElectric)))
                        .andExpect(status().isOk());

        // Verify
        verify(waterElectricValidate, times(1)).validateUpdateWaterElectric(waterElectric);
        verify(waterElectricSerice, times(1)).updateWaterElectric(roomId, waterElectric);

    }

    @Test
    public void testUpdateWaterElectricByRoomId_InvalidChiSoDien() throws Exception {
        // Given
        Long roomId = 1L;

        WaterElectric waterElectric = new WaterElectric();
        waterElectric.setChiSoDauDien(200);
        waterElectric.setChiSoDauNuoc(100);
        waterElectric.setChiSoCuoiDien(150);
        waterElectric.setChiSoCuoiNuoc(150);
        waterElectric.setNumberElectric(50);
        waterElectric.setNumberWater(50);
        waterElectric.setDateUpdate(new Date());

        // Then
        doThrow(new BadRequestException("Chỉ số đầu của điện phải nhỏ hơn chỉ số cuối")).when(waterElectricValidate).validateUpdateWaterElectric(waterElectric);

        // Perform
        mockMvc.perform(put("/api/v1/rooms/{roomId}/service", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(waterElectric)))
                        .andExpect(status().isBadRequest());

        // Verify
        verify(waterElectricValidate, times(1)).validateUpdateWaterElectric(eq(waterElectric));

    }

    @Test
    public void testGetWaterElectrictByRoomId() throws Exception {
        // Given
        Long roomId = 1L;

        WaterElectric waterElectric = new WaterElectric();
        waterElectric.setChiSoDauDien(100);
        waterElectric.setChiSoDauNuoc(100);
        waterElectric.setChiSoCuoiDien(150);
        waterElectric.setChiSoCuoiNuoc(150);
        waterElectric.setNumberElectric(50);
        waterElectric.setNumberWater(50);

        // When
        when(waterElectricSerice.getWaterElectrictByRoomId(roomId)).thenReturn(waterElectric);

        // Perform
        mockMvc.perform(get("/api/v1/rooms/{roomId}/service", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chiSoDauDien", is(waterElectric.getChiSoDauDien())))
                .andExpect(jsonPath("$.chiSoDauNuoc", is(waterElectric.getChiSoDauNuoc())))
                .andExpect(jsonPath("$.chiSoCuoiDien", is(waterElectric.getChiSoCuoiDien())))
                .andExpect(jsonPath("$.chiSoCuoiNuoc", is(waterElectric.getChiSoCuoiNuoc())))
                .andExpect(jsonPath("$.numberElectric", is(waterElectric.getNumberElectric())))
                .andExpect(jsonPath("$.numberWater", is(waterElectric.getNumberWater())));

        // Verify
        verify(waterElectricSerice, times(1)).getWaterElectrictByRoomId(roomId);
    }


}