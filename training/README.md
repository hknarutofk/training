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

#### nginx-ingress-controller

编辑/etc/ansible/manifests/ingress/nginx-ingress/nginx-ingress-hostport.yaml 

采用hostNetwork模式，在宿主机器上开80 443端口

```
apiVersion: v1
kind: Namespace
metadata:
  name: ingress-nginx

---

kind: ConfigMap
apiVersion: v1
metadata:
  name: nginx-configuration
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
kind: ConfigMap
apiVersion: v1
metadata:
  name: tcp-services
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
kind: ConfigMap
apiVersion: v1
metadata:
  name: udp-services
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nginx-ingress-serviceaccount
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: nginx-ingress-clusterrole
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
rules:
  - apiGroups:
      - ""
    resources:
      - configmaps
      - endpoints
      - nodes
      - pods
      - secrets
    verbs:
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - nodes
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - services
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - "extensions"
    resources:
      - ingresses
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - events
    verbs:
      - create
      - patch
  - apiGroups:
      - "extensions"
    resources:
      - ingresses/status
    verbs:
      - update

---
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: nginx-ingress-role
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
rules:
  - apiGroups:
      - ""
    resources:
      - configmaps
      - pods
      - secrets
      - namespaces
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - configmaps
    resourceNames:
      # Defaults to "<election-id>-<ingress-class>"
      # Here: "<ingress-controller-leader>-<nginx>"
      # This has to be adapted if you change either parameter
      # when launching the nginx-ingress-controller.
      - "ingress-controller-leader-nginx"
    verbs:
      - get
      - update
  - apiGroups:
      - ""
    resources:
      - configmaps
    verbs:
      - create
  - apiGroups:
      - ""
    resources:
      - endpoints
    verbs:
      - get

---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: nginx-ingress-role-nisa-binding
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: nginx-ingress-role
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx

---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: nginx-ingress-clusterrole-nisa-binding
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: nginx-ingress-clusterrole
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx

---

apiVersion: apps/v1 
kind: DaemonSet
metadata:
  name: nginx-ingress-controller
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
spec:
  selector:
    matchLabels:
      app.kubernetes.io/name: ingress-nginx
      app.kubernetes.io/part-of: ingress-nginx
  template:
    metadata:
      labels:
        app.kubernetes.io/name: ingress-nginx
        app.kubernetes.io/part-of: ingress-nginx
      annotations:
        prometheus.io/port: "10254"
        prometheus.io/scrape: "true"
    spec:
      serviceAccountName: nginx-ingress-serviceaccount
      hostNetwork: true
      dnsPolicy: "ClusterFirstWithHostNet"
      containers:
        - name: nginx-ingress-controller
          #image: quay.io/kubernetes-ingress-controller/nginx-ingress-controller:0.21.0
          #使用以下镜像，方便国内下载加速
          image: jmgao1983/nginx-ingress-controller:0.21.0
          args:
            - /nginx-ingress-controller
            - --configmap=$(POD_NAMESPACE)/nginx-configuration
            - --tcp-services-configmap=$(POD_NAMESPACE)/tcp-services
            - --udp-services-configmap=$(POD_NAMESPACE)/udp-services
            - --publish-service=$(POD_NAMESPACE)/ingress-nginx
            - --annotations-prefix=nginx.ingress.kubernetes.io
          securityContext:
            capabilities:
              drop:
                - ALL
              add:
                - NET_BIND_SERVICE
            # www-data -> 33
            runAsUser: 33
          env:
            - name: POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
          ports:
            - name: http
              containerPort: 80
            - name: https
              containerPort: 443
            # hostPort可以直接使用node节点的网络端口暴露服务
            #- name: mysql
            #  containerPort: 3306
            #  hostPort: 3306
            #- name: dns
            #  containerPort: 53
            #  hostPort: 53
            #  protocol: UDP
          livenessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            initialDelaySeconds: 10
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 1
          readinessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 1

---
apiVersion: v1
kind: Service
metadata:
  name: ingress-nginx
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
spec:
  type: ClusterIP 
  ports:
    - name: http
      port: 80
      targetPort: 80
      protocol: TCP
    - name: https
      port: 443
      targetPort: 443
      protocol: TCP
  selector:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---

```

部署ingress

```
[yeqiang@harbor ~]$ kubectl apply -f /etc/ansible/manifests/ingress/nginx-ingress/nginx-ingress-hostport.yaml
namespace/ingress-nginx unchanged
configmap/nginx-configuration unchanged
configmap/tcp-services unchanged
configmap/udp-services unchanged
serviceaccount/nginx-ingress-serviceaccount unchanged
clusterrole.rbac.authorization.k8s.io/nginx-ingress-clusterrole unchanged
role.rbac.authorization.k8s.io/nginx-ingress-role unchanged
rolebinding.rbac.authorization.k8s.io/nginx-ingress-role-nisa-binding unchanged
clusterrolebinding.rbac.authorization.k8s.io/nginx-ingress-clusterrole-nisa-binding unchanged
daemonset.apps/nginx-ingress-controller unchanged
service/ingress-nginx created

```

#### default-http-backend

```
[yeqiang@harbor k8s]$ kubectl apply -f default-http-backend.yml 
deployment.apps/default-http-backend created
service/default-http-backend created

```



#### 故障排除

kubernetes-dashboard 故障，启动失败

```
panic: Get https://10.68.0.1:443/api/v1/namespaces/kube-system/secrets/kubernetes-dashboard-csrf: dial tcp 10.68.0.1:443: connect: no route to host
```

   排除方法：依次操作启动、停止firewalld

```
[yeqiang@localhost ~]$ sudo systemctl start firewalld
[yeqiang@localhost ~]$ sudo systemctl stop firewalld
```

​	或者

```
[yeqiang@harbor ~]$ sudo iptables -F
```

ingress没有Endpoints

```
[yeqiang@harbor k8s]$ kubectl get pods -n ingress-nginx
NAME                                       READY   STATUS    RESTARTS   AGE
nginx-ingress-controller-8b77bccc5-jt7p5   1/1     Running   2          72m

[yeqiang@harbor k8s]$ kubectl logs -n ingress-nginx  nginx-ingress-controller-8b77bccc5-jt7p5
-------------------------------------------------------------------------------
NGINX Ingress controller
  Release:    0.21.0
  Build:      git-b65b85cd9
  Repository: https://github.com/aledbf/ingress-nginx
-------------------------------------------------------------------------------

nginx version: nginx/1.15.6
....
W0821 07:32:01.973773       7 controller.go:1071] Error getting SSL certificate "default/hknaruto.com": local SSL certificate default/hknaruto.com was not found. Using default certificate

```

直接采用hostNetwork部署，后期前端应当采用keepalived+nginx作上层高可用代理，提供http、https，k8s集群ingress只需要提供http服务即可。



## harbor(minikube、kubernetes)

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

### docker信任自签名证书

```
[root@harbor system]# sudo vim /etc/docker/daemon.json 
```

```
{
  "insecure-registries": ["harbor.hknaruto.com"],
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "http://hub-mirror.c.163.com"
  ],
  "max-concurrent-downloads": 10,
  "log-driver": "json-file",
  "log-level": "warn",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
    },
  "data-root": "/var/lib/docker"
}
```

增加"insecure-registries": ["harbor.hknaruto.com"],

重启docker服务。

## MySQL(minikube)未完成

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



## 部署training(kubernetes)

```
[yeqiang@harbor training]$ sh k8sDeploy.sh 
[INFO] Scanning for projects...
[INFO] 
[INFO] Using the MultiThreadedBuilder implementation with a thread count of 4
[INFO] 
[INFO] --------------------------< com.example:demo >--------------------------
[INFO] Building demo 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ demo ---
[INFO] Deleting /home/yeqiang/code/training/target
[INFO] 
[INFO] --- maven-resources-plugin:3.1.0:resources (default-resources) @ demo ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] Copying 2 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ demo ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 16 source files to /home/yeqiang/code/training/target/classes
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[12,16] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[12,16] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[12,16] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[12,16] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[12,16] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[21,25] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[WARNING] /home/yeqiang/code/training/src/main/java/com/example/demo/util/CipherUtil.java:[21,61] sun.misc.HexDumpEncoder is internal proprietary API and may be removed in a future release
[INFO] /home/yeqiang/code/training/src/main/java/com/example/demo/util/JacksonUtil.java: /home/yeqiang/code/training/src/main/java/com/example/demo/util/JacksonUtil.java uses unchecked or unsafe operations.
[INFO] /home/yeqiang/code/training/src/main/java/com/example/demo/util/JacksonUtil.java: Recompile with -Xlint:unchecked for details.
[INFO] 
[INFO] --- maven-resources-plugin:3.1.0:testResources (default-testResources) @ demo ---
[INFO] Not copying test resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ demo ---
[INFO] Not compiling test sources
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.2:test (default-test) @ demo ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-jar-plugin:3.1.2:jar (default-jar) @ demo ---
[INFO] Building jar: /home/yeqiang/code/training/target/demo-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.2.7.RELEASE:repackage (repackage) @ demo ---
[INFO] Replacing main artifact with repackaged archive
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.814 s (Wall Clock)
[INFO] Finished at: 2020-08-25T15:37:10+08:00
[INFO] ------------------------------------------------------------------------
Sending build context to Docker daemon  37.63MB
Step 1/6 : FROM openjdk:8-jdk-alpine
 ---> a3562aa0b991
Step 2/6 : VOLUME /tmp
 ---> Using cache
 ---> 66832f61c860
Step 3/6 : COPY BOOT-INF/lib /app/lib
 ---> Using cache
 ---> 5ec17ceeadab
Step 4/6 : COPY META-INF /app/META-INF
 ---> Using cache
 ---> ef3fda3d4b71
Step 5/6 : COPY BOOT-INF/classes /app
 ---> Using cache
 ---> e0a290b9e2a1
Step 6/6 : ENTRYPOINT tini -- java -XX:MaxRAMFraction=2 -cp app:app/lib/* com.example.demo.DemoApplication
 ---> Using cache
 ---> d31217544e4e
Successfully built d31217544e4e
Successfully tagged harbor.hknaruto.com/library/training:master.a216172
Username: admin
Password: 
WARNING! Your password will be stored unencrypted in /home/yeqiang/.docker/config.json.
Configure a credential helper to remove this warning. See
https://docs.docker.com/engine/reference/commandline/login/#credentials-store

Login Succeeded
The push refers to repository [harbor.hknaruto.com/library/training]
3efd11b6f5f7: Pushed 
d9e6d176174f: Pushed 
73cad1bf8429: Pushed 
ceaf9e1ebef5: Pushed 
9b9b7f3d56a0: Pushed 
f1b5933fe4b5: Pushed 
master.a216172: digest: sha256:b40852cf2d1431e51e22a17d304786be4a8dd99312634eb8e98a428482a02fe2 size: 1576
service/training unchanged
deployment.apps/training configured
ingress.extensions/training-ingress unchanged

```

### 增加hosts配置

```
[yeqiang@harbor training]$ sudo su
[root@harbor training]# echo "10.51.72.167 training.hknaruto.com" >> /etc/hosts

```

### 访问ingress暴露的服务

```
[yeqiang@harbor training]$ curl -k https://training.hknaruto.com/ip
10.51.72.167-10.51.72.167-null
```



## OAuth2.0

https://spring.io/guides/tutorials/spring-boot-oauth2/

