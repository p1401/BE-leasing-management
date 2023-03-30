package com.fu.lhm.room.Controller;

import com.fu.lhm.room.Repository.Roomrepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.room.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    RoomService roomService;
    private final Roomrepository roomrepository;

    @GetMapping("/{houseId}")
    public ResponseEntity<List<Room>> getListRoom(
            @PathVariable("houseId") Long houseId) {

        List<Room> listRoom = roomService.getListRoom(houseId);
        return ResponseEntity.ok(listRoom);
    }

    @PostMapping("/{houseId}")
    public ResponseEntity<Room> createRoom(
            @PathVariable("houseId") Long houseId,
            @RequestBody Room room) {

        return ResponseEntity.ok(roomService.createroom(houseId, room));
    }

    @PutMapping({"/{roomId}"})
    public ResponseEntity<Room> updateRoom(@PathVariable("roomId") Long id, @RequestBody Room room) {
//        houseValidate.validateCreateUpdateHouse(house);
        return ResponseEntity.ok(roomService.updateRoom(id,room));
    }




//    public void deleteRoom(Long roomId) {
//        roomrepository.deleteById(roomId);
//    }

}
