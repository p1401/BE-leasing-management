package com.fu.lhm.room.service;

import com.fu.lhm.room.entity.WaterElectric;
import com.fu.lhm.room.repository.WaterElectricRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        return waterElectricRepositoy.save(oldWaterElectric);

    }

    public WaterElectric getWaterElectrictByRoomId(Long roomId){

        return waterElectricRepositoy.findByRoom_Id(roomId);
    }





}
