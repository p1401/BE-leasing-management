package com.fu.lhm.room;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable=false)
    private String name;

    private int bedroomNumber;

    private int area;

    private int electricNumber;

    private int waterNumber;

    private int floor;

    private boolean haveBookRoom;

    @ManyToOne
    @JsonIgnoreProperties(value = "", allowGetters = true)
    @JoinColumn(name = "house_id")
    private House house;

}