package com.fu.lhm.waterElectric.repository;

import com.fu.lhm.waterElectric.entity.WaterElectric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterElectricRepositoy extends JpaRepository<WaterElectric, Long> {

    WaterElectric findByRoom_Id(Long Id);

}
