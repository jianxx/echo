package com.github.jianxx.echo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class EchoController {

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

}
