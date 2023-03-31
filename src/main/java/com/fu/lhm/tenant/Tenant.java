package com.fu.lhm.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.lhm.house.House;
import com.fu.lhm.room.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private int phone;
    private String address;
    private boolean isContractHolder;
    private boolean isBookRoom;

    @ManyToOne
    @JsonIgnoreProperties(value = "", allowGetters = true)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JsonIgnoreProperties(value = "", allowGetters = true)
    @JoinColumn(name = "house_id")
    private House house;


}
