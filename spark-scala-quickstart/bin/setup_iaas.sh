#!/bin/bash
#
# https://www.sdnlab.com/20489.html
#
# OpenStack 核心组件:
#   计算 Nova
#   认证 Keystone - 负责维护每个 Service 的 Endpoint
#   镜像 Glance
#   块存储 Cinder
#   对象存储 Swift
#   网络 Neutron
#
# 查看 Endpoint
# openstack catalog list
#
# 查询 Nova 服务列表
# nova service-list
#
# 查询网络节点信息的命令
# neutron agent-list
#
# OpenStack 社区为了方便使用, 将所有服务的命令统一命令 openstack
# 查询 nova 服务
# openstack compute service list
# 查询网络服务
# openstack network agent list
# 查询所有租户
# openstack project list
# 创建租户
# openstack project create --description "Project desc" project-name
# 删除租户
# openstack project delete project-name
# 查询所有用户
# openstack user list
# 查询镜像
# openstack image list
# 查询具体镜像信息
# openstack image show image_name
# 创建镜像
# openstack image create mycirros --disk-format qcow2 --public --file /root/cirros-0.3.5-x86_64-disk.img
# 查询安全组
# openstack group list
# 创建安全组
# openstack group create group_name --description ""
# 查询环境主机数量
# openstack host list
# 查询某台主机资源
# openstack host show host_name
# 查询网络服务列表
# openstack network agent list
# 查询网络信息
# openstack network list
# 创建外网
# openstack network create --external  outsidenet
# 创建外网子网
# openstack subnet create --allocation-pool start=192.168.13.100,end=192.168.13.200 --subnet-range 192.168.13.0/24 --network outsidenet subnet_name
# 创建内网
# openstack network create --internal --provider-network-type gre insidenet
# 创建内网子网
# openstack subnet create --subnet-range 20.0.0.0/24 --network insidenet --dns-nameserver 114.114.114.114 provider_subent
# 查询端口信息
# openstack port list
# 创建路由器
# openstack router create router_name
# 路由器连接子网
# openstack router add subnet router_name insidenet
# 查询虚拟机数量
# openstack server list
#

IMAGE_FILE="/root/cirros-0.3.5-x86_64-disk.img"
IMAGE_NAME="mycirros"
FLAVOR_NAME="myflavor"
NETWORK_NAME="mynetwork"
VM_NAME="myinstance"

function create_image()
{
    local guest_os_image_file="${IMAGE_FILE}"
    # 创建镜像
    openstack image create "${IMAGE_NAME}" \
        --disk-format qcow2 \
        --public \
        --file "${guest_os_image_file}"
    # 查询创建的镜像
    openstack image list
}

function create_flavor()
{
    openstack flavor create "${FLAVOR_NAME}" \
        --ram 8GB --disk 50GB --vcpus 1 --public
    # 查询创建的规格
    openstack flavor list
}

function create_network()
{
    # 创建运营商网络
    openstack network create "${NETWORK_NAME}" --external
    # 创建子网
    openstack subnet create "${NETWORK_NAME}_subnet" \
        --allocation-pool start=192.168.13.100,end=192.168.13.100 \
        --subnet-range 192.168.13.0/24 \
        --dns-nameserver 114.114.114.114 \
        --gateway 192.168.13.254 \
        --network "${NETWORK_NAME}"
    # 检查创建的网络
    openstack network list
}

function create_vm()
{
    # 创建虚拟机
    openstack server create "${VM_NAME}" \
        --image "${IMAGE_NAME}" \
        --flavor "${FLAVOR_NAME}" \
        --nic net-id="${NETWORK_NAME}"
    # 检查创建的虚拟机
    openstack server show "${VM_NAME}"
    # 查询登录虚拟机的登录 vnc 端口
    openstack console url show "${VM_NAME}"
}

function main()
{
    echo "================== Container Service Platform =================="
    # Step 0 - Import OpenStack Auth Token and Variables
    source set_env
    # Step 1 - Check OpenStack Service Status
    # Step 2 - Create Guest OS Image
    # Step 3 - Create OpenStack Flavor
    # Step 4 - Create Network and Security Group
    # Step 5 - Create Virtual Machines
    # Step 6 - Install CSP Environment
    
}

main "$@"