package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private Long id;
  private String openId;
  private String avatar;//微信获取
  private String userName;//微信获取
  private String phone;
  private String sex;
  private Integer idNumber;
  private String introduction;
  private LocalDateTime createTime;
  private String verifyCode;

}
