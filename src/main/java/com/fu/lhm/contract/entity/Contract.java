package com.fu.lhm.contract.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractCode;
    private Date fromDate;
    private Date toDate;
    private String roomName;
    private String houseName;
    private Boolean isActive;
    private Long deposit;
    private String tenantName;
    private Integer autoBillDate;
    private Long roomId;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Tenant.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "tenantId")
    @JsonBackReference
    Tenant tenant;

    @OneToMany(mappedBy = "contract",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    List<Bill> bills;
}
