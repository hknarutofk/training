# 开发手册

```
curl 'localhost:8080/todo/add' -H 'Content-Type: application/json;charset=UTF-8' -H 'Accept: application/json, text/plain, */*' -d '{"content": "ccc", "gmtCreate" : "2020-05-21 10:01:02"}'
```

## 异常

@ControllerAdvise + @ExceptionHandler注解在@RestController上，会导致原本应当返回500等http状态码，却返回了200，并且吞掉了原有的状态码

AtomicLong 分析
volatile 分析

。。。。。





# 运维手册

操作系统 Fedora 31

## minikube

假定minikube是生产环境k8s集群，其中部署了mysql生产环境

### 安装

```
[yeqiang@localhost training]$ sh -x k8s/install_minicube.sh 
+ set -x -e
+ curl -LO https://storage.googleapis.com/minikube/releases/v1.12.1/minikube-linux-amd64
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 55.7M  100 55.7M    0     0   496k      0  0:01:55  0:01:55 --:--:--  522k
+ sudo install minikube-linux-amd64 /usr/local/bin/minikube
[sudo] password for yeqiang: 
+ sudo dnf install kubernetes-client
Last metadata expiration check: 2:22:37 ago on Wed 12 Aug 2020 12:33:19 PM CST.
Package kubernetes-client-1.15.7-1.fc31.x86_64 is already installed.
Dependencies resolved.
Nothing to do.
Complete!

```

### 启动

```
[yeqiang@localhost training]$ sh k8s/start_minicube.sh 
+ minikube start --driver=docker --image-mirror-country=cn --registry-mirror=https://r6w9c7qa.mirror.aliyuncs.com --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers --iso-url=https://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/iso/minikube-v1.12.2.iso --base-image=registry.cn-hangzhou.aliyuncs.com/google_containers/kicbase:v0.0.10
😄  minikube v1.12.1 on Fedora 31
✨  Using the docker driver based on user configuration
✅  Using image repository registry.cn-hangzhou.aliyuncs.com/google_containers
👍  Starting control plane node minikube in cluster minikube
🔥  Creating docker container (CPUs=2, Memory=8000MB) ...
🎉  minikube 1.12.2 is available! Download it: https://github.com/kubernetes/minikube/releases/tag/v1.12.2
💡  To disable this notice, run: 'minikube config set WantUpdateNotification false'

🐳  Preparing Kubernetes v1.18.3 on Docker 19.03.2 ...
🔎  Verifying Kubernetes components...
🌟  Enabled addons: default-storageclass, storage-provisioner
🏄  Done! kubectl is now configured to use "minikube"
+ minikube addons enable ingress
🔎  Verifying ingress addon...

💣  enable failed: run callbacks: running callbacks: [verifying ingress addon pods : timed out waiting for the condition: timed out waiting for the condition]

😿  minikube is exiting due to an error. If the above message is not useful, open an issue:
👉  https://github.com/kubernetes/minikube/issues/new/choose

```

```
[yeqiang@localhost training]$ minikube addons enable ingress
🔎  Verifying ingress addon...
🌟  The 'ingress' addon is enabled
```

### 查看minikube

```
[yeqiang@localhost k8s]$ kubectl get pods -n kube-system
NAME                                        READY   STATUS      RESTARTS   AGE
coredns-546565776c-p5gr9                    1/1     Running     0          35m
etcd-minikube                               1/1     Running     0          35m
ingress-nginx-admission-create-sgh4d        0/1     Completed   0          35m
ingress-nginx-admission-patch-2g8w5         0/1     Completed   2          35m
ingress-nginx-controller-69ccf5d9d8-z8dwt   1/1     Running     0          35m
kube-apiserver-minikube                     1/1     Running     0          35m
kube-controller-manager-minikube            1/1     Running     0          35m
kube-proxy-xdm27                            1/1     Running     0          35m
kube-scheduler-minikube                     1/1     Running     0          35m
storage-provisioner                         1/1     Running     0          35m

```

```
[yeqiang@localhost k8s]$ kubectl get pods -n kubernetes-dashboard
NAME                                        READY   STATUS    RESTARTS   AGE
dashboard-metrics-scraper-dc6947fbf-qf6nl   1/1     Running   0          18m
kubernetes-dashboard-6dbb54fd95-v88sk       1/1     Running   0          18m

```

### kubenetes dashborad

### 启动

```
yeqiang@localhost k8s]$ minikube dashboard
🤔  Verifying dashboard health ...
🚀  Launching proxy ...
🤔  Verifying proxy health ...
🎉  Opening http://127.0.0.1:37103/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/ in your default browser...
Fontconfig warning: "/usr/share/fontconfig/conf.avail/05-reset-dirs-sample.conf", line 6: unknown element "reset-dirs"
Opening in existing browser session.

```



## kubernetes单节点

```
[yeqiang@localhost training]$ cd k8s/ && sudo sh -x install_kubernetes.sh
```

注意关闭防火墙



## harbor(k8s)

### 安装

```
[yeqiang@localhost training]$ cd k8s/ && sh -x install_harbor.sh
+ set -x -e
+ kubectl create namespace harbor
namespace/harbor created
+ cd openssl-CA
+ kubectl create -n harbor secret tls hknaruto.com --cert=hknaruto.com.pem --key=hknaruto.com.key
secret/hknaruto.com created
+ cd harbor
+ helm install -n harbor harbor .
NAME: harbor
LAST DEPLOYED: Wed Aug 12 15:13:57 2020
NAMESPACE: harbor
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
Please wait for several minutes for Harbor deployment to complete.
Then you should be able to visit the Harbor portal at https://harbor.hknaruto.com
For more details, please visit https://github.com/goharbor/harbor

```

### 检查

```
[yeqiang@localhost k8s]$ kubectl get pods -n harbor
NAME                                           READY   STATUS    RESTARTS   AGE
harbor-harbor-chartmuseum-59b8655f45-k7pfx     1/1     Running   0          33m
harbor-harbor-clair-7d796cbd8b-j5dgd           2/2     Running   6          33m
harbor-harbor-core-5cf749b65d-pmbhl            1/1     Running   6          33m
harbor-harbor-database-0                       1/1     Running   0          33m
harbor-harbor-jobservice-568c7c4bc9-9pnsr      1/1     Running   4          33m
harbor-harbor-notary-server-7796db6c56-nfrqn   1/1     Running   7          33m
harbor-harbor-notary-signer-6dd7bb445d-c26bx   1/1     Running   6          33m
harbor-harbor-portal-b8c64dcf6-cvz8b           1/1     Running   0          33m
harbor-harbor-redis-0                          1/1     Running   0          33m
harbor-harbor-registry-6d6f5b4f88-784qq        2/2     Running   0          33m
harbor-harbor-trivy-0                          1/1     Running   0          33m

```

注意：harbor所有pod处于Running状态需要等待一段时间

### 配置域名

获取ingress服务ip

```
[yeqiang@localhost k8s]$ kubectl get ingress -n harbor 
NAME                           CLASS    HOSTS                        ADDRESS      PORTS     AGE
harbor-harbor-ingress          <none>   harbor.hknaruto.com          172.17.0.2   80, 443   38m
harbor-harbor-ingress-notary   <none>   notary-harbor.hknaruto.com   172.17.0.2   80, 443   38m
```

手动配置本地/etc/hosts，增加记录

```
172.17.0.2	harbor.hknaruto.com
```

### 登陆到harbor

浏览器输入地址https://harbor.hknaruto.com/harbor/sign-in

登陆账号：admin

登陆密码：Harbor12345

### 配置harbor

创建用户：yeqiang

创建harbor工程：yeqiang，并将用户yeqiang设置为该工程Master

### 故障排除

重启宿主服务器后，harbor偶尔有服务出现故障，解决方法：

```
cd k8s/harbor/
helm upgrade -n harbor harbor .
```

多来几次，基本上就可以了。

## MySQL(k8s)

### k8s pet说明?

### 安装

```
[yeqiang@localhost training]$ cd k8s/ && sh install_mysql.sh
+ cd mysql/
+ kubectl apply -f persistentVolume.yml
persistentvolume/mysql configured
persistentvolumeclaim/dev-volume-claim-mysql unchanged
+ kubectl apply -f deployment.yml
deployment.apps/k8s-dev-mysql configured
```

### 查看服务IP、端口

```
[yeqiang@localhost mysql]$ minikube  ip
172.17.0.2
```

```
[yeqiang@localhost k8s]$ kubectl get service | grep mysql
mysql        NodePort    10.111.28.196   <none>        3306:32048/TCP   6m25s
```



### 初始化

1。 启动minikube tunnel

```
[yeqiang@localhost mysql]$ minikube tunnel
[sudo] password for yeqiang: 
Status:	
	machine: minikube
	pid: 273957
	route: 10.96.0.0/12 -> 172.17.0.2
	minikube: Running
	services: []
    errors: 
		minikube: no errors
		router: no errors
		loadbalancer emulator: no errors

```

2。 倒入初始化脚本



## 部署training(k8s)

。。。









