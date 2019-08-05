[How to install KVM on CentOS 7 / RHEL 7 ](https://www.cyberciti.biz/faq/how-to-install-kvm-on-centos-7-rhel-7-headless-server/)
```shell
# 检查CPU是否支持虚拟化
$ lscpu | grep Virtualization
# 安装工具
$ yum install libvirt qemu-kvm virt-install

# 启动服务
$ systemctl enable libvirtd.service
$ systemctl restart libvirtd.service
$ systemctl status libvirtd.service
$ lsmod | grep kvm

# 配置并检查网络(libvirted默认网桥启用dhcp)
$ brctl show
$ virsh net-list
$ virsh net-dumpxml default
# 使虚拟机可以访问局域网的其他机器
$ echo "BRIDGE=br0" >> /etc/sysconfig/network-scripts/ifcfg-ens33
$ cat <<EOF> /etc/sysconfig/network-scripts/ifcfg-br0
DEVICE="br0"
BOOTPROTO="dhcp"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
ONBOOT="yes"
TYPE="Bridge"
DELAY="0"
EOF
$ systemctl restart network && brctl show


# 创建虚拟机
# 下载ISO镜像
# /var/lib/libvirt/boot/CentOS-7-x86_64-Minimal-1810.iso
# 检查支持的虚拟机OS变体
$ osinfo-query os
# 以下命令会自动创建磁盘文件
$ export vmname="test" && virt-install \
    --virt-type kvm \
    --name ${vmname} \
    --memory 1024 \
    --vcpus 2 \
    --os-variant centos7.0 \
    --network network=default,model=virtio \
    --graphics vnc,listen=0.0.0.0 \
    --noautoconsole \
    --cdrom /var/lib/libvirt/boot/CentOS-7-x86_64-Minimal-1810.iso \
    --disk path=/var/lib/libvirt/images/${vmname}.qcow2,size=5,bus=virtio,format=qcow2
# (可选)手动创建磁盘文件
$ qemu-img create -f qcow2 -o preallocation=metadata /var/lib/libvirt/images/test.img 5G

# 获取虚拟机监听的端口
$ virsh dumpxml ${vmname} | grep vnc
$ virsh vncdisplay ${vmname}

# (可选)端口转发
$ ssh root@vncserver -L 5900:127.0.0.1:5900

# 启动虚拟机
$ virsh start ${vmname}

# 检查虚拟机IP地址
$ vm_mac_addr=$(virsh dumpxml ${vmname} | egrep -o "<mac address='(.*)'/>" | awk -F"'" '{print $2}')
$ vm_ip=$(virsh net-dhcp-leases default | grep ${vm_mac_addr} | awk '{print $5}')

# 检查虚拟机域信息
$ virsh dominfo ${vmname}

# 关闭虚拟机
$ virsh shutdown ${vmname}

# 强行终止虚拟机
$ virsh destroy ${vmname}

# 删除虚拟机
$ virsh undefine ${vmname}

# mkisofs 待定
$ export volume_name="CentOS_7"
$ export img_filename="CentOS_7.x86_64.iso"
$ export root_folder="tmp/"
$ genisoimage -R -J -T -r -l -d -input-charset "utf-8" \
        -allow-multidot -allow-leading-dots -no-bak \
        -b isolinux/isolinux.bin -c isolinux/boot.cat \
        -no-emul-boot -boot-load-size 4 -boot-info-table \
        -V "${volume_name}" -o "${img_filename}" "${root_folder}"
```
