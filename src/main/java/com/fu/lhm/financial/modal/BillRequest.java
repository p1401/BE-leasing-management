package com.fu.lhm.financial.modal;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private LocalDate dateCreate;

    private Integer money;

    private Integer chiSoDauDien;

    private Integer chiSoDauNuoc;

    private Integer chiSoCuoiDien;

    private Integer chiSoCuoiNuoc;

    private Integer totalMoney;

    private String description;

    private String billType;

}
