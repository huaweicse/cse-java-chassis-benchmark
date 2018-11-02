# cse-java-chassis-benchmark
This project provides some test scenarios to benchmark performance

## 如何使用基准测试用例
性能对比才有实际意义。这里的测试场景是给对比使用的，因此没有提供具体的指标和结果。
  * 升级新版本的时候，可以在同样的环境下跑这个基准程序，然后修改版本号，跑新版本程序，对比测试结果。
  * 和其他第三方框架进行对比的时候，可以按照下面的场景，使用第三方系统开发，然后在同样的环境下跑这个基准程序，对比性能结果。

## 测试场景一(base-rest)
* 请求流程：测试工具(jmeter, CPTS等） -> client -> server
* 处理链涵盖CSE缺省提供的所有治理功能，包括隔离、流控和负载均衡管理等。
* 其中client和server监听的协议均为HTTP + JSON

## 测试场景二(fast-rest)
* 请求流程：测试工具(jmeter, CPTS等） -> client -> server
* 处理链为空。
* 其中client和server监听的协议均为HTTP + JSON

## 测试场景三(base-highway)
* 请求流程：测试工具(jmeter, CPTS等） -> client -> server
* 处理链涵盖CSE缺省提供的所有治理功能，包括隔离、流控和负载均衡管理等。
* 其中client监听的协议为HTTP + JSON， server监听的协议为Highway + ProtoBuffer

## 测试场景四(fast-highway)
* 请求流程：测试工具(jmeter, CPTS等） -> client -> server
* 处理链为空。
* 其中client监听的协议为HTTP + JSON， server监听的协议为Highway + ProtoBuffer

## tool
本项目提供了一个测试工具，可以调用CSE提供的服务，方便测试。

## 历史测试数据
* 2018-11-02

  * 测试说明

机器为PC，8U16G，在本机同时运行server, client和tool。tool开启10个线程，发送1K字节的数据，返回1K数据。这个数据用于本地开发参考。

  * 测试结果

测试场景|框架|客户端机器数量|线程数量|字节包大小（B）|TPS（w/s）|平均耗时(ms)|cpu利用率|网卡入流量(Mb/s)|出流量(Mb/s)|网卡入包量(/s)|出包量(/s)
--------|------------|--------|-------|--------------|----------|-----------|--------|---------------|------------|-------------|-------|
测试场景一|CSE|1|10|1K|5127|1.901/15.995|-|-|-|-|-|
