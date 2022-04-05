# TLS
## TLS序列号
TCP有序列号，用来保证数据包的顺序。
TLS的序列号用来防重放，它是客户端和服务器两边各自维护的序列号，并不会在消息中发送，但是会用来计算mac
https://serializethoughts.com/2016/02/09/tls-sequence-numbers

## TCP是面向连接的
TCP连接池的含义是什么，避免TCP的三次握手。TLS的会话重用。
https://serverfault.com/questions/1032970/what-is-meant-by-tcp-connection-pooli

## TLS会话重用
https://www.rfc-editor.org/rfc/rfc5246.html#section-7.4.1.1
client hello 携带sessionID
server hello 回复sessionID，服务器需要缓存才可以实现会话重用。服务器也可以不返回sesionID，表示不支持会话重用。不支持会话重用，就表示每次都必须完成完整的握手过程。

## TLS会话重用-session ticket
https://www.rfc-editor.org/rfc/rfc5077.html
服务器把session加密后给到客户端，后续客户端把session ticket发给服务器，服务器解密sesion ticket，还原出会话。
