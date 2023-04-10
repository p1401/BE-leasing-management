package com.fu.lhm.room.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roomId")
    @JsonBackReference
    Room room;
}
