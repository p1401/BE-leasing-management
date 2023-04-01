package com.fu.lhm.house;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.room.Room;
import com.fu.lhm.user.User;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;

    @OneToMany(mappedBy = "house_id", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<Room> rooms;

}
