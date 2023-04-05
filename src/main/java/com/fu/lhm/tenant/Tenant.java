package com.fu.lhm.tenant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.room.Room;
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
    private Date identifyDate;
    private Date birth;
    private String address;
    private Boolean isContractHolder;
    private Boolean isBookRoom;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId")
    @JsonBackReference
    Room room;

    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Contract> contracts;
}
