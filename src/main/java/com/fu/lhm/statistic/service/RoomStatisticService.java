package com.fu.lhm.statistic.service;

import com.fu.lhm.statistic.model.RoomStatistic;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomStatisticService {

        private final RoomRepository roomRepository;

        public RoomStatistic getRoomStatisticByHouseId(Long houseId){

            RoomStatistic roomStatistic = new RoomStatistic();
            int full =0;
            int haveSlot =0;
            int empty=0;

            List<Room> listRoom = roomRepository.findAllByHouse_Id(houseId);

            for(Room room : listRoom){
                if(room.getCurrentTenant()==room.getMaxTenant()){
                    full+=1;
                }else if(room.getCurrentTenant()<room.getMaxTenant() && room.getCurrentTenant()>0){
                    haveSlot+=1;
                }
                else if(room.getCurrentTenant()==0){
                    empty+=1;
                }
            }
            roomStatistic.setRoomFull(full);
            roomStatistic.setRoomHaveSlot(haveSlot);
            roomStatistic.setRoomEmpty(empty);


            return roomStatistic;
        }



}
