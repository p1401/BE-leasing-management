package com.fu.lhm.statistic.service;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.statistic.model.InformationStatistic;
import com.fu.lhm.statistic.service.InformationStatisticService;
import com.fu.lhm.user.entity.User;
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
public class InformationStatisticServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private InformationStatisticService informationStatisticService;

    @Test
    public void getInfor() {
        User user = new User();
        user.setId(1L);

        House house = new House();
        house.setId(1L);

        Room room1 = new Room();
        room1.setId(1L);
        room1.setCurrentTenant(0);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setCurrentTenant(1);

        List<Room> rooms = Arrays.asList(room1, room2);

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setIsActive(true);
        contract.setToDate(new Date());

        List<House> houses = Arrays.asList(house);
        when(houseRepository.findHouses(user.getId(), null)).thenReturn(houses);
        when(roomRepository.findAllByHouse_Id(house.getId())).thenReturn(rooms);
        when(roomRepository.countByHouse_Id(house.getId())).thenReturn(2);
        when(contractRepository.findAllByIsActiveTrueAndTenant_Room_House_Id(house.getId())).thenReturn(Arrays.asList(contract));

        InformationStatistic result = informationStatisticService.getInfor(user, null);

        assertEquals(1, result.getNumberHouse());
        assertEquals(2, result.getNumberRoom());
        assertEquals(1, result.getNumberEmptyRoom());
    }
}