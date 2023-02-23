package com.pact.abinbev.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerIntegrationResponse {

  private String id;
  private String name;
  private String brand;
  private BigDecimal price;
  private String type;
}
