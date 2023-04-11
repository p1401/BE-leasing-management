package com.fu.lhm.statistic.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.statistic.model.InformationStatistic;
import com.fu.lhm.statistic.service.InformationStatisticService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/information-statistic")
@RequiredArgsConstructor
public class InformationStatisticController {

    private final InformationStatisticService informationStatisticService;

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

    @GetMapping("")
    public ResponseEntity<InformationStatistic> getTotalInformationStatistic() throws BadRequestException {
        return ResponseEntity.ok(informationStatisticService.getTotalInfor(getUserToken()));
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<InformationStatistic> getHouseInformationStatistic(@PathVariable("houseId") Long houseId) throws BadRequestException {
        return ResponseEntity.ok(informationStatisticService.getHouseInfor(houseId));
    }

}
