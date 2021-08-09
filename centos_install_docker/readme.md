#### 1. 安装
##### 1.1 卸载旧版本
```shell
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```
##### 1.2 安装软件包
```shell
sudo yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
```
1.3 设置仓库
```shell
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```
##### 1.4 安装Docker Engine
```shell
sudo yum install -y docker-ce docker-ce-cli containerd.io
```
#### 2. 设置镜像
##### 2.1 创建或修改 
编辑`/etc/docker/daemon.json`文件
```json
{
    "registry-mirrors": [
        "https://1nj0zren.mirror.aliyuncs.com",
        "https://docker.mirrors.ustc.edu.cn",
        "http://f1361db2.m.daocloud.io",
        "https://registry.docker-cn.com"
    ]
}
```
##### 2.2 使配置生效
```shell
systemctl daemon-reload
```
##### 2.3 重启docker
```shell
systemctl restart docker
```
##### 2.4 验证是否生效
查看`docker info`
```
 Registry Mirrors:
  https://1nj0zren.mirror.aliyuncs.com/
  https://docker.mirrors.ustc.edu.cn/
  http://f1361db2.m.daocloud.io/
  https://registry.docker-cn.com/
```