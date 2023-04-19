package com.fu.lhm.house.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.house.validate.HouseValidate;
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


import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    @Mock
    private HouseValidate houseValidate;

    @InjectMocks
    private User user;
    private House house;

    @Before
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email("phuc@gmail.com")
                .password("123456")
                .build();
        house = House.builder()
                .id(1L)
                .name("Test House")
                .city("Test City")
                .district("Test District")
                .address("Test Address")
                .electricPrice(1000)
                .waterPrice(5000)
                .floor(3).build();
    }

    @Test
    public void testCreateHouseWithValidData() throws BadRequestException {
        // given - precondition or setup
        House createHouse = House.builder()
                .id(2L)
                .name("Test House 1")
                .city("Test City")
                .district("Test District")
                .address("Test Address")
                .electricPrice(1000)
                .waterPrice(5000)
                .floor(3).build();

        // when -  action or the behaviour that we are going test
        when(houseService.createHouse(house, user)).thenReturn(createHouse);
        House createdHouse = houseService.createHouse(house, user);

        // then - verify the output
        Assert.assertNotNull(createdHouse);
    }

//    @Test(expected = BadRequestException.class)
//    public void testCreateHouseWithExistName() throws BadRequestException {
//        when(houseRepository.findHouseByName(house.getName())).thenReturn(house);
//
//        // Create a new House object with the same ID as the existing house
//        House newHouse = House.builder()
//                .id(2L)
//                .name("Test House")
//                .city("Test City")
//                .district("Test District")
//                .address("Test Address")
//                .electricPrice(1000)
//                .waterPrice(5000)
//                .floor(3).build();
//
//
//        doThrow(BadRequestException.class).when(houseValidate).validateCreateHouse(house, user);
//        houseService.createHouse(newHouse, user);
//    }

    @Test
    public void testUpdateHouseWithValidData() throws BadRequestException {
        // given - precondition or setup
        given(houseRepository.save(house)).willReturn(house);
        house.setName("Trọ Sao");
        house.setDescription("OK");

        // when -  action or the behaviour that we are going test
        House updatedHouse = houseService.updateHouse(house.getId(), house);

        // then - verify the output
        Assert.assertEquals(updatedHouse.getName(), "Trọ Sao");
        Assert.assertEquals(updatedHouse.getDescription(), "OK");
    }

    @Test
    public void testDeleteHouseWithValidData() throws BadRequestException {
        // given - precondition or setup
        long houseId = 1L;
        willDoNothing().given(houseRepository).deleteById(houseId);

        // when -  action or the behaviour that we are going test
        houseService.deleteHouse(houseId);

        // then - verify the output
        verify(houseRepository, times(1)).deleteById(houseId);
    }

//    @Test
//    public void testCreateHouseWithValidData_5() throws BadRequestException {
//        // given
//        User user = new User();
//        user.setId(1L);
//
//        House house = new House();
//        house.setName("Test House");
//        house.setCity("Test City");
//        house.setDistrict("Test District");
//        house.setAddress("Test Address");
//        house.setElectricPrice(100);
//        house.setWaterPrice(50);
//        house.setFloor(2);
//
//        when(houseService.createHouse(any(House.class),any(User.class))).thenReturn(house);
//
//        House createdHouse = houseService.createHouse(house, user);
//
//        Assert.assertNotNull(createdHouse);
//        Assert.assertEquals(house, createdHouse);
//        Assert.assertEquals(house.getId(), createdHouse.getId());
//        Assert.assertEquals(house.getName(), createdHouse.getName());
//        Assert.assertEquals(house.getCity(), createdHouse.getCity());
//        Assert.assertEquals(house.getDistrict(), createdHouse.getDistrict());
//        Assert.assertEquals(house.getFloor(), createdHouse.getFloor());
//        Assert.assertEquals(house.getWaterPrice(), createdHouse.getWaterPrice());
//        Assert.assertEquals(house.getElectricPrice(), createdHouse.getElectricPrice());
//        Assert.assertEquals(house.getUser(), createdHouse.getUser());
//    }
}