package com.fu.lhm.tenant;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.lhm.room.Room;
import com.fu.lhm.tenant.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String contractCode;
    private Date fromDate;
    private Date toDate;
    private boolean isActive;
    private int deposit;

    @ManyToOne
    @JsonIgnoreProperties(value = "", allowGetters = true)
    @JoinColumn(name = "tenantId")
    private Tenant tenant;

    @ManyToOne
    @JsonIgnoreProperties(value = "", allowGetters = true)
    @JoinColumn(name = "roomId")
    private Room room;


}
