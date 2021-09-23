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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolPerf {
  static AtomicLong switchTime;

  static AtomicLong execTime;

  static int executeCount = 10000000;

  static CountDownLatch latch;

  static class Job implements Runnable {
    long begin;

    int runTimes;

    double value;

    Job(int runTimes) {
      begin = System.nanoTime();
      this.runTimes = runTimes;
    }

    @Override
    public void run() {
      long beginExec = System.nanoTime();
      switchTime.addAndGet(System.nanoTime() - begin);

      double temp = 0.3f;
      for (int i = 0; i < runTimes; i++) {
        temp = temp + 3.173333F;
      }
      this.value = temp;

      execTime.addAndGet(System.nanoTime() - beginExec);
      latch.countDown();
    }

    public double getValue() {
      return this.value;
    }
  }

  public static void main(String[] args) throws Exception {
    int threadSize = Integer.parseInt(args[0]);
    int runTimes = Integer.parseInt(args[1]);

    double v = 0f;

    Executor executor = Executors.newFixedThreadPool(threadSize);
    for (int j = 0; j < threadSize; j++) {
      long begin = System.currentTimeMillis();
      latch = new CountDownLatch(executeCount);
      switchTime = new AtomicLong(0);
      execTime = new AtomicLong(0);
      long submitBegin = System.currentTimeMillis();
      for (int i = 0; i < executeCount; i++) {
        Job job = new Job(runTimes);
        executor.execute(job);
        v = v + job.getValue();
      }
      long submitTime = System.currentTimeMillis() - submitBegin;
      latch.await();
      System.out.println(
          "switch time (nanos): " + (switchTime.get() / executeCount)
              + ", submit time (millis):" + submitTime
              + ", round time (millis):" + (System.currentTimeMillis() - begin)
              + ", exec time (nanos):" + (execTime.get() / executeCount)
              + ", tps:" + (executeCount) / ((System.currentTimeMillis() - begin) / 1000)
              + ", test:" + v
      );
    }
  }
}
