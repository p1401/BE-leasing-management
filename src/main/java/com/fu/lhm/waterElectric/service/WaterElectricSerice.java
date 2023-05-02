package com.fu.lhm.waterElectric.service;

import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.repository.WaterElectricRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class WaterElectricSerice {

    private final WaterElectricRepositoy waterElectricRepositoy;

    public WaterElectric updateWaterElectric(Long roomId, WaterElectric newWaterElectric){

        WaterElectric oldWaterElectric = waterElectricRepositoy.findByRoom_Id(roomId);

        oldWaterElectric.setChiSoDauNuoc(newWaterElectric.getChiSoDauNuoc());
        oldWaterElectric.setChiSoDauDien(newWaterElectric.getChiSoDauDien());
        oldWaterElectric.setChiSoCuoiNuoc(newWaterElectric.getChiSoCuoiNuoc());
        oldWaterElectric.setChiSoCuoiDien(newWaterElectric.getChiSoCuoiDien());
        oldWaterElectric.setNumberElectric(newWaterElectric.getNumberElectric());
        oldWaterElectric.setNumberWater(newWaterElectric.getNumberWater());
        oldWaterElectric.setDateUpdate(new Date());

        return waterElectricRepositoy.save(oldWaterElectric);
    }

    public WaterElectric getWaterElectrictByRoomId(Long roomId){

        return waterElectricRepositoy.findByRoom_Id(roomId);
    }





}
