package com.fu.lhm.statistic.Controller;

import com.fu.lhm.statistic.Entity.RoomStatistic;
import com.fu.lhm.statistic.Service.RoomStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/roomStatistic")
@RequiredArgsConstructor
public class RoomStatisticController {

    private final RoomStatisticService roomStatisticService;


    @GetMapping("/{houseId}")
    public ResponseEntity<RoomStatistic> getRoomStatisticByHouseId(
            @PathVariable("houseId") Long houseId) {

        return ResponseEntity.ok(roomStatisticService.getRoomStatisticByHouseId(houseId));
    }


}
