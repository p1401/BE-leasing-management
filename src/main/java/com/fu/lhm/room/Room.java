package com.fu.lhm.room;


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
    private String name;
    private int bedroomNumber;
    private int area;
    private int electricNumber;
    private int waterNumber;
    private int floor;
    @ManyToOne
    @JoinColumn(name="houseId", nullable=false)
    private House houseId;
}