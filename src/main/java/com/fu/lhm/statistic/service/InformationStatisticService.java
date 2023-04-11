package com.fu.lhm.statistic.service;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.statistic.model.InformationStatistic;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationStatisticService {

    private final HouseRepository houseRepository;

    private final RoomRepository roomRepository;

    private final ContractRepository contractRepository;

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public InformationStatistic getTotalInfor(User user){

        Integer numberHouse = 0;
        Integer numberRoom = 0;
        Integer numberRoomEmpty =0;
        Integer numberContractExpired30days=0;

        List<House> listHouse = houseRepository.findAllByUser(user);
        LocalDate today = LocalDate.now();
        for(House house : listHouse){
            numberHouse=numberHouse+1;
            numberRoom = numberRoom+roomRepository.countByHouse_Id(house.getId());
            List<Room> listRoom = roomRepository.findAllByHouse_Id(house.getId());
            for(Room room : listRoom){
                if(room.getCurrentTenant()==0){
                    numberRoomEmpty=numberRoomEmpty+1;
                }
            }


            List<Contract>  listContact = contractRepository.findAllByIsActiveTrueAndTenant_Room_House_Id(house.getId());
            for (Contract contract : listContact) {
                LocalDate toDate = convertToLocalDateViaInstant(contract.getToDate());
                long days = today.until(toDate, ChronoUnit.DAYS);
                //neu hop sap het han
                        if (days<=30 && days>0) {
                        numberContractExpired30days=numberContractExpired30days+1;
                }
            }
        }

        InformationStatistic informationStatistic = new InformationStatistic();
        informationStatistic.setNumberHouse(numberHouse);
        informationStatistic.setNumberRoom(numberRoom);
        informationStatistic.setNumberEmptyRoom(numberRoomEmpty);
        informationStatistic.setNumberContractExpired30days(numberContractExpired30days);

        return informationStatistic;
    }

    public InformationStatistic getHouseInfor(long houseId){

        Integer numberHouse = 1;
        Integer numberRoom = 0;
        Integer numberRoomEmpty =0;
        Integer numberContractExpired30days=0;

        LocalDate today = LocalDate.now();

        House house = houseRepository.findById(houseId).orElseThrow(() -> new BadRequestException("Nhà không tồn tại!"));

            numberRoom = numberRoom+roomRepository.countByHouse_Id(house.getId());
            List<Room> listRoom = roomRepository.findAllByHouse_Id(house.getId());
            for(Room room : listRoom){
                if(room.getCurrentTenant()==0){
                    numberRoomEmpty=numberRoomEmpty+1;
                }
            }

            List<Contract>  listContact = contractRepository.findAllByIsActiveTrueAndTenant_Room_House_Id(house.getId());
            for (Contract contract : listContact) {
                LocalDate toDate = convertToLocalDateViaInstant(contract.getToDate());
                long days = today.until(toDate, ChronoUnit.DAYS);
                //neu hop sap het han
                if (days<=30 && days>0) {
                    numberContractExpired30days=numberContractExpired30days+1;
                }
            }


        InformationStatistic informationStatistic = new InformationStatistic();
        informationStatistic.setNumberHouse(numberHouse);
        informationStatistic.setNumberRoom(numberRoom);
        informationStatistic.setNumberEmptyRoom(numberRoomEmpty);
        informationStatistic.setNumberContractExpired30days(numberContractExpired30days);

        return informationStatistic;
    }


}
