

## keystore
使用HttpClient的时候，对keystore的设置不理解。

1. keystore是java用来存储密钥。java要进行SSL通信，需要从keystore中获取密钥。服务端的keystore保存了公钥和私钥。客户端的keystore保存了公钥。

2. 客户端的keystore相当于是信任域的证书。在自签证书的场景下，只要保证客户端拿到的keystore没有被伪造过，就可以用来进行安全通信，而这种场景就必须明确告诉java我信任这个keystore。


## 证书
1. 场景1：S给自己颁发了证书【S的公钥为somepublic--S签发】，假如这个证书是通过可靠途径（我可以确认是S）给我了，在我本地留着。那么别人仿冒服务端，他必须用这约定的公钥跟我通信，但是他没有密钥，假冒失败。

2. 场景2：假如我没有可靠途径获取S的证书（不知道是不是S），别人仿冒S给我一个假证书【S的公钥为somefake--S签发】，我怎么相信它呢？这种情况下不能相信。

3. 场景3：那么S怎么获取我的信任呢？我有R的公钥，我相信R的话，假如R说过【S的公钥是somepublic】，我就相信。所以，S让R给【S的公钥是somepublic--R签发】盖个章，我拿S的公钥可以验证这句话确实是R说的，从而可以相信S传递给我的公钥。

4. 场景4：仿冒者因为没有让R盖章，它告诉我【S的公钥是somefake--R签发】，这句话我用R的公钥验证可以发现是假的。


## 自己给自己签发证书做认证的过程
[keytool生成keystore、truststore、证书以及SSL单向认证在服务端tomcat和客户端的配置](https://blog.csdn.net/shfqbluestone/article/details/21242323)

```bat
rem 生成一对密钥。这个keystore给服务端使用
D:\keystore>keytool -genkeypair -alias certificatekey -keyalg RSA -validity 365 -keystore mykey.jks
D:\keystore>keytool -list -v -keystore mykey.jks
rem 导出CA证书。
D:\keystore>keytool -export -alias certificatekey -keystore mykey.jks -rfc -file mycert.cer
rem 导入CA证书。这个keystore给客户端使用。
D:\keystore>keytool -import -alias certificatekey -file mycert.cer -keystore mytruststore.jks
D:\keystore>keytool -list -v -keystore mytruststore.jks
```



