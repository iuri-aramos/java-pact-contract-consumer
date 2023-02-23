package com.pact.abinbev.data.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(value = "orders")
public class Order {

  @Id
  @Field("orderNumber")
  @Indexed
  private String orderNumber;

  private List<Item> items;
}
