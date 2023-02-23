package com.pact.abinbev.api.controllers;

import com.pact.abinbev.api.payloads.OrderRequest;
import com.pact.abinbev.api.payloads.OrderResponse;
import com.pact.abinbev.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(final OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<Object> postBeer(@RequestBody final OrderRequest orderRequest) {

    String idOrder = orderService.saveOrder(orderRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse(idOrder));
  }
}
