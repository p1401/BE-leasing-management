package com.fu.lhm.house.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseValidate {
    private final HouseRepository houseRepository;

    public void validateCreateHouse(House house, User user) throws BadRequestException {

        validateForNameExist(house.getName(), user);

        isNotPopulated(house.getName(),"Vui lòng nhập tên nhà");
        isNotPopulated(house.getCity(),"Vui lòng nhập tên Thành Phố");
        isNotPopulated(house.getDistrict(),"Vui lòng nhập tên Quận");
        isNotPopulated(house.getAddress(),"Vui lòng nhập địa chỉ");
        isNotPopulated(house.getFloor()+"","Vui lòng nhập số tầng tối đa");
        isNotPopulated(house.getElectricPrice()+"","Vui lòng nhập giá Điện");
        isNotPopulated(house.getWaterPrice()+"","Vui lòng nhập giá nước");

        checkFloorInput(house.getFloor(), "Tầng phải lớn hơn 0");
        checkElectricPriceInput(house.getElectricPrice(), "Tiền điện phải lớn hơn hoặc bằng 0");
        checkWaterPriceInput(house.getWaterPrice(), "Tiền nước phải lớn hơn hoặc bằng 0");
    }

    public void validateUpdateHouse(Long houseId, House house,User user) throws BadRequestException {

        House oldHouse = houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nhà không tồn tại!"));
        if(oldHouse.getName().equals(house.getName())){

        }else {
            validateForNameExist(house.getName(), user);
        }

        isNotPopulated(house.getName(),"Vui lòng nhập tên nhà");
        isNotPopulated(house.getCity(),"Vui lòng nhập tên Thành Phố");
        isNotPopulated(house.getDistrict(),"Vui lòng nhập tên Quận");
        isNotPopulated(house.getAddress(),"Vui lòng nhập địa chỉ");
        isNotPopulated(house.getFloor()+"","Vui lòng nhập số tầng tối đa");
        isNotPopulated(house.getElectricPrice()+"","Vui lòng nhập giá Điện");
        isNotPopulated(house.getWaterPrice()+"","Vui lòng nhập giá nước");

        checkFloorInput(house.getFloor(), "Tầng phải lớn hơn 0");
        checkElectricPriceInput(house.getElectricPrice(), "Tiền điện phải lớn hơn hoặc bằng 0");
        checkWaterPriceInput(house.getWaterPrice(), "Tiền nước phải lớn hơn hoặc bằng 0");
    }


    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(errorMsg);
        }
    }


    private void validateForNameExist(String houseName, User user) throws BadRequestException {
        if (!houseRepository.existsByNameAndUser(houseName, user)) {
            return;
        }

        throw new BadRequestException("Tên nhà đã được sử dụng!");
    }

    private void checkFloorInput(int floor, String errorMsg) throws BadRequestException {
        if (floor <= 0) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkElectricPriceInput(int electricPrice, String errorMsg) throws BadRequestException {
        if (electricPrice < 0) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkWaterPriceInput(int waterPrice, String errorMsg) throws BadRequestException {
        if (waterPrice < 0) {
            throw new BadRequestException(errorMsg);
        }
    }


}
