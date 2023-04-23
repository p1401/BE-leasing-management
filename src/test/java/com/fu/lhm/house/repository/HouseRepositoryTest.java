package com.fu.lhm.house.repository;

import com.fu.lhm.house.entity.House;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class HouseRepositoryTest {

    @Mock
    private HouseRepository houseRepository;

    @Test
    public void createHouse_1(){
        //arrange
        House houseTest = new House();
        houseTest.setName("T");
        houseTest.setCity("Test c");
        houseTest.setDistrict("Test 1");
        houseTest.setAddress("Test x");
        houseTest.setElectricPrice(100);
        houseTest.setWaterPrice(50);
        houseTest.setFloor(2);

        //act
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);
        House createdHouse = houseRepository.save(houseTest);

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
    public void createHouse_2(){
        //arrange
        House houseTest = new House();
        houseTest.setName("T 2");
        houseTest.setCity("Test 2");
        houseTest.setDistrict("Test 2");
        houseTest.setAddress("Test 2");
        houseTest.setElectricPrice(10);
        houseTest.setWaterPrice(5);
        houseTest.setFloor(2);

        //act
        when(houseRepository.save(eq(houseTest))).thenReturn(houseTest);
        House createdHouse = houseRepository.save(houseTest);

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
}