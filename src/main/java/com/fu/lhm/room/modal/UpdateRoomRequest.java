package com.fu.lhm.room.modal;

import lombok.*;


@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomRequest {

    private Long roomId;

    private String name;

    private int bedroomNumber;

    private int area;

    private int roomMoney;

    private int floor;

    private boolean haveBookRoom;


}
