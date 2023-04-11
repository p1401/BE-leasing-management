package com.fu.lhm.tenant.model;

import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.room.entity.Room;
import jakarta.persistence.Entity;
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
public class TenantRequest {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String identifyNumber;
    private Date birth;
    private String address;
    private Boolean isStay;
    private Boolean isContractHolder;
    private Room room;
    private House house;
    private List<Contract> contracts;
}
