package com.fu.lhm.financial;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.lhm.house.House;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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

    private int electricNumber;

    private int waterNumber;

    private int electricMoney;

    private int waterMoney;

    private String payer;

    private boolean isPay;

    private LocalDate dateCreate;

    private String description;

    private int totalMoney;

    //Phieu thu, phieu chi
    @Enumerated(EnumType.STRING)
    private BillType billType;

    //Tien phong hoac tien thu them
    @Enumerated(EnumType.STRING)
    private BillContent billContent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "", allowGetters = true)
    private Room room;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "", allowGetters = true)
    private House house;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "", allowGetters = true)
    private Contract contract;

}
