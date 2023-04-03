package com.fu.lhm.Statistic.Entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatistic {

    private int roomFull;

    private int roomHaveSlot;

    private int roomEmpty;





}
