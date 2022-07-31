package com.huawei.cse.banchmark.base.rest;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netflix.config.DynamicPropertyFactory;

@RestSchema(schemaId = "ServerEndpoint")
@RequestMapping(path = "/")
public class ServerEndpoint {
  @PostMapping(path = "/body")
  public String body(@RequestBody String body) {
    int sleep = DynamicPropertyFactory.getInstance().getIntProperty("test.sleep.time", 0).get();
    if (sleep > 0) {
      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return body;
  }
}
