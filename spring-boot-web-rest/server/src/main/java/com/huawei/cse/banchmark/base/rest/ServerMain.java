package com.huawei.cse.banchmark.base.rest;

import org.apache.servicecomb.springboot2.starter.EnableServiceComb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableServiceComb
public class ServerMain {
  public static void main(String[] args) throws Exception {
    try {
      SpringApplication.run(ServerMain.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}