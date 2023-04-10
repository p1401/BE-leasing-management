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

    public void validateCreateUpdateHouse(House house, User user) {

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

    private void isNotPopulated(String value, String errorMsg) {
        if (null == value || value.trim().isEmpty()) {
            throw new BadRequestException(errorMsg);
        }
    }


    private void validateForNameExist(String houseName, User user) {
        if (!houseRepository.existsByNameAndUser(houseName, user)) {
            return;
        }

        throw new BadRequestException("Tên nhà đã được sử dụng!");
    }

    private void checkFloorInput(int floor, String errorMsg) {
        if (floor <= 0) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkElectricPriceInput(int electricPrice, String errorMsg) {
        if (electricPrice < 0) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkWaterPriceInput(int waterPrice, String errorMsg) {
        if (waterPrice < 0) {
            throw new BadRequestException(errorMsg);
        }
    }


}
