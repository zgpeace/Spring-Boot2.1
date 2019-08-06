package com.zgpeace.demomybatis;

import com.zgpeace.demomybatis.dao.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemomybatisApplication {

  @Autowired
  private CityMapper cityMapper;

  public static void main(String[] args) {
    SpringApplication.run(DemomybatisApplication.class, args);
  }

  @Bean
  CommandLineRunner sampleCommandLineRunner() {
    return (args) -> {
      System.out.println(cityMapper.findByState("CA"));
    };
  }

}

