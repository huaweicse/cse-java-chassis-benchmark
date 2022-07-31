package com.huawei.cse.banchmark.tool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;
import org.springframework.web.client.RestTemplate;

public class ToolMain {
  private static RestTemplate template = RestTemplateBuilder.create();

  static AtomicLong success = new AtomicLong(0);

  static AtomicLong timeout = new AtomicLong(0);

  static AtomicLong error = new AtomicLong(0);

  public static void main(String[] args) throws Exception {
    Log4jUtils.init();
    BeanUtils.init();

    runTest(40, "1234567890");
  }

  private static void runTest(int threadCount, String message) throws Exception {
    // initialize
    ExecutorService executor = Executors.newFixedThreadPool(threadCount, new ThreadFactory() {
      AtomicLong count = new AtomicLong(0);

      @Override
      public Thread newThread(Runnable r) {
        return new Thread(r, "test-thread" + count.getAndIncrement());
      }
    });
    int count = 100;
    CountDownLatch latch = new CountDownLatch(threadCount * count);
//    for (int i = 0; i < threadCount; i++) {
//      executor.submit(() -> {
//        runTest(message);
//        latch.countDown();
//      });
//    }
//    latch.await();

    // run
    long begin = System.currentTimeMillis();
    for (int i = 0; i < threadCount; i++) {
      executor.submit(() -> {
        for (int j = 0; j < count; j++) {
          runTest(message);
          latch.countDown();
        }
      });
    }

    latch.await();

    System.out.println("==========================================");
    System.out.println(System.currentTimeMillis() - begin);
    System.out.println(success.get());
    System.out.println(timeout.get());
    System.out.println(error.get());
    System.out.println("==========================================");
  }

  private static void runTest(String message) {
    try {
      String result = template.postForObject("cse://client/body", message, String.class);
      if (!message.equals(result)) {
        error.incrementAndGet();
      } else {
        success.incrementAndGet();
      }
    } catch (Throwable e) {
      if (e instanceof InvocationException) {
        if (((InvocationException) e).getStatusCode() == 408) {
          timeout.incrementAndGet();
          return;
        }
      }
      error.incrementAndGet();
    }
  }
}
