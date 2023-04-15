package com.fu.lhm.house.service;

import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @InjectMocks
    private HouseService houseService;

    @Test
    void createHouse() {
        //arrange
        House house = House.builder().name("Nhà trọ 1").address("Xa La").district("Hà Đông").city("Hà Nội").electricPrice(1000).waterPrice(10000).description("Goood").build();

        when(houseRepository.save(Mockito.any(House.class))).thenReturn(house);

        House saveHouse = houseService.createHouse(house);

        Assertions.assertThat(saveHouse).isNotNull();


    }

    @Test
    void updateHouse() {
    }

    @Test
    void getListHouse() {
    }

    @Test
    void getHouseById() {
    }

    @Test
    void deleteHouse() {
    }
}