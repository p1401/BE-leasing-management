package com.fu.lhm.house.Controller;


import com.fu.lhm.house.House;
import com.fu.lhm.house.Repository.HouseRepository;
import com.fu.lhm.house.Serice.HouseService;
import com.fu.lhm.house.Validate.HouseValidate;
import com.fu.lhm.jwt.JwtService;
import com.fu.lhm.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final HouseValidate houseValidate;
    private final HouseRepository houseRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;


    @PostMapping({""})
    public ResponseEntity<House> addHouse(@RequestBody House house) {
        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(houseService.createHouse(house));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<House> updateHouse(@PathVariable("id") Long id, @RequestBody House house) {
        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(houseService.updateHouse(id, house));
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

        User user = jwtService.getUser(httpServletRequest);

        List<House> listHouse = houseService.getListHouse();

        return ResponseEntity.ok(listHouse);
    }
}
