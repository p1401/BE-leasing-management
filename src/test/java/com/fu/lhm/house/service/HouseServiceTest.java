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
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.Optional;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class HouseServiceTest {
    @InjectMocks
    private HouseService houseService;

    @Mock
    private HouseRepository houseRepository;

    private House house;
    @Before
    public void setUp() {
        house = new House();
        house.setId(1L);
        house.setName("Test House");
        house.setCity("Test City");
        house.setDistrict("Test District");
        house.setAddress("Test Address");
        house.setElectricPrice(100);
        house.setWaterPrice(50);
        house.setFloor(2);

    }

    @Test
    public void testCreateHouseWithValidData_1() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("Nhà trọ 1");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());

    }

    @Test
    public void testCreateHouseWithValidData_2() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("T");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());

    }

    @Test()
    public void testCreateHouseWithValidData_3() throws BadRequestException {
        // given
        User user = new User();
        user.setId(1L);

        House houseTest = new House();
        houseTest.setName("T");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //when
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);

        House createdHouse = houseService.createHouse(houseTest, user);

        //assert
        Assert.assertNotNull(createdHouse);
        Assert.assertEquals(houseTest, createdHouse);
        Assert.assertEquals(houseTest.getId(), createdHouse.getId());
        Assert.assertEquals(houseTest.getName(), createdHouse.getName());
        Assert.assertEquals(houseTest.getCity(), createdHouse.getCity());
        Assert.assertEquals(houseTest.getDistrict(), createdHouse.getDistrict());
        Assert.assertEquals(houseTest.getFloor(), createdHouse.getFloor());
        Assert.assertEquals(houseTest.getWaterPrice(), createdHouse.getWaterPrice());
        Assert.assertEquals(houseTest.getElectricPrice(), createdHouse.getElectricPrice());
        Assert.assertEquals(houseTest.getUser(), createdHouse.getUser());

    }

    @Test()
    public void testUpdateHouseWithValidData_1() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("Old House Name");

        House updatedHouse = new House();
        updatedHouse.setName("Test House");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District");
        updatedHouse.setAddress("Test Address");
        updatedHouse.setElectricPrice(100);
        updatedHouse.setWaterPrice(50);
        updatedHouse.setFloor(2);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }

    @Test()
    public void testUpdateHouseWithValidData_2() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("Old House Name");

        House updatedHouse = new House();
        updatedHouse.setName("Test House");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District");
        updatedHouse.setAddress("Test Address");
        updatedHouse.setElectricPrice(100);
        updatedHouse.setWaterPrice(50);
        updatedHouse.setFloor(2);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }

    @Test()
    public void testUpdateHouseWithValidData_3() throws BadRequestException {
        //given
        House existingHouse = new House();
        existingHouse.setId(1L);
        existingHouse.setName("My House");

        House updatedHouse = new House();
        updatedHouse.setName("Txxx");
        updatedHouse.setCity("Test City");
        updatedHouse.setDistrict("Test District 2");
        updatedHouse.setAddress("Test Address 2");
        updatedHouse.setElectricPrice(1);
        updatedHouse.setWaterPrice(2);
        updatedHouse.setFloor(1);

        //when
        when(houseRepository.findById(existingHouse.getId())).thenReturn(Optional.of(Optional.of(existingHouse).get()));
        when(houseRepository.save(eq(existingHouse))).thenReturn(existingHouse);

        houseService.updateHouse(1L,updatedHouse);
        House updateHouseTest = houseService.updateHouse(1L,updatedHouse);

        //assert
        Assert.assertNotNull(updateHouseTest);
        Assert.assertEquals(existingHouse, updateHouseTest);
        Assert.assertEquals(existingHouse.getId(), updateHouseTest.getId());
        Assert.assertEquals(existingHouse.getName(), updateHouseTest.getName());
        Assert.assertEquals(existingHouse.getCity(), updateHouseTest.getCity());
        Assert.assertEquals(existingHouse.getDistrict(), updateHouseTest.getDistrict());
        Assert.assertEquals(existingHouse.getFloor(), updateHouseTest.getFloor());
        Assert.assertEquals(existingHouse.getWaterPrice(), updateHouseTest.getWaterPrice());
        Assert.assertEquals(existingHouse.getElectricPrice(), updateHouseTest.getElectricPrice());
        Assert.assertEquals(existingHouse.getUser(), updateHouseTest.getUser());
    }


}