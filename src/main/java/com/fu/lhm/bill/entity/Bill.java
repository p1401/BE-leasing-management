package com.fu.lhm.bill.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.tenant.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "bills")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billCode;

    private int roomMoney;

    private int chiSoDauDien;

    private int chiSoDauNuoc;

    private int chiSoCuoiDien;

    private int chiSoCuoiNuoc;

    private int electricNumber;

    private int waterNumber;

    private int electricMoney;

    private int waterMoney;

    private String payer;

    private Boolean isPay;

    private LocalDate dateCreate;

    private String description;

    private int totalMoney;

    //Phieu thu, phieu chi
    @Enumerated(EnumType.STRING)
    private BillType billType;

    //Tien phong hoac tien thu them
    @Enumerated(EnumType.STRING)
    private BillContent billContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contractId")
    @JsonBackReference
    private Contract contract;

    private Long roomId;
//    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Room.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "roomId")
//    @JsonBackReference
//    Room room;
}