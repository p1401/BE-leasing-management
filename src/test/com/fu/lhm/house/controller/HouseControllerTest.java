package com.fu.lhm.house.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.controller.HouseController;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.service.HouseService;
import com.fu.lhm.house.validate.HouseValidate;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class HouseControllerTest {
    @InjectMocks
    private HouseController houseController;
    @Mock
    private HouseService houseService;
    @Mock
    private HouseValidate houseValidate;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private JwtService jwtService;

    ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;


    private final User user = new User();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(houseController).build();
    }

    @Test
    public void addHouse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("password");
        String token = "testtoken";
        // Prepare test data
        House house = new House();
        house.setName("Test House");
        house.setFloor(10);
        house.setCity("xxx");
        house.setDistrict("xxx");
        house.setWaterPrice(10);
        house.setUser(user);
        house.setDescription("xxx");
        house.setAddress("123 Test Street");



        // Mock dependencies
        when(jwtService.getUser(httpServletRequest)).thenReturn(user);
        when(houseService.createHouse(house, user)).thenReturn(house);

        // Perform the request
        mockMvc.perform(post("/api/v1/houses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(house)))
                .andExpect(status().isOk());

        // Verify the interactions
        verify(houseValidate).validateCreateHouse(house, user);
        verify(houseService).createHouse(house, user);
    }

    @Test
    public void updateHouse() throws Exception {
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setName("Test House");
        house.setFloor(10);
        house.setCity("xxx");
        house.setDistrict("xxx");
        house.setWaterPrice(10);
        house.setUser(user);
        house.setDescription("xxx");
        house.setAddress("123 Test Street");



        String requestJson = "{\"name\":\"house1\"}";

        mockMvc.perform(
                        put("/api/v1/houses/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());

    }


    @Test
    public void getListHouse() throws Exception {
        // Tạo một danh sách các House
        House house = new House();
        house.setName("Test House");
        house.setFloor(10);
        house.setCity("xxx");
        house.setDistrict("xxx");
        house.setWaterPrice(10);
        house.setUser(user);
        house.setDescription("xxx");
        house.setAddress("123 Test Street");

        House house2 = new House();
        house2.setName("Test House");
        house2.setFloor(10);
        house2.setCity("xxx");
        house2.setDistrict("xxx");
        house2.setWaterPrice(10);
        house2.setUser(user);
        house2.setDescription("xxx");
        house2.setAddress("123 Test Street");

        List<House> houseList = new ArrayList<>();
        houseList.add(house);
        houseList.add(house2);
        Page<House> housePage = new PageImpl<>(houseList);


        // Gọi API để lấy danh sách các House
        mockMvc.perform(get("/api/v1/houses"))
                .andExpect(status().isOk());
    }


    @Test
    public void getHouse() throws Exception {
        Long id = 1L;
        House house = new House();
        house.setId(id);
        house.setName("Test house");

        Mockito.when(houseService.getHouseById(id)).thenReturn(house);

        mockMvc.perform(get("/api/v1/houses/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteHouse() throws Exception {
        Long id = 1L;
        doNothing().when(houseService).deleteHouse(id);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(houseController).build();
        mockMvc.perform(delete("/api/v1/houses/" + id))
                .andExpect(status().isOk());

        verify(houseService, times(1)).deleteHouse(id);
    }
}