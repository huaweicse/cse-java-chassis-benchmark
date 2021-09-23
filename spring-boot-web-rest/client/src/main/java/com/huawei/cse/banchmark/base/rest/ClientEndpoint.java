package com.huawei.cse.banchmark.base.rest;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@RestSchema(schemaId = "ClientEndpoint")
@RequestMapping(path = "/")
public class ClientEndpoint {
  private RestTemplate template = RestTemplateBuilder.create();

  @PostMapping(path = "/body")
  public String body(@RequestBody String body) {
    return body;
//    return template.postForObject("cse://server/body", body, String.class);
  }
}
