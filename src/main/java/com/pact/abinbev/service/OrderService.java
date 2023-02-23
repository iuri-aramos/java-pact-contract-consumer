package com.pact.abinbev.service;

import com.pact.abinbev.api.payloads.OrderRequest;
import com.pact.abinbev.data.domain.Item;
import com.pact.abinbev.data.domain.Order;
import com.pact.abinbev.data.dto.BeerIntegrationResponse;
import com.pact.abinbev.data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

  private final BeerService beerService;
  private final OrderRepository orderRepository;

  @Autowired
  public OrderService(final BeerService itemService, final OrderRepository orderRepository) {
    this.beerService = itemService;
    this.orderRepository = orderRepository;
  }

  public String saveOrder(final OrderRequest beerRequest) {
    final Order order = new Order();
    final List<Item> itemToSave = new ArrayList<>();

    beerRequest
        .getBeers()
        .forEach(
            beerItem -> {
              final BeerIntegrationResponse itemResponse;
              try {
                itemResponse = beerService.retrieveItemPrice(beerItem);
              } catch (ServiceUnavailableException e) {
                throw new RuntimeException(e);
              }

              final Item item = new Item();
              item.setBrand(itemResponse.getBrand());
              item.setName(itemResponse.getName());
              item.setType(itemResponse.getType());
              item.setPrice(itemResponse.getPrice());
              item.setQuantity(beerItem.getQuantity());
              itemToSave.add(item);
            });

    order.setItems(itemToSave);
    order.setOrderNumber(
        "ORDER_" + ((int) Math.floor(Math.random() * (99999 - 10000 + 1) + 10000)));

    final Order orderSaved = orderRepository.save(order);

    return orderSaved.getOrderNumber();
  }
}
