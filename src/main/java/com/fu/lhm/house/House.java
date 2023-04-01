package com.fu.lhm.house;


import com.fu.lhm.room.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "houses")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 64, nullable = false)
    private String name;
    private String city;
    private String district;
    private String address;
    private int electricPrice;
    private int waterPrice;
    private int floor;
    private String emailUser;

//    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
//    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
//    @ToString.Exclude
//    @JoinColumn(name = "house")
//    List<Room> rooms;

}
