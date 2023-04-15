package com.fu.lhm.house.repository;

import com.fu.lhm.house.entity.House;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HouseRepositoryTest {

    @Autowired
    private HouseRepository houseRepository;

    @Test
    public void createHouse(){
        //arrange
        House house = House.builder().name("Nhà trọ 1").address("Xa La").district("Hà Đông").city("Hà Nội").build();

        //act
        House saveHouse = houseRepository.save(house);

        //Assert
        Assertions.assertThat(saveHouse).isNotNull();
        Assertions.assertThat(saveHouse.getId()).isGreaterThan(0);
    }
}