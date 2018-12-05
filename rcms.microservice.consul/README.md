## 1 Consul简介

Consul 是 HashiCorp 公司推出的开源工具，用于实现分布式系统的服务发现与配置。与其他分布式服务注册与发现的方案，Consul的方案更“一站式”，内置了服务注册与发现框 架、分布一致性协议实现、健康检查、Key/Value存储、多数据中心方案，不再需要依赖其他工具（比如ZooKeeper等）。使用起来也较 为简单。Consul使用Go语言编写，因此具有天然可移植性(支持Linux、windows和Mac OS X)；安装包仅包含一个可执行文件，方便部署，与Docker等轻量级容器可无缝配合 。

## 2 Consul安装

从consul官网: https://www.consul.io/downloads.html ,进行下载就好,解压consul_0.6.4_darwin_amd64.zip,将解压后的二进制文件consul（拷贝到/usr/local/bin下）; sudo scp consul /usr/local/bin

## 3 Consul 启动

./consul agent -dev              # -dev表示开发模式运行，另外还有-server表示服务模式运行
consul agent -dev -client 127.0.0.1

说明：
-dev（该节点的启动不能用于生产环境，因为该模式下不会持久化任何状态），该启动模式仅仅是为了快速便捷的启动单节点consul
* 该节点处于server模式 
* 该节点是leader 
* 该节点是一个健康节点 

consul members   #查看consul cluster中的每一个consul节点的信息

说明：
* Address：节点地址
* Status：alive表示节点健康
* Type：server运行状态是server状态
* DC：dc1表示该节点属于DataCenter1 

### 4 停止服务（优雅退出）
命令：CTRL+C
说明：
* 该节点离开后，会通知cluster中的其他节点

## 5 Consul常用命令

1. agent	;运行一个consul agent;	consul agent -dev
1. join	;将agent加入到consul集群;	consul join IP
1. members	;列出consul cluster集群中的members;	consul members
1. leave	;将节点移除所在集群;	consul leave

* consul agent 命令的常用选项，如下：

* -data-dir 
   * 作用：指定agent储存状态的数据目录
   * 这是所有agent都必须的
   * 对于server尤其重要，因为他们必须持久化集群的状态
* -config-dir 
   * 作用：指定service的配置文件和检查定义所在的位置
   * 通常会指定为”某一个路径/consul.d”（通常情况下，.d表示一系列配置文件存放的目录）
* -config-file 
   * 作用：指定一个要装载的配置文件
   * 该选项可以配置多次，进而配置多个配置文件（后边的会合并前边的，相同的值覆盖）
* -dev 
   * 作用：创建一个开发环境下的server节点
   * 该参数配置下，不会有任何持久化操作，即不会有任何数据写入到磁盘
   * 这种模式不能用于生产环境（因为第二条）
* -bootstrap-expect 
   * 作用：该命令通知consul server我们现在准备加入的server节点个数，该参数是为了延迟日志复制的启动直到我们指定数量的server节点成功的加入后启动。
* -node 
   * 作用：指定节点在集群中的名称
   * 该名称在集群中必须是唯一的（默认采用机器的host）
   * 推荐：直接采用机器的IP
* -bind 
   * 作用：指明节点的IP地址
   * 有时候不指定绑定IP，会报Failed to get advertise address: Multiple private IPs found. Please configure one. 的异常
* -server 
   * 作用：指定节点为server
   * 每个数据中心（DC）的server数推荐至少为1，至多为5
   * 所有的server都采用raft一致性算法来确保事务的一致性和线性化，事务修改了集群的状态，且集群的状态保存在每一台server上保证可用性
   * server也是与其他DC交互的门面（gateway）
* -client 
   * 作用：指定节点为client，指定客户端接口的绑定地址，包括：HTTP、DNS、RPC
   * 默认是127.0.0.1，只允许回环接口访问
   * 若不指定为-server，其实就是-client
* -join 
   * 作用：将节点加入到集群
* -datacenter（老版本叫-dc，-dc已经失效） 
   * 作用：指定机器加入到哪一个数据中心中


## 6 Consul 的高可用

这边准备了三台CentOS 7的虚拟机，主机规划如下，供参考：

主机名称	IP	作用	是否允许远程访问
* node0	192.168.11.143	consul server	是
* node1	192.168.11.144	consul client	否
* node2	192.168.11.145	consul client	是
### 搭建步骤方法1
1. 启动node0机器上的Consul（node0机器上执行）：
* consul agent -data-dir /tmp/node0 -node=node0 -bind=192.168.11.143 -datacenter=dc1 -ui -client=192.168.11.143 -server -bootstrap-expect 1
1. 启动node1机器上的Consul（node1机器上执行）：
* consul agent -data-dir /tmp/node1 -node=node1 -bind=192.168.11.144 -datacenter=dc1 -ui
1. 启动node2机器上的Consul（node2机器上执行）：
* consul agent -data-dir /tmp/node2 -node=node2 -bind=192.168.11.145 -datacenter=dc1 -ui -client=192.168.11.145
1. 将node1节点加入到node0上（node1机器上执行）：
* consul join 192.168.11.143
1. 将node2节点加入到node0上（node2机器上执行）：
* consul join -rpc-addr=192.168.11.145:8400  192.168.11.143
1. 这样一个简单的Consul集群就搭建完成了，在node1上查看当前集群节点：
* consul members -rpc-addr=192.168.11.143:8400

    结果如下：
     * node0  192.168.11.143:8301  alive   server  0.7.0   2         dc1
     * node1  192.168.11.144:8301  alive   client  0.7.0   2         dc1
     * node2  192.168.11.145:8301  alive   client  0.7.0   2         dc1


### 搭建步骤方法2

* 192.168.1.185启动consul

  * consul agent -server -bootstrap-expect 3 -data-dir /tmp/consul -node 192.168.1.185-datacenter huanan –ui

* 192.168.3.152启动consul

  * consul agent -server -bootstrap-expect 3 -data-dir /tmp/consul -node 192.168.3.152-datacenter huanan –ui

* 192.168.1.235启动consul

  * consul agent -server -bootstrap-expect 3 -data-dir /tmp/consul -node 192.168.1.235-datacenter huanan -ui

* 此时三台机器都会打印：

  * 2017/09/07 14:54:26 [WARN] raft: no knownpeers, aborting election

  * 2017/09/07 14:54:26 [ERR] agent: failed tosync remote state: No cluster leader

  * 此时三台机器还未join，不能算是一个集群，三台机器上的consul均不能正常工作，因为leader未选出

三台机器组成consul集群

* 192.168.3.152加入192.168.1.185

  * consul join 192.168.1.185

  * Successfully joined cluster by contacting 1nodes.

* 192.168.1.235加入192.168.1.185

  * consul join 192.168.1.185

  * Successfully joined cluster by contacting 1nodes.

* 很快三台机器都会打印：

  * consul: New leader elected: 192.168.1.235

  * 证明此时leader已经选出，集群可以正常工作
