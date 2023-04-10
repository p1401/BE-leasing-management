package com.fu.lhm.room.repository;

import com.fu.lhm.room.entity.WaterElectric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterElectricRepositoy extends JpaRepository<WaterElectric, Long> {

    WaterElectric findByRoom_Id(Long Id);
}
