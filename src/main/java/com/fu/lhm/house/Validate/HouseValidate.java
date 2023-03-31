package com.fu.lhm.house.Validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.House;
import com.fu.lhm.house.Repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseValidate {

    @Autowired
    HouseRepository houseRepository;

    public void validateCreateUpdateHouse(House house) {
        this.checkNameExist(house.getName(),house.getEmailUser(), "Tên nhà đã tồn tại");
        this.checkFloorInput(house.getFloor(),"Tầng phải lớn hơn 0");
        this.checkElectricPriceInput(house.getElectricPrice(),"Tiền điện phải lớn hơn hoặc bằng 0");
        this.checkWaterPriceInput(house.getWaterPrice(),"Tiền nước phải lớn hơn hoặc bằng 0");
    }


    private void checkNameExist(String houseName, String emailUser, String errorMsg) {
        List<House> listHouse = houseRepository.findAllByEmailUserIgnoreCase(emailUser);

        for(House house : listHouse){
            if(houseName.equalsIgnoreCase(house.getName())){
                throw new BadRequestException(errorMsg);
            }
        }
    }

    private void checkFloorInput(int floor, String errorMsg){
        if(floor<=0){
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkElectricPriceInput(int electricPrice, String errorMsg){
        if(electricPrice<0){
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkWaterPriceInput(int waterPrice, String errorMsg){
        if(waterPrice<0){
            throw new BadRequestException(errorMsg);
        }
    }



}
