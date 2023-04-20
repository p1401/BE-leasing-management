package com.fu.lhm.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long id;

    private String email;
    private String password;
    private String name;
    private String identityNumber;
    private String phone;
    private String address;
    private Date birth;
}
