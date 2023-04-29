package com.fu.lhm.house.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.service.HouseService;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HouseControllerTest {
    @Autowired
    private HouseController houseController;

    @MockBean
    private HouseService houseService;

    @Test
    public void addHouse(){

    }

    @Test
    public void updateHouse(){

    }

    @Test
    public void getListHouse() {
    }

    @Test
    public void getHouse() {
    }

    @Test
    public void deleteHouse() {
    }
}