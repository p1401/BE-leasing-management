package com.fu.lhm.statistic.service;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.house.repository.HouseRepository;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.statistic.model.InformationStatistic;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.tenant.repository.TenantRepository;
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
    private final TenantRepository tenantRepository;

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public InformationStatistic getInfor(User user, Long houseId){

        Integer numberHouse = 0;
        Integer numberRoom = 0;
        Integer numberRoomEmpty =0;
        Integer numberContractExpired30days=0;
        Integer numberTenant=0;

        List<House> listHouse = houseRepository.findHouses(user.getId(),houseId);
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
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+days);
                //neu hop sap het han
                        if (days<=30 && days>0) {
                        numberContractExpired30days=numberContractExpired30days+1;
                }
            }

            List<Tenant> listTenant = tenantRepository.findAllByRoom_House_IdAndIsStayTrue(house.getId());
            for(Tenant tenant : listTenant){
                if(tenant.getIsStay()==true){
                    numberTenant=numberTenant+1;
                }
            }
        }

        InformationStatistic informationStatistic = new InformationStatistic();
        informationStatistic.setNumberHouse(numberHouse);
        informationStatistic.setNumberRoom(numberRoom);
        informationStatistic.setNumberEmptyRoom(numberRoomEmpty);
        informationStatistic.setNumberContractExpired30days(numberContractExpired30days);
        informationStatistic.setNumberTenant(numberTenant);
        return informationStatistic;
    }


}
