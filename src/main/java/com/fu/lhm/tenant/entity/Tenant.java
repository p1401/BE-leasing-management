package com.fu.lhm.tenant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenant")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String identifyNumber;
    private Date birth;
    private String address;
    private Boolean isStay;
    private Boolean isContractHolder;
    private String roomName;
    private String houseName;
    private Long rID;
    private Long hID;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "roomId")
    @JsonBackReference
    Room room;

    @OneToMany(mappedBy = "tenant",cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    List<Contract> contracts;
}
