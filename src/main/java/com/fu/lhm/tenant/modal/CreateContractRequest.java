package com.fu.lhm.tenant.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractRequest {

    //contract
    private String houseName;
    private String roomName;
    private String floor;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int deposit;

    //tenant
    private String tenantName;
    private String email;
    private String phone;
    private LocalDate birth;
    private String identityNumber;
    private String address;

}
