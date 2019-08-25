https://www.ibm.com/developerworks/cn/cloud/library/cl-cn-bluemix-kubernetes-cluster-practice1/index.html
https://www.ibm.com/developerworks/cn/cloud/library/cl-getting-started-docker-and-kubernetes/index.html
https://www.ibm.com/developerworks/cn/cloud/library/cluster-api-manage-your-kubernetes-cluster-in-a-kubernetes-way/index.html


### Bluemix Kubernetes 实战入门
https://www.ibm.com/developerworks/cn/cloud/library/cl-cn-bluemix-kubernetes-cluster-practice1/index.html

### Docker 和 Kubernetes 入门
https://www.ibm.com/developerworks/cn/cloud/library/cl-getting-started-docker-and-kubernetes/index.html

### 集群 API：以 Kubernetes 原生方式管理 Kubernetes 集群
https://www.ibm.com/developerworks/cn/cloud/library/cluster-api-manage-your-kubernetes-cluster-in-a-kubernetes-way/index.html


https://github.com/AliyunContainerService/minikube


### 检查CPU是否支持虚拟化
```shell
grep -E '(vmx|svm)' /proc/cpuinfo
```

### 安装Docker(使用本机来运行k8s组件)
```shell
$ wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
$ wget -O /etc/yum.repos.d/docker-ce.repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
$ yum clean all && yum makecache
$ yum install -y docker-ce libvirt libvirt-client qemu-kvm qemu-img
# [Docker Hub 镜像加速器](https://segmentfault.com/a/1190000019115546)
$ curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s http://f1361db2.m.daocloud.io
$ systemctl daemon-reload && systemctl restart docker.service
# 确认加速器生效
$ docker info
```

[AliyunContainerService/minikube forked from kubernetes/minikube](https://github.com/AliyunContainerService/minikube)
[Minikube - Kubernetes本地实验环境](https://yq.aliyun.com/articles/221687)


### 安装并使用 minikube 创建 k8s 单节点集群
```shell
# 安装 kubectl/minikube
$ export k8s_ver=$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)
$ curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
$ curl -Lo kubectl https://storage.googleapis.com/kubernetes-release/release/${k8s_ver}/bin/linux/amd64/kubectl
$ curl -Lo minikube http://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/releases/v1.2.0/minikube-linux-amd64
$ chmod u+x minikube kubectl && mv minikube kubectl /usr/local/bin
# 启动k8s集群
$ minikube start --vm-driver=none
$ minikube start --registry-mirror=https://registry.docker-cn.com
# 支持不同的 kubernetes
$ minikube start --registry-mirror=https://registry.docker-cn.com --kubernetes-version v1.12.1
$ kubectl cluster-info
$ minikube ssh # 检查虚拟机里面的容器是否起来
$ minikube dashboard
```

### 自己构建
git clone https://github.com/AliyunContainerService/minikube
cd minikube
git checkout aliyun-v1.2.0
make
cp out/minikube /usr/local/bin/

### 成功输出日志
root@localhost ~/project/k8s$ minikube start --vm-driver=none
* minikube v1.2.0 on linux (amd64)
* using image repository registry.cn-hangzhou.aliyuncs.com/google_containers
* Creating none VM (CPUs=2, Memory=2048MB, Disk=20000MB) ...
* Configuring environment for Kubernetes v1.15.0 on Docker 19.03.1
* Pulling images ...
* Launching Kubernetes ...
* Configuring local host environment ...

! The 'none' driver provides limited isolation and may reduce system security and reliability.
! For more information, see:
  - https://github.com/kubernetes/minikube/blob/master/docs/vmdriver-none.md

! kubectl and minikube configuration will be stored in /root
! To use kubectl or minikube commands as your own user, you may
! need to relocate them. For example, to overwrite your own settings:

  - sudo mv /root/.kube /root/.minikube $HOME
  - sudo chown -R $USER $HOME/.kube $HOME/.minikube

* This can also be done automatically by setting the env var CHANGE_MINIKUBE_NONE_USER=true
* Verifying: apiserver proxy etcd scheduler controller dns
* Done! kubectl is now configured to use "minikube"
* For best results, install kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl/



### RBAC
> k8s 1.13 开启了 RBAC, 所以需要创建一个 RBAC 认证
kubectl get role -n kube-system

### [Kubernetes /Minikube – Enable Dashboard – RHEL 7 / CentOS 7](https://www.unixarena.com/2019/05/kubernetes-minikube-install-and-enable-dashboard.html/)
```shell
# 启动本地 k8s dashboard 容器
$ minikube dashboard --url
# 设置 remote 代理
$ kubectl proxy --address='192.168.100.135' --disable-filter=true
# 远程访问k8s dashboard集群
$ http://192.168.100.135:8001/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy/
```



https://kubernetes.io/docs/setup/learning-environment/minikube/
https://kubernetes.feisky.xyz/ji-chu-ru-men/index
https://jimmysong.io/kubernetes-handbook/concepts/networking.html
https://blog.csdn.net/aixiaoyang168/article/details/78356033



kubectl get pods --all-namespaces
kubectl logs -f pod_name


# 创建 example 部署
$ kubectl run example --image=tomcat:8.0 --port 8080
# 发布服务
$ kubectl expose deployment example --type=NodePort
# 查看 pods
$ kubectl get pods
# 获取服务地址
$ kubectl service example --url
# 导入环境变量
$ eval $(minikube docker-env)



















### network namespace
```shell
# 检查网络命名空间
$ ip netns list => /var/run/netns
# 创建 test 网络命名空间
$ ip netns add test
# 检查网络命名空间
$ ip netns exec test /bin/bash --rcfile <(echo "PS='netns test> '")
# 激活环回接口
$ ip netns exec test ip link set lo up
# 检查激活的接口
$ ip netns exec test ip link
# 删除网络命名空间
$ ip netns delete test


# network namespace 通信(veth pair)
# 创建两个网络 test1/test2
$ ip netns add test0
$ ip netns add test1
# 创建一对 veth pair(veth0 <--> veth1)
$ ip link add type veth
$ ip link add veth0 type veth peer name veth1
# 把 veth pair 放到两个网络命名空间
$ ip link set veth0 netns test0
$ ip link set veth1 netns test1
# 激活网络接口并配置IP地址(lo/veth0/veth1)
$ ip netns exec test0 ip link set lo up
$ ip netns exec test0 ip link set veth0 up
$ ip netns exec test0 ip addr add 192.168.13.10/24 dev veth0

$ ip netns exec test1 ip link set lo up
$ ip netns exec test1 ip link set veth1 up
$ ip netns exec test1 ip addr add 192.168.13.20/24 dev veth1
# 添加默认路由
$ ip netns exec test0 ip route add default via 192.168.13.254 dev veth0
$ ip netns exec test1 ip route add default via 192.168.13.254 dev veth1
# 检查路由
$ ip netns exec test0 ip route
$ ip netns exec test1 ip route
# 检查网络连通性
$ ip netns exec test0 ping -c 4 192.168.13.10
$ ip netns exec test0 ping -c 4 192.168.13.20

$ ip netns exec test1 ping -c 4 192.168.13.20
$ ip netns exec test1 ping -c 4 192.168.13.10
# 删除 veth pair(只需要删除一个)
$ ip link delete veth0
# 删除网络命名空间(会删除其中的网络接口)
$ ip netns delete test0
$ ip netns delete test1


# 多 network namespace 通信(bridge)
# 创建 br0 网桥并激活(默认工作在二层,配置ip地址后工作在三层)
$ ip link add br0 type bridge
$ ip link set dev br0 up
$ ip addr add 192.168.13.254/24 dev br0
# 创建两个 network namespace
$ ip netns add test0
$ ip netns add test1
# 创建两对 veth pair
$ ip link add veth00 type veth peer name veth01
$ ip link add veth10 type veth peer name veth11
# 把 veth pair 一端连接到网桥, 一端放到网络命名空间
$ ip link set dev veth00 master br0
$ ip link set dev veth10 master br0

$ ip link set dev veth01 netns test0
$ ip link set dev veth11 netns test1
# 配置两对 veth pair 的 IP 地址并激活
$ ip link set dev veth00 up
$ ip link set dev veth10 up

$ ip netns exec test0 ip link set dev veth01 name eth0  # 重命名网卡
$ ip netns exec test1 ip link set dev veth11 name eth0  # 重命名网卡

$ ip netns exec test0 ip addr add 192.168.13.10/24 dev eth0
$ ip netns exec test0 ip link set dev eth0 up
$ ip netns exec test0 ip link set dev lo up
$ ip netns exec test0 ip route add default via 192.168.13.254 dev eth0

$ ip netns exec test1 ip addr add 192.168.13.20/24 dev eth0
$ ip netns exec test1 ip link set dev eth0 up
$ ip netns exec test1 ip link set dev lo up
$ ip netns exec test1 ip route add default via 192.168.13.254 dev eth0
# 检查网络配置
$ ip netns exec test0 ip addr
$ ip netns exec test0 ip route

$ ip netns exec test1 ip addr
$ ip netns exec test1 ip route
# 检查网络连通性
$ ip netns exec test0 ping -c 2 192.168.13.10
$ ip netns exec test0 ping -c 2 192.168.13.20
$ ip netns exec test0 ping -c 2 192.168.13.254

$ ip netns exec test1 ping -c 2 192.168.13.10
$ ip netns exec test1 ping -c 2 192.168.13.20
$ ip netns exec test1 ping -c 2 192.168.13.254
# 检查网桥
$ brctl show
$ bridge link
# 删除网络命名空间
$ ip netns delete test0
$ ip netns delete test1
# 删除网桥
$ ip link set dev br0 down
$ brctl delbr br0
$ ip link delete br0
```


