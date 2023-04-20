package com.fu.lhm.statistic.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.statistic.model.RevenueStatistic;
import com.fu.lhm.statistic.service.RevenueStatisticService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/revenue-statistic")
@RequiredArgsConstructor
public class RevenueStatisticController {

    private final RevenueStatisticService revenueStatisticService;

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

    @GetMapping("")
    public ResponseEntity<List<RevenueStatistic>> getHouseRevenueStatistic(@RequestParam(name = "year", required = false) int year,
                                                                           @RequestParam(name = "houseId", required = false) Long houseId) throws BadRequestException {

        return ResponseEntity.ok(revenueStatisticService.getRevenueStatistic(getUserToken(),houseId, year));
    }


}
