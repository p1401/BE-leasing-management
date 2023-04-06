package com.fu.lhm.tenant.model;

import com.fu.lhm.tenant.Tenant;
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

    //tenant
    private Tenant tenant;

}
