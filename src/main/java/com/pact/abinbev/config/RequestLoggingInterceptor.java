package com.pact.abinbev.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    logRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    logResponse(response);

    return response;
  }

  private void logRequest(HttpRequest request, byte[] body) throws IOException {

    log.debug("===log request start===");
    log.debug("URI: {}", request.getURI());
    log.debug("Method: {}", request.getMethod());
    log.debug("Headers: {}", request.getHeaders());
    log.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
    log.debug("===log request end===");
  }

  private void logResponse(ClientHttpResponse response) throws IOException {

    log.debug("===log response start===");
    log.debug("Status code: {}", response.getStatusCode());
    log.debug("Status text: {}", response.getStatusText());
    log.debug("Headers: {}", response.getHeaders());
    log.debug(
        "Response body: {}",
        StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    log.debug("===log response end===");
  }
}
