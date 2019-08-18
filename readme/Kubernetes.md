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
kubectl get role -n kube-system

