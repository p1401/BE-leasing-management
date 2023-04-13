package com.fu.lhm.bill.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.user.entity.User;
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

    private long roomId;

    private long houseId;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;



}
