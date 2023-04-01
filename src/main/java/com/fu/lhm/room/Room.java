package com.fu.lhm.room;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.house.House;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "rooms")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int roomMoney;

    //So nguoi toi da
    private int maxTenant;

    //So nguoi hien tai
    private int currentTenant;

    private int area;

    private int chiSoCuoiDien;

    private int chiSoCuoiNuoc;

    private int electricNumber;

    private int waterNumber;

    private int floor;

    private boolean haveBookRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id")
    @JsonBackReference
    private House house;
}