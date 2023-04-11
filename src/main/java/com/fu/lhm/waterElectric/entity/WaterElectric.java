package com.fu.lhm.waterElectric.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "waterelectrics")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterElectric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int chiSoDauDien;

    private int chiSoDauNuoc;

    private int chiSoCuoiNuoc;

    private int chiSoCuoiDien;

    private int numberWater;

    private int numberElectric;

    private Date dateUpdate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId")
    @JsonBackReference
    Room room;
}
