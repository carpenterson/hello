# OAuth和SSO

## 概念

* SSO: Single Sign-On 单点登录
* CAS: Central Authentication Service 集中式认证服务
* OAuth: Open Authorization 开放授权

## SSO

SSO是多个相互关联甚至相互独立的软件系统在访问控制上的一种特性，这个特性使用户只需要登录一次就可以访问所有的软件系统。它有很多种实现手段。

### SSO历史

SSO的需求由来已久。在web应用中，有基于cookie实现的单点登录机制，这个机制相对简单，但需要各个应用有相同的顶级域名。CAS则较为复杂。OAuth面向第三方应用，相互交互的角色更多。

## CAS

[官网](https://apereo.github.io/cas/5.3.x/index.html)
[流程图](https://apereo.github.io/cas/5.3.x/protocol/CAS-Protocol.html)
[协议内容](https://apereo.github.io/cas/5.3.x/protocol/CAS-Protocol-Specification.html) 

### CAS使用场景

CAS是SSO的一种实现，使用的场景主要是同一个公司内的各个系统之间通过CAS实现单点登录。

### CAS历史
1. 由耶鲁大学研发出来的一套系统。
2. 2004年，成为Java in Administraon Special Interest Group(JASIG)的项目，由"Yale CAS"成为"Jasig CAS"。
3. 2012年， Jasig和Sakai基金会合并为Apereo基金会。
4. 2014年5月，CAS 3.0发布

## OAuth

OAuth也可以算是一种SSO实现，场景是大平台把API开放给第三方应用。

[官网](https://oauth.net/2/)
[OAuth2.0的授权码模式流程图](https://tools.ietf.org/html/rfc6749#section-4.1)
[OAUth2.0协议内容](https://tools.ietf.org/html/rfc6749)

### OAuth历史

1. 2006年11月，Twitter和Ma.gnolia的开发人员讨论，认为没有完成API访问委托的开放标准
2. 2010年4月，OAuth 1.0协议发表为RFC 5849，一个非正式RFC
3. 2012年10月，OAuth 2.0协议发布 