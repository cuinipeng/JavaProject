```shell
# 流控只能控制发包动作,不能控制收包动作.它直接对物理网口生效,
# 如果控制对物理网卡 eth0 限制, 那么逻辑网卡 eth0:1 也会受
# 到影响, 如果在逻辑网卡做流控, 那么该控制可能对物理网卡无效.

#++++++++++++++++++++ 延迟 ++++++++++++++++++++
# 将网卡 eth0 的传输设置为延迟 100ms 发送
$ tc qdisc add dev eth0 root netem delay 100ms

# 带波动的延迟发送(90ms ~ 110ms)
$ tc qdisc add dev eth0 root netem delay 100ms 10ms

# 随机带波动的延迟发送(90ms ~ 100ms/30%)
$ tc qdisc add dev eth0 root netem delay 100ms 10ms 30%

#++++++++++++++++++++ 丢包 ++++++++++++++++++++
# 随机丢掉 eth0 1% 的数据包
$ tc qdisc add dev eth0 root netem lost 1%

#++++++++++++++++++++ 重复 ++++++++++++++++++++
# 随机产生 1% 重复数据包
$ tc qdisc add dev eth0 root netem duplicate 1%

#++++++++++++++++++++ 损坏 ++++++++++++++++++++
# 随机产生 0.5% 损坏的数据包
$ tc qdisc add dev eth0 root netem corrupt 0.5%

#++++++++++++++++++++ 检查 ++++++++++++++++++++
$ tc qdisc show dev eth0
```
