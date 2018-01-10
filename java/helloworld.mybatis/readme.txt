
-- MyBatis中文文档 
http://mybatis.github.io/mybatis-3/zh/getting-started.html

-- 如何准备这个例子中用到的数据源？
-- 安装sqliteBrowser，创建一个db，执行下面的sql语句

create table book(id number, name varchar2(255), author varchar2(255));

insert into book values(1, '西游记', '吴承恩');
insert into book values(2, '红楼梦', '曹雪芹');
insert into book values(3, '三国演义', '施耐庵');
insert into book values(4, '水浒传', '罗贯中');

select * from book;

---
helloworld中发生了什么？