/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolPerf2 {
  static AtomicLong switchTime;

  static AtomicLong execTime;

  static int executeCount = 1000000;

  static CountDownLatch latch;

  static class Job implements Runnable {
    long begin;

    Job() {
      begin = System.nanoTime();
    }

    @Override
    public void run() {
      long beginExec = System.nanoTime();
      switchTime.addAndGet(System.nanoTime() - begin);
      latch.countDown();
      execTime.addAndGet(System.nanoTime() - beginExec);
    }
  }

  public static void main(String[] args) throws Exception {
    List<Executor> executorList = new ArrayList<>(100);
    for (int i = 0; i < 100; i++) {
      executorList.add(Executors.newSingleThreadExecutor());
    }

    for (int j = 0; j < 100; j++) {
      long begin = System.currentTimeMillis();
      latch = new CountDownLatch(executeCount);
      switchTime = new AtomicLong(0);
      execTime = new AtomicLong(0);
      long submitBegin = System.currentTimeMillis();

      for (int i = 0; i < executeCount; i++) {
        executorList.get(i % 100).execute(new Job());
      }
      long submitTime = System.currentTimeMillis() - submitBegin;

      latch.await();
      System.out.println(
          "switch time (nanos): " + (switchTime.get() / executeCount)
              + ", submit time (millis):" + submitTime
              + ", round time (millis):" + (System.currentTimeMillis() - begin)
              + ", exec time (nanos):" + (execTime.get() / executeCount));
    }
  }
}
