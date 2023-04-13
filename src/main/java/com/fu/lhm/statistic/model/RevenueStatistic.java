package com.fu.lhm.statistic.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatistic {

    private int receive;

    private int spend;

    private int revenue;

    private Date date;

}
