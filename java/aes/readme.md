
# AES
Advanced Encryption Standard，高级加密标准。

## 加密过程
1. 将原文分成16字节（128比特）的4X4区块
2. 将密钥作用在4X4区块上，进行多轮次的转换操作，得到密文

## 工作模式
分组加密算法要解决两个问题：
1. 明文被切成多个块，多个块的加密过程，按什么流程处理？
2. 明文的大小不是16的倍数时，怎么处理？

* ECB：密码本模式。各个块之间用同样的密码加密。无法隐藏明文的模式。
* CBC：密码分组链接模式。引入了**初始化向量**。加解密过程无法并行操作。
* CTR：计数器模式。同样需要**初始化向量**。加解密过程可以并行操作。
* GCM：伽罗瓦计数器模式。CTR+GMAC。提供消息的加密性和完整性校验。

## 消息验证码
Message Authentication Code，用来校验消息的完整性。根据(key, 密文[+其他信息])计算得出。
密文的收发双发需要提前共享一个mac秘钥。发送者把mac和密文一起发送，接收者先根据共享的mac密钥计算密文的mac，如果算出的mac值与接收到的mac值不匹配，说明消息不正确。

## GCM模式的输入输出
加密过程:
```
Inputs:
   octet_string PT ;   // Plain Text, to be both authenticated and encrypted
   octet_string AAD;   // Additional Authenticated Data, authenticated but not encrypted
   octet_string IV;    // Initialization Vector
   octet_string BK;    // Block Cipher Key

Outputs:
   octet_string  CT;   // Cipher Text
   octet_string  AT;   // Authentication Tag
```
解密过程:
```
Inputs:
   octet_string CT ;   // Cipher text, to be both authenticated and decrypted
   octet_string AAD;   // Additional Authenticated Data, authenticated only
   octet_string AT;    // Authentication Tag
   octet_string IV;    // Initialization Vector
   octet_string BK;    // Block Cipher Key

Output:
   Failure_Indicator;  // Returned if the authentication tag is invalid
   octet_string  PT;   // Plain Text, returned if and only if the authentication tag is valid
```

* 需要发送方和接收方提前约定的：BK和AAD
* 在通信过程中需要传递的：IV


## 参考链接
* [github：AES简介](https://github.com/matt-wu/AES)
* [维基百科：GCM](https://en.wikipedia.org/wiki/Galois/Counter_Mode)
* [csdn：AES-GCM加密算法](https://blog.csdn.net/t0mato_/article/details/53160772)
* [github：AES-GCM使用的代码示例](https://gist.github.com/praseodym/f2499b3e14d872fe5b4a)
* [csdn：AES-CBC使用的代码示例（不含mac机制）](http://www.cnblogs.com/arix04/archive/2009/10/15/1511839.html)
* [rfc：GCM输入输出定义](https://www.ietf.org/rfc/rfc5647.txt)
* [Google Cloud：AAD介绍](https://cloud.google.com/kms/docs/additional-authenticated-data)

