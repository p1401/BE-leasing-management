package com.fu.lhm.statistic.controller;

import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.statistic.model.InformationStatistic;
import com.fu.lhm.statistic.model.RevenueStatistic;
import com.fu.lhm.statistic.service.InformationStatisticService;
import com.fu.lhm.statistic.service.RevenueStatisticService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/information-statistic")
@RequiredArgsConstructor
public class InformationStatisticController {

    private final InformationStatisticService informationStatisticService;

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() {
        return jwtService.getUser(httpServletRequest);
    }

    @GetMapping("")
    public ResponseEntity<InformationStatistic> getTotalInformationStatistic() {
        return ResponseEntity.ok(informationStatisticService.getTotalInfor(getUserToken()));
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<InformationStatistic> getHouseInformationStatistic(@PathVariable("houseId") Long houseId) {
        return ResponseEntity.ok(informationStatisticService.getHouseInfor(houseId));
    }

}
