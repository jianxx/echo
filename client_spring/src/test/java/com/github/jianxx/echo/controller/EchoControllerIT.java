package com.github.jianxx.echo.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EchoControllerIT {

  @LocalServerPort
  private int port;

  private URL base;

  @Autowired
  private TestRestTemplate template;

  @BeforeEach
  public void setUp() throws Exception {
    this.base = new URL("http://localhost:" + port + "/");
  }

  @Test
  public void getHello() throws Exception {
    ResponseEntity<String> response = template.getForEntity(base.toString(),
        String.class);
    assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
  }
}