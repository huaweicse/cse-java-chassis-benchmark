package com.huawei.cse.banchmark.tool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class ToolMain {
  private static RestTemplate template = RestTemplateBuilder.create();

  public static void main(String[] args) throws Exception {
    Log4jUtils.init();
    BeanUtils.init();

    runTest(10, "1234567890");
  }

  private static void runTest(int threadCount, String message) throws Exception {
    // initialize
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        runTest(message);
        latch.countDown();
      });
    }
    latch.await();

    // run 
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        while (true) {
          runTest(message);
        }
      });
    }
  }

  private static void runTest(String message) {
    String result = template.postForObject("cse://client/body", message, String.class);
    if (!message.equals(result)) {
      throw new RuntimeException("error");
    }
  }
}
