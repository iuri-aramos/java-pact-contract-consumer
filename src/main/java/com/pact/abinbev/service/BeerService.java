package com.pact.abinbev.service;

import com.pact.abinbev.api.payloads.BeerItem;
import com.pact.abinbev.data.dto.BeerIntegrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BeerService {

  Logger logger = LoggerFactory.getLogger(BeerService.class);

  private String itemUrl;
  private final RestTemplate restTemplate;

  @Autowired
  public BeerService(
      @Value("${api.item-service}") String itemUrl, final RestTemplate restTemplate) {
    this.itemUrl = itemUrl;
    this.restTemplate = restTemplate;
  }

  public BeerIntegrationResponse retrieveItemPrice(final BeerItem beerItem)
      throws ServiceUnavailableException {
    URI uri;
    BeerIntegrationResponse body = null;
    try {
      uri = new URI(itemUrl + "/beer/" + beerItem.getId());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> entity = new HttpEntity<>(headers);

      body =
          restTemplate
              .exchange(uri, HttpMethod.GET, entity, BeerIntegrationResponse.class)
              .getBody();
    } catch (final HttpClientErrorException | HttpServerErrorException e) {
      logger.error("An ERROR happens during the communication with item-service.");
      e.printStackTrace();
      if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
        throw new ServiceUnavailableException("");
      }
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    return body;
  }
}
