package com.fu.lhm.statistic.service;


import static org.junit.jupiter.api.Assertions.*;

import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.statistic.model.RoomStatistic;
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
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
public class RoomStatisticServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomStatisticService roomService;
    @Test
   public void getRoomStatisticByHouseId() {
        // Arrange
        Long houseId = 1L;
        Room room1 = new Room();
        room1.setId(1L);
        room1.setCurrentTenant(1);
        room1.setMaxTenant(2);
        Room room2 = new Room();
        room2.setId(2L);
        room2.setCurrentTenant(0);
        room2.setMaxTenant(2);
        List<Room> rooms = Arrays.asList(room1, room2);
        Mockito.when(roomRepository.findAllByHouse_Id(houseId)).thenReturn(rooms);

        // Act
        RoomStatistic result = roomService.getRoomStatisticByHouseId(houseId);

        // Assert
        assertEquals(0, result.getRoomFull());
        assertEquals(1, result.getRoomHaveSlot());
        assertEquals(1, result.getRoomEmpty());
    }
}