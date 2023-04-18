package com.fu.lhm.waterElectric.controller;


import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.waterElectric.service.WaterElectricSerice;
import com.fu.lhm.waterElectric.validate.WaterElectricValidate;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rooms/{roomId}/service")
@RequiredArgsConstructor

public class WaterElecticController {

    private final WaterElectricSerice waterElectricSerice;

    private final WaterElectricValidate waterElectricValidate;

    @PutMapping({""})
    public ResponseEntity<WaterElectric> updateWaterElectrictByRoomId(@PathVariable("roomId") Long id, @RequestBody WaterElectric waterElectric) throws BadRequestException {

        waterElectricValidate.validateUpdateWaterElectric(waterElectric);

        return ResponseEntity.ok(waterElectricSerice.updateWaterElectric(id,waterElectric));
    }

    @GetMapping({""})
    public ResponseEntity<WaterElectric> getWaterElectrictByRoomId(@PathVariable("roomId") Long id) {

        return ResponseEntity.ok(waterElectricSerice.getWaterElectrictByRoomId(id));
    }


}
