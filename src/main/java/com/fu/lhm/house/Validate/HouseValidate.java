package com.fu.lhm.house.Validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.House;
import com.fu.lhm.house.Repository.HouseRepository;
import com.fu.lhm.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseValidate {

    @Autowired
    HouseRepository houseRepository;

    public void validateCreateUpdateHouse(House house, User user) {

        validateForNameExist(house.getName(), user);

        this.checkFloorInput(house.getFloor(), "Tầng phải lớn hơn 0");
        this.checkElectricPriceInput(house.getElectricPrice(), "Tiền điện phải lớn hơn hoặc bằng 0");
        this.checkWaterPriceInput(house.getWaterPrice(), "Tiền nước phải lớn hơn hoặc bằng 0");
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
