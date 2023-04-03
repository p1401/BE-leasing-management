package com.fu.lhm.room.controller;

import com.fu.lhm.house.House;
import com.fu.lhm.room.Room;
import com.fu.lhm.room.modal.SendListRoomAndInforRequest;
import com.fu.lhm.room.service.RoomService;
import com.fu.lhm.room.validate.RoomValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    private final RoomValidate roomValidate;

    @GetMapping("/{houseId}/floor/{floor}")
    public ResponseEntity<Page<Room>> getListRoom(
            @PathVariable("houseId") Long houseId,
            @PathVariable("floor") int floor,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Room> listRoom =  roomService.getListRoomByHouseIdAndFloor(houseId ,floor, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(listRoom);
    }

    @PostMapping("/{houseId}")
    public ResponseEntity<Room> createRoom(
            @PathVariable("houseId") Long houseId,
            @RequestBody Room room) {
        roomValidate.validateCreateRoom(room, houseId);

        return ResponseEntity.ok(roomService.createroom(houseId, room));
    }

    @PutMapping({"/{roomId}"})
    public ResponseEntity<Room> updateRoom(@PathVariable("roomId") Long id, @RequestBody Room room) {
//        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    @GetMapping({"/{roomId}"})
    public ResponseEntity<Room> getRoomById(@PathVariable("roomId") Long id) {
//        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(roomService.getRoomById(id));
    }


    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable("roomId") Long id) {
        roomService.deleteRoom(id);
    }

}
