package com.fu.lhm.room.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.service.RoomService;
import com.fu.lhm.room.validate.RoomValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/rooms")
@RequiredArgsConstructor
public class
RoomController {

    private final RoomService roomService;

    private final RoomValidate roomValidate;

    @GetMapping("")
    public ResponseEntity<Page<Room>> getListRoom(
            @RequestParam(name = "houseId") Long houseId,
            @RequestParam(name = "floor", defaultValue = "0") int floor,
            @RequestParam(name = "name", defaultValue = "") String roomName,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<Room> listRoom = roomService.getListRoomByHouseIdAndFloor(houseId, floor,roomName, PageRequest.of(page, pageSize));
        return ResponseEntity.ok(listRoom);
    }

    @PostMapping("/{houseId}")
    public ResponseEntity<Room> createRoom(
            @PathVariable("houseId") Long houseId,
            @RequestBody Room room) throws BadRequestException {

        roomValidate.validateCreateRoom(room, houseId);

        return ResponseEntity.ok(roomService.createRoom(houseId, room));
    }

    @PutMapping({"/{roomId}"})
    public ResponseEntity<Room> updateRoom(@PathVariable("roomId") Long id, @RequestBody Room room) throws BadRequestException {

        roomValidate.validateUpdateRoom(room, id);

        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    @GetMapping({"/{roomId}"})
    public ResponseEntity<Room> getRoomById(@PathVariable("roomId") Long id) {
//        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable("roomId") Long id) {
        roomService.deleteRoom(id);
    }

}
