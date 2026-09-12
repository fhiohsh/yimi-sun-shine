package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author: Joey
 * @Description: 地址簿实体
 * @date:2024/5/20 14:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

  private long id;
  private long userId;
  private String consignee;
  private String sex;
  private String phone;
  private String provinceCode;
  private String provinceName;
  private String cityCode;
  private String cityName;
  private String districtCode;
  private String districtName;
  private String detail;
  private String label;
  private long isDefault;

}
