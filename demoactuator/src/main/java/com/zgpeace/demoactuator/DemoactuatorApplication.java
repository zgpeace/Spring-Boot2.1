package com.zgpeace.demoactuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoactuatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoactuatorApplication.class, args);
  }

  @RequestMapping("/hello")
  public String hello() {
    return "Hello Actuator";
  }
}
