package com.fu.lhm.tenant;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fu.lhm.bill.entity.Bill;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenantId")
    @JsonBackReference
    Tenant tenant;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER)
    @JsonManagedReference
    List<Bill> bills;
}
