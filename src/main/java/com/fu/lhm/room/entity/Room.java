package com.fu.lhm.room.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.tenant.entity.Tenant;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private int floor;

    private int moneyNotPay;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "houseId")
    @JsonBackReference
    House house;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Tenant> tenants;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    WaterElectric waterElectric;
}