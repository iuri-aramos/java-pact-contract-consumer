package com.order.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pact.abinbev.OrderApplication;
import com.pact.abinbev.api.payloads.BeerItem;
import com.pact.abinbev.api.payloads.OrderRequest;
import com.pact.abinbev.data.dto.BeerIntegrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = {OrderApplication.class})
@AutoConfigureMockMvc
public class BeerControllerTest {

  private final MockMvc mockMvc;

  private final RestTemplate restTemplate;

  private MockRestServiceServer mockServer;
  private ObjectMapper mapper = new ObjectMapper();

  @Autowired
  public BeerControllerTest(final MockMvc mockMvc, final RestTemplate restTemplate) {
    this.mockMvc = mockMvc;
    this.restTemplate = restTemplate;
  }

  @BeforeEach
  public void init() {
    mockServer = MockRestServiceServer.bindTo(restTemplate).bufferContent().build();
  }

  @Test
  public void shouldReturnDefaultMessage() throws Exception {
    OrderRequest orderRequest = new OrderRequest();
    BeerItem beerItemRequest = new BeerItem();
    beerItemRequest.setId("1");
    beerItemRequest.setQuantity(2);
    orderRequest.setBeers(List.of(beerItemRequest));

    BeerIntegrationResponse itemResponse = new BeerIntegrationResponse();
    itemResponse.setBrand("Ambev");
    itemResponse.setType("Beer");
    itemResponse.setName("Skol");
    itemResponse.setPrice(new BigDecimal("2.32"));
    itemResponse.setId("1");

    mockServer
        .expect(once(), requestTo("http://localhost:8080/beer/1"))
        .andRespond(
            withSuccess(mapper.writeValueAsString(itemResponse), MediaType.APPLICATION_JSON));

    mockMvc
        .perform(
            post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(orderRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString("orderNumber")));

    mockServer.verify();
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
