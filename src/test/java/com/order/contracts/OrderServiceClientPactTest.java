package com.order.contracts;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.pact.abinbev.OrderApplication;
import com.pact.abinbev.api.payloads.BeerItem;
import com.pact.abinbev.data.dto.BeerIntegrationResponse;
import com.pact.abinbev.service.BeerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {OrderApplication.class})
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(pactVersion = PactSpecVersion.V3, providerName = "BeerService")
public class OrderServiceClientPactTest {
  @MockBean private BeerService itemService;

  @Pact(consumer = "OrderService")
  public RequestResponsePact getOneBeer(PactDslWithProvider builder) {

    return builder
        .given("beer exists")
        .uponReceiving("get beer by Id")
        .path("/beer/63c18997cf5ec173590cc15a")
        .willRespondWith()
        .status(200)
        .body(
            new PactDslJsonBody()
                .stringType("id", "63c18997cf5ec173590cc15a")
                .stringType("name", "BHRAMA")
                .stringType("brand", "AMBEV")
                .numberType("price", new BigDecimal("3.54"))
                .stringType("type", "Beer"))
        .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "getOneBeer")
  void testOneBeer(MockServer mockServer) throws ServiceUnavailableException {

    itemService = new BeerService(mockServer.getUrl(), new RestTemplate());
    final BeerIntegrationResponse itemResponseReq =
        itemService.retrieveItemPrice(new BeerItem("63c18997cf5ec173590cc15a", 2));

    assertEquals("63c18997cf5ec173590cc15a", itemResponseReq.getId());
    assertEquals("BHRAMA", itemResponseReq.getName());
    assertEquals("63c18997cf5ec173590cc15a", itemResponseReq.getId());
    assertEquals("AMBEV", itemResponseReq.getBrand());
    assertEquals("Beer", itemResponseReq.getType());
  }

  @Pact(consumer = "OrderService")
  public RequestResponsePact getOtherBeer(PactDslWithProvider builder) {

    return builder
        .given("beer exists")
        .uponReceiving("get beer by Id 2")
        .path("/beer/e00111fc959f11eda1eb0242")
        .willRespondWith()
        .status(200)
        .body(
            new PactDslJsonBody()
                .stringType("id", "e00111fc959f11eda1eb0242")
                .stringType("name", "BHRAMA")
                .stringType("brand", "AMBEV")
                .numberType("price", new BigDecimal("3.54"))
                .stringType("type", "Beer"))
        .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "getOtherBeer")
  void testOtherBeer(MockServer mockServer) throws ServiceUnavailableException {

    itemService = new BeerService(mockServer.getUrl(), new RestTemplate());
    final BeerIntegrationResponse itemResponseReq =
        itemService.retrieveItemPrice(new BeerItem("e00111fc959f11eda1eb0242", 2));

    assertEquals("e00111fc959f11eda1eb0242", itemResponseReq.getId());
    assertEquals("BHRAMA", itemResponseReq.getName());
    assertEquals("AMBEV", itemResponseReq.getBrand());
    assertEquals("Beer", itemResponseReq.getType());
  }
}
