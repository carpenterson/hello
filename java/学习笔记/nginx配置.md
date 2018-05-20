
## nginx重新加载配置
```
nginx -s stop
nginx -s reload
service nginx restart
```

## nginx路由配置
```conf
        location / {
            root  /data/release/client;
			try_files $uri $uri/ /index.html;
            index  index.html;
        }

        location /music {
            root /data/file/;
        }
		location ^~ /api/ {
            #proxy_pass  http://127.0.0.1:8080;
            proxy_pass http://127.0.0.1:8080/api/;
		}
```

参考资料
* [前后端分离实践（一）](https://segmentfault.com/a/1190000009329474)
* [nginx服务器安装及配置文件详解](https://segmentfault.com/a/1190000002797601)
* [nginx配置location总结及rewrite规则写法](https://segmentfault.com/a/1190000002797606)
* [react.js react-router关于服务器部署问题](https://segmentfault.com/q/1010000012285245)
* [nginx 之 proxy_pass详解](https://blog.csdn.net/zhongzh86/article/details/70173174)


