package com.pact.abinbev.data.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

  private String name;
  private String brand;
  private BigDecimal price;
  private String type;
  private Integer quantity;
}
