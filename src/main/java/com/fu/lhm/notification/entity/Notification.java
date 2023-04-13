package com.fu.lhm.notification.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "notifications")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Date dateCreate;

    private Boolean isRead;

    private Long roomId;

    private Long houseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;
}
