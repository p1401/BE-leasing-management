package com.fu.lhm.house.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class HouseServiceTest {
    @Mock
    private HouseService houseService;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private RoomRepository roomRepository;
    @Before
    public void setUp() {

    }

    @Test
    public void testCreateHouseWithInvalidData() {
        // given
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setName("Test House");
        house.setCity("Test City");
        house.setDistrict("Test District");
        house.setAddress("Test Address");
        house.setElectricPrice(100);
        house.setWaterPrice(50);
        house.setFloor(2);

        when(houseRepository.save(any(House.class))).thenReturn(house);
        when(houseService.createHouse(any(House.class))).thenReturn(house);

        House createdHouse = houseService.createHouse(house);

        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(house, createdHouse);


    }
}