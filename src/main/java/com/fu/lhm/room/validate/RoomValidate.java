package com.fu.lhm.room.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomValidate {

    private final HouseRepository houseRepository;

    private final RoomRepository roomRepository;

    public void validateCreateRoom(Room room, Long houseid) throws BadRequestException {
        House house = houseRepository.findById(houseid).orElseThrow(() -> new EntityNotFoundException("Nha không tồn tại!"));
        isNotPopulated(room.getName(),"Vui lòng nhập tên phòng");
        isNotPopulated(room.getRoomMoney()+"","Vui lòng nhập giá thuê phòng");
        isNotPopulated(room.getMaxTenant()+"","Vui lòng nhập số lượng người ở tối đa");
        isNotPopulated(room.getArea()+"","Vui lòng nhập diện tích");
        isNotPopulated(room.getFloor()+"","Vui lòng nhập tầng");

        validateForNameExistInFloor(room.getName(), room.getFloor(), house.getId());
        validateFloor(room.getFloor(), house.getFloor());
        validateArea(room.getArea());
        validateRoomMoney(room.getRoomMoney());
        validateMaxTenant(room.getMaxTenant());
        validateCurrentTenant(room.getMaxTenant(), room.getCurrentTenant());
    }

    public void validateUpdateRoom(Room room, Long roomId) throws BadRequestException {
        Room oldRoom = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));
        isNotPopulated(room.getName(),"Vui lòng nhập tên phòng");
        isNotPopulated(room.getRoomMoney()+"","Vui lòng nhập giá thuê phòng");
        isNotPopulated(room.getMaxTenant()+"","Vui lòng nhập số lượng người ở tối đa");
        isNotPopulated(room.getArea()+"","Vui lòng nhập diện tích");
        isNotPopulated(room.getFloor()+"","Vui lòng nhập tầng");
        validateFloor(room.getFloor(), oldRoom.getHouse().getFloor());
        validateRoomMoney(room.getRoomMoney());
        validateArea(room.getArea());
        validateMaxTenant(room.getMaxTenant());
        validateCurrentTenant(room.getMaxTenant(), room.getCurrentTenant());
        if(oldRoom.getName().equals(room.getName())){

        }else{
            validateForNameExistInFloor(room.getName(), room.getFloor(), oldRoom.getHouse().getId());

        }

    }


    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void validateForNameExistInFloor(String roomName, int floor, Long houseId) throws BadRequestException {
        if (!roomRepository.existsByNameAndFloorAndHouse_Id(roomName, floor, houseId)) {
            return;
        }
        throw new BadRequestException("Tên Phòng ở tầng" +floor+" đã được sử dụng!");
    }

    private void validateFloor(int roomFloor, int houseFloor) throws BadRequestException {
        if (roomFloor>houseFloor ) {
            throw new BadRequestException("Tầng phòng không được lớn hơn tầng tối đa của nhà");
        }else if(roomFloor<=0){
            throw new BadRequestException("Tầng phòng không được nhỏ hơn hoặc bằng 0");
        }
    }


    private void validateArea(int area) throws BadRequestException {
        if (area<0 ) {
            throw new BadRequestException("Diện tích phải > 0");
        }
    }

    private void validateRoomMoney(int roomMoney) throws BadRequestException {
        if (roomMoney<0 ) {
            throw new BadRequestException("Giá thuê phải lớn hơn 0");
        }
    }

    private void validateMaxTenant(int maxTenant) throws BadRequestException {
        if(maxTenant<=0){
            throw new BadRequestException("Số người tối đa 1 phòng phải lớn hơn 0");
        }
    }

    private void validateCurrentTenant(int maxTenant, int currentTenant) throws BadRequestException {
        if(currentTenant>maxTenant){
            throw new BadRequestException("Số người ở không lớn hơn số người tối đa");
        }
    }

    private void validateChiSoCuoiNuoc(int chiSoCuoiNuoc) throws BadRequestException {
        if(chiSoCuoiNuoc<0){
            throw new BadRequestException("Chỉ số cuối nước >=0");
        }
    }

    private void validateChiSoCuoiDien(int chiSoCuoiDien) throws BadRequestException {
        if(chiSoCuoiDien<0){
            throw new BadRequestException("Chỉ số cuối điện >=0");
        }
    }

    private void validateWaterNumber(int waterNumber) throws BadRequestException {
        if(waterNumber<0){
            throw new BadRequestException("Số lượng nước >=0");
        }
    }

    private void validateElectricNumber(int electricNumber) throws BadRequestException {
        if(electricNumber<0){
            throw new BadRequestException("Số lượng điện >=0");
        }
    }

}
