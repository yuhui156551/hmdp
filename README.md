# hmdp
## 点评前后端分离项目

### 系统开发及运行环境
1.操作系统：Windows 10

2.Java开发包：JDK 8

3.项目管理工具：Maven 3.6.1

4.项目开发工具：IntelliJ IDEA 2022.2.2 x64

5.数据库：MySQL 8.0.30

6.浏览器：Google Chrome

7.服务器架构：Spring Boot 2.3.12 + MyBatisPlus 3.4.3 + Redis6.2.6 + Nginx

### 馀灰的话多时刻

  我这个人比较喜欢记录，但是很少分享，大多时候是把话说给自己听。今天比较有纪念意义，今天刚好是在学习Java的第三个月的最后一天写下这篇readme文件。自学之路荆棘丛生，遇到困难大多时间也只能靠自己，好就好在学习java的人很多，很多坑前人就给你踩过了，所以在解决问题的时候，本该站在原地迷茫的我，实际却被各路Java大佬指引着冲出重重迷雾，也是有种莫名的幸福感。其实在很多时候我都在仰望他人的强大，但是自己却又不付诸行动，于是别人在无限进步，我仍止步不前，有句话我一直记在心里：提升自己比仰望别人更有意义，如果你能看到这里，那么我希望这句话也能给你一些启发。废话不多说，请你接着往下看吧，馀灰玩星露谷物语去了。

### 前言

  上面的运行环境仅供参考，Redis版本必须大于6.2，否则项目中的一些命令使用不了。
  
  项目基本都是使用Redis解决一些企业开发问题，例如缓存穿透、缓存雪崩、缓存击穿、库存超卖、一人一单，学了Redis就可以做。
  
  想要测试并发问题，可以下载JMeter，网上找教程就行，只需要注意要提前配置好请求头信息。
  
  想测试集群下的并发问题，就需要idea配置多服务启动，只需要注意不同服务端口号要不同，然后修改nginx的conf文件，开启轮询。
  
  技术栈直接看pom文件，SpringBoot2，MybatisPlus，hutool工具包，此项目大量运用此工具包，因为实在太好用，lombok简化实体类的书写，Redisson分布式锁，lua脚本解决原子性问题，前端不用管，因为这是一个真正的前后端分离项目，技术栈不算难，整个项目围绕着Redis来解决问题。
  
  主要实现以下功能：用户注册登录、上传博客、点赞博客、查询商铺、关注博主、抢优惠券等，有一些例如feed流、附近商户、用户签到、UV统计还未实现。太多东西说不完，需要自己体验，具体内容可以google下载octotree插件预览代码或者直接fork到本地看（注释很多，不怕看不懂）。
  
### 项目启动
  首先idea里创建名为hmdp的数据库，然后执行resource目录下的sql文件，修改yml配置文件，把数据库、redis账号密码改成自己的，运行之前请在Redis客户端提前写好命令 XGROUP CREATE stream.orders g1 0 MKSTREAM 并执行（这条命令是关于消息队列的，提前创建好以免运行报错，或者也可以把相关代码注释掉：在VoucherOrderServiceImpl.java的第66行），启动项目，随后启动nginx（最好是在纯英文路径下），访问localhost:8080/login.html。
  
  下面是一些效果图，如果看不了的话，建议科学上网。

### 主页面
 ![image](https://github.com/yuhui156551/hmdp/blob/master/img/20221222132330.png)
### 笔记详情
 ![image](https://github.com/yuhui156551/hmdp/blob/master/img/20221222132351.png)
### 美食类型商铺
 ![image](https://github.com/yuhui156551/hmdp/blob/master/img/20221222132358.png)
### 上传博客
 ![image](https://github.com/yuhui156551/hmdp/blob/master/img/20221222132406.png)
