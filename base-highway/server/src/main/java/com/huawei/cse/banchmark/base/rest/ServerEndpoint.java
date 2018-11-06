package com.huawei.cse.banchmark.base.rest;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "ServerEndpoint")
@RequestMapping(path = "/")
public class ServerEndpoint {
  @PostMapping(path = "/body")
  public String body(@RequestBody String body) {
    return body;
  }
}
