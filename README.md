# UU考勤 - uuAttendance

> 一款考勤软件的后端，更新完毕，正式上线。。。

## uu考勤的流程图：

<img src="https://github.com/Flying-pig-z/uuAttendance/assets/117554874/7116ec22-125f-4689-8ab5-fb33f2a9fb28" width="100%" height="auto">

简单说就是在课前，学生进行请假，老师判断请假是否通过，老师指派督导进行考勤。

在课时，学生进行定位签到，督导进行考勤。

在课后，考勤如果有误，学生可以进行申诉，由老师进行审批。老师多样化查看考勤的最终结果。

## 客户端仓库地址以及演示视频
* 教师端仓库地址：https://github.com/ROBINRUGAN/uu-attendance

* 学生端仓库地址：https://github.com/klxiaoniu/UUAttendance

* 演示视频：https://www.bilibili.com/video/BV1wu411T7Lt/?spm_id_from=333.999.0.0&vd_source=d171ed648157798a725c9e901af91f9e

* 数据库文件：https://github.com/flying-pig-z/uuAttendance/blob/master/deployment-file/table.sql

  > 这是数据库文件包含了建表SQL和部分数据。
  >
  > 其实本来造了不少假数据，但是后来丢失了。不过这里面基本的数据还是有的，里面的用户密码经过了加密，大部分都是102201604。当然可以自己注册一下。

## 接口文档

https://apifox.com/apidoc/shared-54488126-29d5-4edf-b6c3-112c11ab22b3

## 1.项目目录树
```bash
├─main
│  ├─java
│  │  └─com
│  │      └─flyingpig
│  │          ├─common
│  │          ├─config
│  │          ├─controller
│  │          ├─dataobject
│  │          │  ├─constant
│  │          │  ├─dto
│  │          │  ├─entity
│  │          │  ├─message
│  │          │  └─vo
│  │          ├─exception
│  │          │  └─handle
│  │          ├─filter
│  │          ├─framework
│  │          │  ├─cache
│  │          │  │  ├─core
│  │          │  │  └─model
│  │          │  ├─log
│  │          │  │  ├─annotation
│  │          │  │  └─core
│  │          │  └─ratelimiter
│  │          │      ├─annotation
│  │          │      ├─core
│  │          │      │  └─strategy
│  │          │      ├─model
│  │          │      └─util
│  │          ├─mapper
│  │          ├─mq
│  │          │  └─rule
│  │          ├─service
│  │          │  ├─excel
│  │          │  └─serviceImpl
│  │          │      └─security
│  │          ├─util
│  │          └─websocket
│  └─resources
│      ├─com
│      │  └─flyingpig
│      │      └─mapper
│      ├─scripts
│      └─template
└─test
    └─java
```
## 2.使用的技术栈

* 项目基于springboot框架进行开发
* 持久层方面使用mybatis和mybatis-plus进行开发
* 数据库方面使用关系型数据库myql存储主要数据，非关系型数据库redis对热点数据进行缓存
* 安全方面使用spring-securiy+jwt对框架对访问接口的用户进行认证，对密码进行哈希加密存储，并对用户的身份进行精细化鉴权，确保接口使用的安全性
* 使用spring-boot-starter-mail实现了邮箱验证码的发送以及账号的注册
* 集成swagger便于文档的生成和接口的调试
* 使用EasyExcel将学生名单导入到数据库中
* 使用websocket长连接使得学生考勤签到界面的信息实时更新，解决轮询消耗大量带宽的问题
* 使用阿里云OSS存储请假图片以及考勤申诉的图片
* 使用docker+nginx进行部署

## 3.项目亮点

1.规范性方面

【1】项目代码命名的编写遵循阿里巴巴Java开发规范

* 在各层方法的命名上都采用"动词+名词+by+名词"，并且动词的命名规范如下：

![img](https://img-blog.csdnimg.cn/0888dc8a8e68415eb543f569608ada43.png)

* 在包的命名上包名统一小写。

【2】接口的命名严格遵循restful api的规范，以资源为中心，动词用get,post,put,delete方法代替，by后面的名词用传参代替。
并且get方法传参统一采用query参数，而其他方法传参统一采用json参数。

【3】规范请求的状态码

2.在数据库的设计方面，尽量避免字段冗余，除了个别的字段重复以平衡提高查询效率。

持久层代码方面使用mybatis与mybatis-plus结合，简单查询采用mybatis-plus提高开发效率，便于代码重构，提高封装性，而复杂的多表查询则采用mybatis来简化代码，提高代码的效率。

3.性能方面

* 采用redis对热点数据，运用泛型和自定义接口将redis相关操作封装成工具类，对邮箱验证码，登录用户的信息，请假信息等进行缓存，提高热点数据的存取效率。

* 使用RabbitMQ将签到入库操作放到消息队列中，进行异步削封，并提高响应速度

* 使用多线程导入学生Excel名单并批量导入数据库中，技术栈使用的是EasyExcel

4.安全性方面

使用spring-securiy+jwt对框架对访问接口的用户进行认证，对密码进行哈希加密存储，
并对用户的身份进行精细化鉴权，使用的是RBAC（基于角色的访问控制），确保接口使用的安全性，避免学生调用督导教师接口这类的异常发生。

5.签到页面

课程信息使用websocket长连接动态更新应点名的课程和点名的状态，实现状态的同步。每隔几秒就向缓存查询应点名数据和点名的状态有无更新，如果有更新就将修改的数据推送给前端。提高响应速度，优化体验，节省带宽。

签到的时候先修改缓存中签到的状态，然后再异步到数据库中。

6.在业务层进行事务处理，避免异常导致数据库的操作错误。

## 4.功能亮点

1.实现了定位签到功能。
定位签到功能的基本逻辑：

* 在后端数据库中存储着课程的准确经纬度。
* 当开始考勤，后端将经纬度发送给app，app通过高德地图SDK调用GPS进行用户的定位，实现用户位置与考勤位置之间的距离的可视化展示，如果用户的经纬度在考勤范围之内，则可以点击签到。
* 点击签到后，app将用户的经纬度发送给后台，后台再次将用户的经纬度与数据库中课程的经纬度进行比较，如果在范围之内则签到成功，否则签到失败。

2.设计对督导队员操作的审核机制，教师指定督导人员进行审批。

3.业务逻辑严密，考虑对多人考勤同一课程、同一课程有多份考勤名单和同一堂课出现两次点名等多种情况。

4.支持学生名单的导入和考勤数据的导出，导出时候进行分页查询防止OOM(其实数据量并不大也用不着分页查询)

## 5.优化思考

1.客户端传回来的定位会存在飘动的现象。

2.因为客户端的一切计算都是不安全的，所以虚拟定位问题难以得到解决。

3.点名的时候可以将课堂学生列表存储在缓存中，然后进行抽取，并将抽取到的学生进行删除。



