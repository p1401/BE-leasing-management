package com.fu.lhm.user.model;

import lombok.*;

import java.util.Date;

@Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String email;
  private String password;
  private String name;
  private String identityNumber;
  private String phone;
  private String address;
  private Date birth;
}
