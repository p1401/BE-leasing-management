package com.fu.lhm.house.controller;


import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.service.HouseService;
import com.fu.lhm.house.validate.HouseValidate;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final HouseValidate houseValidate;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

    @PostMapping({""})
    public ResponseEntity<House> addHouse(@RequestBody House house) throws BadRequestException {

        houseValidate.validateCreateHouse(house, getUserToken());

        house.setUser(getUserToken());

        return ResponseEntity.ok(houseService.createHouse(house, getUserToken()));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<House> updateHouse(@PathVariable("id") Long id, @RequestBody House house) throws BadRequestException {
        houseValidate.validateUpdateHouse(id, house, getUserToken());
        return ResponseEntity.ok(houseService.updateHouse(id, house));
    }

    @GetMapping({""})
    public ResponseEntity<Page<House>> getListHouse(
            @RequestParam(name = "name", defaultValue = "") String houseName,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) throws BadRequestException {
        Page<House> listHouse = houseService.getListHouse(getUserToken(),houseName, PageRequest.of(page, pageSize));

        return ResponseEntity.ok(listHouse);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<House> getHouse(@PathVariable("id") Long id) throws BadRequestException {

        return ResponseEntity.ok(houseService.getHouseById(id));
    }

    @DeleteMapping({"/{id}"})
    public void deleteHouse(@PathVariable("id") Long id) {

        houseService.deleteHouse(id);
    }


}
