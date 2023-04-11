package com.fu.lhm.statistic.controller;

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
    private User getUserToken() {
        return jwtService.getUser(httpServletRequest);
    }

    @GetMapping("")
    public ResponseEntity<List<RevenueStatistic>> getTotalRevenueStatistic(@RequestParam(name = "year", required = false) int year) {

        return ResponseEntity.ok(revenueStatisticService.getTotalRevenueStatistic(getUserToken(), year));
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<List<RevenueStatistic>> getHouseRevenueStatistic(@RequestParam(name = "year", required = false) int year,
                                                                           @PathVariable("houseId") Long houseId) {

        return ResponseEntity.ok(revenueStatisticService.getHouseRevenueStatistic(houseId, year));
    }


}
