package com.fu.lhm.statistic.controller;

import com.fu.lhm.statistic.model.RoomStatistic;
import com.fu.lhm.statistic.service.RoomStatisticService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Generated
@RestController
@RequestMapping("api/v1/room-statistic")
@RequiredArgsConstructor
public class RoomStatisticController {

    private final RoomStatisticService roomStatisticService;


    @GetMapping("/{houseId}")
    public ResponseEntity<RoomStatistic> getRoomStatisticByHouseId(
            @PathVariable("houseId") Long houseId) {

        return ResponseEntity.ok(roomStatisticService.getRoomStatisticByHouseId(houseId));
    }


}
