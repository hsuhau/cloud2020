# Spring Cloud

![](image\Cloud升级.png)

## 服务注册中心

### 什么是服务注册

- [ ] [Eureka](https://github.com/Netflix/eureka)采用了CS的设计框架，Eureka Server作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用Eureka的客户端连接到Eureka的客户端连接到Eureka Server并维持心跳连接。这样系统的维护人员就可以通过Eureka Server来监控系统中各个微服务是否正常运行。

- [ ] 在服务注册与发现中，有一个注册中心。当服务器启动的时候，会把当前服务器的信息比如服务器地址通讯地址等以别名方式注册到注册中心上。另一方（消费者|服务提供者），以该别名的方式去注册中心上获取到实际的服务器通讯地址，然后再实现本地RPC调用，RPC远程调用框架核心设计思想：在于注册中心，因为使用注册中心管理每个服务与服务之间的一个依赖关系（服务治理概念）。在任何RPC远程框架中，都会有一个注册中心（存放服务地址相关信息（接口地址））

- [ ] ```mermaid
  graph BT
  A[Eureka Server]
  B[Service Consumer]
  C[Service Provider]
  B <-- Get Registry --> A
  B -->|Remote Call| C
  C --Register/Review/Cancel--> A
  
  D[Registry]
  E[Consumer]
  F[Provider]
  G[Container]
  H[Monitor]
  D -.->|3.notify| E
  E -.->|2.subscribe| D
  E -->|4.invoke| G
  E -->|4.invoke| F
  G -.->|0.start| F
  G -.-> H
  E -.-> H
  F -.->|1.register| D
  ```

- [ ] Eureka架构图<br>![](https://github.com/Netflix/eureka/blob/master/images/eureka_architecture.png?raw=true)

- [ ] 架构图<br>

- [ ] ![](https://dubbo.apache.org/img/architecture.png)

### Eureka集群原理说明

- [ ] 服务注册
  - [ ] 将服务信息注册进注册中心
- [ ] 服务发现
  - [ ] 从注册中心获取服务信息
- [ ] 实质
  - [ ] 存key服务命令 取value调用地址
- [ ] 流程
  1. 先启动eureka注册中心
  2. 启动服务提供者payment支付服务
  3. 支付服务启动后会把自身信息（比如服务地址以别名方式注册进eureka）
  4. 消费者order服务在需要调用接口时，使用服务别名去注册中心获取实际的PRC远程调用地址
  5. 消费者获得调用地址后，底层实际是利用HttpClient技术实现远程调用
  6. 消费者获得服务地址后会缓存在本地JVM内存中，默认每间隔30秒更新一次服务调用地址
- [ ] 问题
  1. [ ] 微服务RPC远程服务调用最核心的是什么	
     1. [ ] 高可用，试想你的注册中心只有一个，他出故障了那就呵呵了，会导致整个微服务环境不可用
- [ ] 解决办法
  - [ ] 搭建eureka注册中心集群，实现负载均衡+故障容错（互相注册，相互守望）
  - [ ] ![](image\Eureka.png)



### 8. [Ribbon](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html)

* [Introduction to Spring Cloud Rest Client with Netflix Ribbon](https://www.baeldung.com/spring-cloud-rest-client-with-netflix-ribbon)

#### 定义

* Spring Cloud Ribbon是Netflix Ribbon实现的一套客户端 负载均衡的工具
* 简单地说，Ribbon是Netflix发布的开源项目，主要功能是根据客户端软件故在均衡算法的服务调用。Ribbon客户端组件提供了一系列完善的配置项如连接超时，重试等。简单地说，就是在配置文件中列出Load Balance（简称LB）后面所有的机器，Ribbon会自动地帮助你基于某种规则（如简单轮询，随机连接等）去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。

#### LB负载均衡是什么

* 简单地说就是将用户的请求平坦的分配到多个服务器上，从而达到系统的HA（高可用）

#### 常见的负载均衡

* 软件
  * Nginx
  * LVS
* 硬件
  * F5

#### Ribbon本地负载均衡客户端VS Nginx服务端负载均衡区别

* [Nginx](https://nginx.org/en/)是服务端负载均衡，客户端所有请求都会交给nginx，然后由nginx实现转发请求。即负载均衡是由服务端实现的。
* Ribbon本地负载均衡，在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术。

#### 集中式LB

* 即在服务的消费方和提供方之间使用孤立的LB设施（可以是硬件，如F%，也可以是软件，如Nginx），由该设施负责把访问请求通过某种策略转发至服务的提供方；

#### 进程内LB

* 将LB逻辑集成到消费方，消费方从服务注册中心获知有那些地址可用，然后自己再从这些地址中选择出一个合适服务器。
* Ribbon就属于进程内LB，它只是一个类库，集成于消费方进程，消费方通过它获取到服务提供方的地址。

Ribbon在工作时分成两步

* 第一步先选择EurekaServer，它优先选择在同一个区域内负载较少的server

* 第二步再根据用户指定的策略，在从server取到的服务注册列表中选择一个地址

* 其中Ribbon提供了多种策略：比如轮询、随机和根据响应时间加权

Ribbon常用机制

* `com.netflix,loadbalancer.RoundBobinRule`
  * 轮询
* `com.netflix.loadbalancer.RandomRule`
  * 随机
* `com.netflix.loadbalancer.RetryRule`
  * 先按照RoundBobinRule的策略获取服务，如果获取服务失败则再执行时间内会进行重试，获取可用的服务
* `WeightedResponseTimeRule 
  * 对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择
* BestAvailableRule
  * 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
* AvaliabilityFilteringRule
  * 先过滤掉故障实例，再选择并发较小的实例
* ZoneAvoidanceRule
  * 默认规则，复合判断server所在区域的性能和server的可用性选择服务器

### 9. Spring Cloud [OpenFeign](https://github.com/Netflix/eureka/blob/master/images/eureka_architecture.png?raw=true)

Feign是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单

它的使用方法是定义一个服务接口然后在上面添加注解。Feign也支持可热拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，十七支持了Spring MVC标准注解和HttpMessageConverters。Feign可以与Eureka和Ribbon组合使用以支持负载均衡

Feign能干什么

* Feign旨在使编写Java Http客户端变得更容易
* 前面在使用Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模板化的调用方法。到那时在实际开发中，由于对服务以来的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以，Feign在此基础上做了进一步封装，由它来帮助我们定义和实现依赖服务接口的定义。在Feign的实现下，我们只需创建一个接口并使用注解的方式来配置它（以前是Dao接口上面标注Mapper注解，现在是一个微服务接口上面标注一个Feign注解即可），即可晚会曾对服务提供方的接口绑定，简化了使用Spring Cloud Ribbon时，自动封装服务调用客户端的开发量。
* Feign继承了Ribbon
  * 利用Ribbon维护了Payment的服务列表信息，并且通过轮询实现了客户端的负载均衡。而与Bibbon不同的是，通过Feign只需要定义服务绑定接口且以声明式的方法，优雅而简单的实现了服务调用

### 10. [Hystrix](https://github.com/Netflix/Hystrix)

分步实施系统面临的问题

* 复杂分布式体系结构中的应用程有数十个依赖关系，每个依赖关系在某些时候将不可避免地失败。

服务雪崩

* 每个微服务见调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C有调用其他的微服务，这就是所谓的“扇出”。如果删除的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的“雪崩效应”。

* 对于高流量的应用来说，单一的后端以来可能会导致服务器上的所有资源都会在几秒内饱和。比失败更糟糕的是，这些应用程序还可能导致服务之间的延迟增加，备份队列，线程和其他系统资源紧张，导致整个系统发生更多的级联故障。这些都表示需要对故障和延迟进行隔离和管理，一边单个依赖关系的失败，不能取消整个应用程序或系统。

* 所以通常当你发现一个模板下的某个实例失败后，这时候这个模板依然还会接收流量，然后这个有问题的模块还调用了其他的模块，这样就会发生级联故障，或者叫雪崩。

概述

* Hystrix是一个用于处理分布式系统的**延迟**和**容错**的开源库，在分布式系统荣立，许多原来不可避免的会调用失败，比如超市、异常等，Hystrix能够保证在一个依赖出问题的情况下，**不会导致整个服务失败，避免级联故障，以提高分布式系统的弹性。**

* “断路器”本身是一种开服安装之，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），**向调用方返回一个符合预期的。可处理的备选响应（FallBack)，而不是长时间的等待或者抛出调用方无法处理的异常**，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。 

重要概念

* 服务降级
  * fallback
  * 服务器忙，其稍后再试，不让客户端等待并立刻返回一个友好提示
  * 哪些情况下会触发降级
    * 程序运行异常
    * 超时
    * 服务熔断
    * 线程池/信号量打满
* 服务熔断
  * break
  * 类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示
  * 保险丝
    * 服务降级:arrow_right:进而熔断:arrow_right:恢复调用链路 
* 服务限流
  * flowlimit
  * 秒杀，高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进

案例

* 故障现象和导致原因
  * 8001同一层次的其他接口服务被困死，因为tomcat线程池里面的工作线程已经被挤占完毕
  * 80此时调用8001，客户端访问响应缓慢，转圈圈

* 解决
  * 对方服务（8001）超时了，调用者（80）不能一直卡死等待，必须有服务降级
  * 对方服务（8001）宕机了，调用者（80）不能一直卡死等待，必须有服务降级
  * 对方服务（80）OK，调用者（80）自己出故障或有自己要求（自己的等待时间小于服务器响应时间）
* 服务降级
  * 降级配置
    * @HystrixCommand
  * 8001先从自身找问题
    * 设置自身调用超时时间的峰值，峰值内可以正常运行
    * 超过了需要有兜底的方法处理，做服务降级fallback
  * 8001 fallback
    * 业务类启动
      * @HystrixCommand报异常后如何处理
    * 主启动类激活
      * 添加新注解@EnableCircuitBreaker
  * 80 fallback
  * 

熔断机制

* 概述
  * 熔断机制是应对雪崩效应的一种微服务链路保护机制。当删除链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而溶遁该节点微服务的调用，快速返回错误的响应信息。
  * 当检测到该节点微服务调用响应正常后，恢复调用链路。
* 在Spring Cloud框架中，熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，
* 当失败的调用到一定阈值，缺省时5秒内20次调用失败，就会启动熔断机制。熔断机制的注解是@HystrixCommand。

[CircuitBreaker](https://martinfowler.com/bliki/CircuitBreaker.html)

![](https://martinfowler.com/bliki/images/circuitBreaker/state.png)

熔断类型

* 熔断打开
  * 请求不再进行调用当前服务，内部设置时钟一般为MTTR（平均故障处理时间），当打开时长达到所设施中则进入半熔断状态
* 熔断关闭
  * 熔断关闭不会对服务进行熔断
* 熔断半开
  * 部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断

官网断路器流程图![](image\断路器流程图.png)

断路器打开之后

* 再有请求调用的时候，将不会调用主逻辑，而是直接调用降级fallback，实现了自动地发现错误并将降级逻辑切换为主逻辑，减少响应延迟的效果。
* 原来的主逻辑要如何恢复呢？
  * 对于这个问题，Hystrix因为我们实现了自动回复功能。
  * 当断路器打开，对主逻辑进行熔断之后，Hystrix会启动一个休眠时间窗，在这个时间窗内，降级逻辑是临时的成为主逻辑。
  * 当休眠时间窗到期，断路器将进入半开状态，释放一次请求到原来的主逻辑上，如果次吃请求正常返回，那么断路器将继续闭合，
  * 主逻辑恢复，如果这次请求依然有问题，断路器继续进入打开状态，休眠时间窗重新计时。

All配置

* ![](image\Hystrix全部配置项.png)

Hystrix流程图

* ![](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png)

### 11. [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/)

概述

* Spring Cloud Gateway是Spring Cloud的一个全新项目，基于Spring 5.0+Spring Boot2.0和Project Reactor等技术开发的网关，它旨在为微服务提供一种简单有效的统一的API路由管理方式。

* Spring Cloud Gateway作为Spring Cloud生态系统中的网关，目标是替换Zuul，在Spring Cloud2.0以上版本始终，没有对新版本的Zuul2.0以上最新高性能版本进行集成，仍然还是使用的Zuul 1.x非Reactor模式的老版本。而为了提升网关的性能，Spring Cloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty。

* Spring Cloud Gateway的目标提供统一的路由方式且基于Filter链的方式提供了网关基本的功能，例如：安全，监控/指标，和限流。

选型

* 一方面因为Zuul 1.x已经进入维护阶段，而且Gateway是Spring Cloud团队研发的，是亲儿子作品，值得信赖，而且很多功能Zuul都没有用起来也非常的简单便捷。

* Gateway是基于异步非阻塞模型上进行开发的，性能方面不需要担心。虽然Netflix早就发布了最新的Zuul 2.x，但Spring Cloud貌似没有整合计划。而且Netflix相关组件都宣布进入维护期；不知前景如何？

* 多方面综合考虑Gateway是很理想的网管选择。

特性

* 基于Spring Framework 5，Project Reactor和Spring Boot 2.0进行构建
* 动态路由
  * 能够匹配任何请求属性
* 可以对路由指定Predicate断言和Filter过滤器
* 集成Hystrix的断路器功能
* 继承Spring Cloud服务发现功能
* 易于编写的Predicate断言和Filter过滤器
* 请求限流功能
* 支持路径重写

与Zuul的区别

* Zuul 1.x是一个基于阻塞I/O的API Gateway
* Zuul 1.x基于servlet2.5使用阻塞架构，它不支持任何长连接（如WebSocket）Zuul的设计模式和Nginx较像，每次I/O操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成。但是差别是Nginx用C++实现，Zuul用Java实现，而JVM本身会有第一次加载较慢的情况，使得Zuul的性能较差。
* Zuul 2.x理念更先进，想基于Netty非阻塞和支持长连接，但Spring Cloud目前还没有整合。Zuul 2.x的性能叫Zuul1.x 有较大提升。在性能方面，根据官方提供的基准测试，Spring Cloud Gateway的RPS（每秒请求数）是Zuul的1.6倍
* Spring Cloud Gateway建立在Spring Framework 5、Project Reactor和Spring Boot2以上，使用非阻塞API
* Spring Cloud Gateway还支持WebSocket，并且与Spring紧密集成拥有更好的开发体验

Zuul 1.x模型

* Spring Cloud中所继承的Zuul版本，采用的是Tomcat容器，使用的是传统的Servlet IO处理模型
* Servlet的生命周期
  * container启动时构造servlet对象并调用servlet init()进行初始化
  * container运行时接受请求，并为每个请求分配一个线程（一般从线程池中获取空闲线程），然后调用service()
  * container关闭时调用servlet destroy()销毁servlet
  * ![](image/Zuul 1.x 模型.png)

上述模式的缺点

* Servlet是一个简单的网络IO模型，

* 求进入servlet container时，servlet container就会为其绑定一个线程，在并发不高的场景下这种模型是适用的。但是一旦高并发（比如抽风用jmeter压），线程数量就会上涨，而线程资源代价是昂贵的（上下文切换，内存消耗大）严重影响请求的处理时间。在一些简单业务场景下，不希望为每个request分配一个线程，只需要一个或几个线程就能赢堆积大并发的请求，这种业务场景下servlet模型没有优势

* 所以Zuul 1.x是基于servlet之上的一个阻塞式处理模型，即spring实现了处理所有request请求的一个servlet（DispatchServlet)并由该servlet阻塞式处理，所以Spring Cloud Zuul为u发巴托servlet模型的弊端。

Spring Cloud Gateway 模型

* WebFlux
  * 优秀的Web框架，比如说：structs2，springmvc等都是基于servlet api与serlvet容器基础之上运行的
  * 但是，在Servlet 3.1 之后有了异步非阻塞的支持。而WebFlux是一个典型非阻塞异步的框架，它的核心是基于Reactor的相关API实现的。相对于传统的web框架来所，它可以运行在诸如Netty，Undertow及支持Servlet 3.1 的容器上。非阻塞式+函数式编程（Spring5 必须让你使用Java 8）
  * Spring WebFlux是Spring 5.0引入的新的响应式框架，区别于Spring MVC，它不需要依赖Servlet API，它是完全异步非阻塞的，并且基于Reactor来实现响应式流规范。

![](image/微服务架构中网关位置.png)

三大核心概念

* Route 路由
  * 路由是构建网关的基本模块，它是由ID，目标URI，一系列的断言和过滤器组成，如果断言为ture则匹配该路由
* Predicate 断言
  * 参考的是Java 8 的 java.util.function.Predicate
  * 开发人员可以匹配HTTP请求中的所有内容（例如请求头或请求参数），如果请求与断言相匹配则进行路由
* Filter 过滤
  * 指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。
* 总结
  * Web请求，通过一些匹配条件，定位到真正的服务节点。并在这个转发过程的前后，进入一些精细化控制。
  * predicate就是我们的匹配条件：而filter，就可以理解为一个无所不能的拦截器。有了这两个元素，再加上目标URI，就可以实现一个具体的路由了

Gateway 工作流程

* 官网总结
  * ![](https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.3.RELEASE/reference/html/images/spring_cloud_gateway_diagram.png)
  * 客户端向Spring Cloud Gateway 发送请求。然后再Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到Gateway Web Handler。
  * Handler 再通过指定的过滤器链来请求发送到我们实际的服务志雄业务逻辑，然后返回
  * 过滤器之间用虚线分开是一位过滤器坑你会再发送代理请求之前或之后执行业务逻辑
  * 过滤器在”pre”类型的过滤器可以做参数校验，权限控制，流量监控，日志输出，协议转换等
  * 在“post”类型的过滤器中可以做相应内容、响应头的修改，日志的输出，流量监控等有着非常重要的作用。
* 核心逻辑
  * 路由转发+执行过滤器链

### Spring Cloud Config

概述

* 是什么
  * Spring Cloud Config 为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。

* 怎么玩
  * 服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口。
  * 客户端则是指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取的加载配置信息配置服务器默认采用git来存储配置信息，这样就有利于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容。

* 能干什么
  * 集中管理配置文件
  * 不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release
  * 运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉取配置自己的信息
  * 当配置发生变动时，服务不需要重启即可感知到配置的变化并应用新的配置
  * 将配置信息以REST接口的形式暴露
* 与GitHub整合配置
  * 由于Spring Cloud Config 默认使用Git来存储配置文件（也有其他文件，比如支持SVN和本地文件）
  * 但是最推荐的还是Git，而且使用的是http/https访问的形式
* 官网[https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.3.RELEASE/reference/html/](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.3.RELEASE/reference/html/)

客户端配置与测试

* bootstrap.yml
  * 是什么
    * application.yml是用户级的资源配置项
    * bootstrap.yml是系统级的，优先级更加高
    * Spring Cloud 会创建一个“Bootstrap Context”，作为Spring应用的Application Context的父上下文。初始化的时候，Bootstrap Context负责从外部资源加载配置属性并解析配置。这两个上下文共享一个从外部获取的Environment
    * Bootstrap属性有高优先级，默认情况下，它们不会被本地配置覆盖。Bootstrap Context和ApplicationContext有着不同的约定，所以新增一个boostrap.yml文件，保证Bootstrap Context和Application Context配置的分离。
    * 要将Client模块下的application.yml文件改为bootstrap.yml，这是很关键的，因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml

### Spring Cloud Bus

概述

* 目的
  * 分布式自动刷新配置功能
  * Spring Cloud Bus配合Spring Cloud Config使用可以实现配置的动态刷新
* 是什么
  * Bus支持两种消息代理：RabbitMQ、Kafka
* 能干嘛
  * Spring Cloud Bus 配合Spring Cloud Config使用可以实现配置的动态刷新。
  * ![](image/Spring Cloud Bus.png)
  * Spring Cloud Bus是用来将分布式系统的节点与轻量级消息系统链接起来的框架，它整合了Java的事件处理机制和消息中间件的功能。Spring Cloud Bus目前支持RabbitMQ和Kafka。
  * Spring Cloud Bus能管理和传播分布式系统间的信息，就像一个分布式执行器，可用于广播状态更改、时间推送等，也可以当作微服务间的通信通道
  * ![](image/Spring Cloud Bus 01.png)
* 为什么被称为总线
  * 什么是总线
    * 在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来。由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线。在总线上的各个实例，都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息。
  * 基本原理
    * ConfigClient实例都监听MQ中同一个topic（默认是Spring Cloud Bus）。当一个微服务刷新数据的时候，他会把这个信息放入到Topic中，这样其它监听同一Topic的服务救恩那个得到通知，然后去更新自身的配置

### Spring Cloud Stream

#### 是什么

* 官方定义Spring Cloud Stream是一个构建消息驱动微服务的框架。
* 应用程序通过inputs或者outputs来与Spring Cloud Stream中binder对象交互。
* 通过我们配置来binding（绑定），而Spring Cloud Stream 的binder对象负责与消息中间件交互
* 所以，我们只要搞清楚如何与Spring Cloud Stream交互就可以方便使用消息驱动的方式
* 通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动
* Spring Cloud Stream为一些供应商的消息中间件产品提供了个性化的自动化配置实现，引用了发布-订阅、消费组、分区的三个核心概念。
* 目前只支持RabbitMQ，Kafka

### Spring Cloud Alibaba Nacos

简介

* 为什么叫Nacos
  * 前四个字母分别为Naming和Configuration前两个字母，最后的s为Service。
* 是什么
  * 一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台
  * Nacos: Dynamic Naming and Configuration Service
  * Nacos就是注册中心 + 配置中心的组合
  * Nacos = Eureka + Config + Bus
* 能干嘛
  * 替代Eureka做服务注册中心
  * 替代Config做服务配置中心
* 官网
  * https://github.com/alibaba/nacos
  * https://nacos.io/zh-cn/

