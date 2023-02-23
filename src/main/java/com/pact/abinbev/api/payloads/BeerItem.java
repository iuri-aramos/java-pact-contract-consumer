package com.pact.abinbev.api.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerItem {

  private String id;
  private Integer quantity;
}
