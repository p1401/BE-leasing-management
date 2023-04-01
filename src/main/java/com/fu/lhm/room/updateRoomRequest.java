package com.fu.lhm.room;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class updateRoomRequest {

    private Long roomId;

    private String name;

    private int bedroomNumber;

    private int area;

    private int chiSoDauDien;

    private int chiSoDauNuoc;

    private int chiSoCuoiDien;

    private int chiSoCuoiNuoc;

    private int floor;

    private boolean haveBookRoom;


}
