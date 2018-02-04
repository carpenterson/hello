搭建npm私服，发布react组件

# 安装npm私服(windows)

##### 安装sinopia
1. 已安装python2.7
2. 忽略可选依赖项的报错信息：crypt3 fs-ext
```
npm install sinopia -g

```

##### 配置sinopia
先启动一次，可以看到控制台打印出的配置文件路径
```
sinopia
```

修改配置
```
storage: E:/npm-restore
uplinks:
  npmjs:
    url: https://registry.npm.taobao.org/
listen: 0.0.0.0:4873
```

##### 用pm2守护sinopi
安装pm2，启动sinopia，查看pm2守护的所有进程列表，查看sinopia进程的详细信息
```
npm install -g pm2
pm2 start C:\Users\Administrator\AppData\Roaming\npm\node_modules\sinopia\bin\sinopia
pm2 list
pm2 show sinopia
```

##### 切换npm注册中心
安装nrm
```
npm install -g nrm
nrm add xzh http://localhost:4873/
nrm use xzh
nrm ls
nrm current
```

##### 创建用户
```
npm adduser
Username: xzh
Password: xzh
Email: (this IS public) xx@xx.com
```

# 发布
```
npm run build
npm publish
```
修改版本号之后再发布
```
npm run build
npm version patch
npm publish
```
引用组件
```
npm install --save select-list
```
更新组件。会自动把package.json中的版本号升级为最新
```
npm install --save select-list
```