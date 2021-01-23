# 环境搭建步骤

## 下载xampp

1. 下载xampp 7.1.33版本
2. 解压到E盘根目录
3. 配置php到path环境变量

## 安装composer

1. 下载composer.pharr
2. 写一个composer.bat脚本
```bat
@php "%~dp0composer.phar" %*
```
3. 把composer.bat配置到path环境变量
4. 配置阿里镜像代理
```bat
composer config -g repo.packagist composer https://mirrors.aliyun.com/composer/
```
配置文件的位置
C:\Users\Administrator\AppData\Roaming\Composer\config.json

## 创建hello-lumen应用
只需执行如下命令即可
```bat
composer create-project laravel/lumen --prefer-dist hello-lumen 
```

## 把CTF题目拷贝到hello-lumen应用的app目录下
拷过来之后，增加一个依赖ramsey/uuid
```bat
composer require ramsey/uuid
```

## 配置apache，使之能够访问hello-lumen应用
修改appache httpd-vhosts.conf
```conf
Listen 8082
<VirtualHost *:8082>
    DocumentRoot "E:/Code/php/hello-lumen/public"
    ServerName test.lumen
    <Directory "E:/Code/php/hello-lumen/public">
        Options Indexes FollowSymLinks
        AllowOverride all
        Require local
    </Directory>
</VirtualHost>
```
参考：https://blog.csdn.net/wowkk/article/details/52097376

## 在mysql中创建表结构
1. 创建数据库ctf，执行如下sql
```sql
drop table if exists users;
create table users(
    id int not null auto_increment,
    username varchar(255),
    password varchar(255),
    uuid varchar(255),
    api_token varchar(255) default null,
    coin int default 300,
    created_at timestamp default current_timestamp(),
    updated_at timestamp default current_timestamp(),
    primary key(id)
);

drop table if exists lotteries;
create table lotteries(
    id int not null auto_increment,
    uuid varchar(255),
    coin int default 300,
    used int default 0,
    created_at timestamp default current_timestamp(),
    updated_at timestamp default current_timestamp(),
    primary key(id)
);
```
2. 在mysql中创建用户ctf，并赋予权限

## 配置hello-lumen的.env文件
配置数据库连接、key、flag
```txt
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=ctf
DB_USERNAME=ctf
DB_PASSWORD=ctf
LOTTERY_KEY=1234567890123456
FLAG=flag{GoooodJooooob}
```

## 配置hello-lumen的bootstrap/app.php
把所有的代码取消注释，启用所有特性
```php
$app->withFacades();
$app->withEloquent();

$app->middleware([
    App\Http\Middleware\ExampleMiddleware::class
]);

$app->routeMiddleware([
    'auth' => App\Http\Middleware\Authenticate::class,
]);

$app->register(App\Providers\AppServiceProvider::class);
$app->register(App\Providers\AuthServiceProvider::class);
$app->register(App\Providers\EventServiceProvider::class);
```

## 配置hello-lumen的routes/web.php
配置如下路由
```php
$router->post('user/register', 'UserController@register');
$router->post('user/login', 'UserController@login');
$router->get('user/info', 'UserController@info');
$router->post('/lottery/buy', 'LotteryController@buy');
$router->post('/lottery/charge', 'LotteryController@charge');
$router->post('/lottery/info', 'LotteryController@info');
$router->post('/flag', 'FlagController@flag');
```

## 修改app/Exceptions/Handler.php，把Throwable改成Exception
否则会报兼容错误
```php
    use Exception;

    public function render($request, Exception $exception)
```

## 因为使用的是php 7.1，要抑制deprecated报错
修改php.ini
```ini
error_reporting = E_ALL & ~E_NOTICE & ~E_DEPRECATED
```

修改LotterryController.php，在调用mcrypt_encrypt和mcrypt_decrypt之前要加一句`error_reporting(E_ALL ^ E_DEPRECATED)`
```php
error_reporting(E_ALL ^ E_DEPRECATED); // 使用php7.1.33版本时，要加这一行，否则会导致报错
$enc = base64_encode(mcrypt_encrypt(MCRYPT_RIJNDAEL_256, env('LOTTERY_KEY'), $serilized, MCRYPT_MODE_ECB));
```
