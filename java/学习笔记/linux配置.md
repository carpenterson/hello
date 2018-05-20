
# 用户管理

#### 修改用户密码
```
passwd xzh
```

#### 为用户增加sudo权限
```
chmod u+w /etc/sudoers
vim /etc/sudoers 
chmod u-w /etc/sudoers 
```

#### 维护用添加data文件夹读写权限
```
# 改变文件夹所属群组
sudo chgrp -R xzh /data
# 为群组赋予读写权限
sudo chmod -R g+rw /data
```
* [在 Linux 上给用户赋予指定目录的读写权限](https://blog.csdn.net/skykingf/article/details/71418678)

