package com.fu.lhm.contract.model;

import com.fu.lhm.tenant.entity.Tenant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    //contract
    @NotNull
    private long roomId;
    private Date fromDate;
    private Date toDate;
    private Long deposit;
    private int autoBillDate;

    //tenant
    private Tenant tenant;

}
