package com.fu.lhm.Statistic.Controller;

import com.fu.lhm.Statistic.Entity.RoomStatistic;
import com.fu.lhm.Statistic.Service.RoomStatisticService;
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
