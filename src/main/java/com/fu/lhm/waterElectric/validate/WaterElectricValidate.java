package com.fu.lhm.waterElectric.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaterElectricValidate {

    public void validateUpdateWaterElectric(WaterElectric waterElectric) throws BadRequestException {
        isNotPopulated(waterElectric.getChiSoDauDien()+"","Vui lòng nhập Chỉ số đầu của điện");
        isNotPopulated(waterElectric.getChiSoDauNuoc()+"","Vui lòng nhập chỉ số đầu của nước");
        isNotPopulated(waterElectric.getChiSoCuoiDien()+"","Vui lòng nhập chỉ số cuối của điện");
        isNotPopulated(waterElectric.getChiSoDauNuoc()+"","Vui lòng nhập chỉ số cuối của nước");
        isNotPopulated(waterElectric.getNumberElectric()+"","Vui lòng nhập số lượng điện");
        isNotPopulated(waterElectric.getNumberWater()+"","Vui lòng nhập số lượng nước");

        validateForChiSoDien(waterElectric.getChiSoDauDien(), waterElectric.getChiSoCuoiDien());
        validateForChiSoNuoc(waterElectric.getChiSoDauNuoc(), waterElectric.getChiSoCuoiNuoc());
        validateForNumberElectric(waterElectric.getNumberElectric());
        validateForNumberWater(waterElectric.getNumberWater());

    }

    public void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    public void validateForChiSoDien(int chiSoDauDien, int chiSoCuoiDien) throws BadRequestException {
        if(chiSoDauDien>chiSoCuoiDien){
            throw new BadRequestException("Chỉ số đầu của điện phải nhỏ hơn chỉ số cuối");
        }else if(chiSoDauDien<0 || chiSoCuoiDien<0){
            throw new BadRequestException("Chỉ số đầu và cuối của điện phải >=0");
        }
    }

    public void validateForChiSoNuoc(int chiSoDauNuoc, int chiSoCuoiNuoc) throws BadRequestException {
        if(chiSoDauNuoc>chiSoCuoiNuoc){
            throw new BadRequestException("Chỉ số đầu của nước phải nhỏ hơn chỉ số cuối");
        }else if(chiSoDauNuoc<0 || chiSoCuoiNuoc<0){
            throw new BadRequestException("Chỉ số đầu và cuối của nước phải >=0");
        }
    }

    public void validateForNumberElectric(int numberElectrict) throws BadRequestException {
        if(numberElectrict<0){
            throw new BadRequestException("Lượng điện sử dụng phải >=0");
        }
    }

    public void validateForNumberWater(int numberWater) throws BadRequestException {
        if(numberWater<0){
            throw new BadRequestException("Lượng nước sử dụng phải >=0");
        }
    }
}
