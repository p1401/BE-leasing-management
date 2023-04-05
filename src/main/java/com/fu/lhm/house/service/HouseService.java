package com.fu.lhm.house.service;

import com.fu.lhm.house.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;


    public House createHouse(House house) {
        return houseRepository.save(house);
    }

    public House updateHouse(Long houseId, House updateHouse) {
        House oldHouse = houseRepository.findById(houseId).orElseThrow(() -> new EntityNotFoundException("Nhà không tồn tại!"));
        oldHouse.setName(updateHouse.getName());
        oldHouse.setCity(updateHouse.getCity());
        oldHouse.setDistrict(updateHouse.getDistrict());
        oldHouse.setAddress(updateHouse.getAddress());
        oldHouse.setFloor(updateHouse.getFloor());
        oldHouse.setElectricPrice(updateHouse.getElectricPrice());
        oldHouse.setWaterPrice(updateHouse.getWaterPrice());

        return houseRepository.save(oldHouse);
    }

    public Page<House> getListHouse(User user, Pageable pageable) {
        return houseRepository.findAllByUser(user, pageable);
    }

    public House getHouseById(Long houseId){

        return houseRepository.findById(houseId).orElseThrow(() -> new EntityNotFoundException("Nhà không tồn tại!"));
    }
    public void deleteHouse(Long houseId) {

        houseRepository.deleteById(houseId);
    }

}
