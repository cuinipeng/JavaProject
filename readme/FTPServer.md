### 安装并配置FTP服务器(CentOS7)
```shell
# 安装软件
$ yum install -y vsftpd
# 启动服务
$ systemctl enable vsftp.service
$ systemctl restart vsftp.service
# 检查端口
$ netstat -nltp | grep 21
# 匿名访问
$ ftp://ftp_server_ip
# 修改配置文件
/etc/vsftpd/vsftpd.conf
    anonymous_enable=NO
    chroot_local_user=YES
# 重启配置生效
$ systemctl restart vsftp.service
# 创建ftp用户
$ useradd ftpuser
$ echo "ftpuser_password" | passwd --stdin ftpuser
$ usermod -d /data/ftp ftpuser
# 限制用户登录
$ usermod -s /sbin/nologin ftpuser
# 分配用户主目录
$ mkdir -p /data/ftp/pub
$ echo "Welcome to use FTP service" > /data/ftp/welcome.txt
# 设置访问权限
$ chmod a-w /data/ftp
$ chmod 777 -R /data/ftp/pub
```