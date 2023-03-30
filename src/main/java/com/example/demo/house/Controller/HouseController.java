package com.example.demo.house.Controller;


import com.example.demo.house.House;
import com.example.demo.house.Repository.HouseRepository;
import com.example.demo.house.Serice.HouseService;
import com.example.demo.house.Validate.HouseValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    private final HouseValidate houseValidate;
    private final HouseRepository houseRepository;

    @PostMapping({""})
    public ResponseEntity<House> addHouse(@RequestBody House house) {
        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(houseService.createHouse(house));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<House> updateHouse(@PathVariable("id") Long id, @RequestBody House house) {
        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(houseService.updateHouse(id,house));
    }

//    @GetMapping({""})
//    public ResponseEntity<List<House>> getListHouse(@RequestHeader("Authorization") String token) {
//
//        List<House> listHouse = houseService.getListHouse(token);
//
//        return ResponseEntity.ok(listHouse);
//    }

    @GetMapping({""})
    public ResponseEntity<List<House>> getListHouse() {

        List<House> listHouse = houseService.getListHouse();

        return ResponseEntity.ok(listHouse);
    }





}
