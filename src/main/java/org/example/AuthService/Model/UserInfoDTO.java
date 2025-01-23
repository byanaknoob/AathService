package org.example.AuthService.Model;

import org.example.AuthService.Entity.UserInfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDTO extends UserInfo
{
   private String userName;
   
   private String lastName;
   
   private Long phoneNumber;
   
   private String email;
   
   
}
