package com.example.demo.house.Serice;

import com.example.demo.config.JwtService;
import com.example.demo.house.House;
import com.example.demo.house.Repository.HouseRepository;
import com.example.demo.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService {

    @Autowired
    HouseRepository houseRepository;

//    JwtService jwtService;

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

//    public List<House> getListHouse(String token){
//
//        String useremail = jwtService.extractUsername(token);
//
//        return houseRepository.findByEmailUser("xxx1x@gmail.com");
//
//    }

    public List<House> getListHouse(){
        return houseRepository.findByEmailUser("test@gmail.com");
    }

    public void deleteHouse(Long houseId) {
         houseRepository.deleteById(houseId);
    }

}
