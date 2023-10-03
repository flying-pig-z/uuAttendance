# uuAttendance

一款考勤软件的后端，差不多更新完毕，想要上线并应用。。。

uu考勤的流程图：

<img src="https://github.com/Flying-pig-z/uuAttendance/assets/117554874/7116ec22-125f-4689-8ab5-fb33f2a9fb28" width="50%" height="auto">

简单说就是在课前，学生进行请假，老师判断请假是否通过，老师指派督导进行考勤。

在课时，学生进行定位签到，督导进行考勤。

在课后，考勤如果有误，学生可以进行申诉，由老师进行审批。老师多样化查看考勤的最终结果。

## 接口文档

https://apifox.com/apidoc/shared-54488126-29d5-4edf-b6c3-112c11ab22b3

## 1.项目目录树

├─src<br>
│  ├─main<br>
│  │  ├─java<br>
│  │  │  └─com<br>
│  │  │      └─flyingpig<br>
│  │  │          ├─common<br>
│  │  │          ├─config<br>
│  │  │          ├─controller<br>
│  │  │          ├─dataobject<br>
│  │  │          │  ├─dto<br>
│  │  │          │  ├─entity<br>
│  │  │          │  └─vo<br>
│  │  │          ├─exception<br>
│  │  │          ├─filter<br>
│  │  │          ├─mapper<br>
│  │  │          ├─service<br>
│  │  │          │  └─serviceImpl<br>
│  │  │          │      └─security<br>
│  │  │          ├─util<br>
│  │  │          └─websocket<br>
│  │  └─resources<br>
│  │      ├─com<br>
│  │      │  └─flyingpig<br>
│  │      │      └─mapper<br>
│  │      └─template<br>
│  └─test<br>
│      └─java<br>

## 2.使用的技术栈

* 项目基于springboot框架进行开发
* 持久层方面使用mybatis和mybatis-plus进行开发
* 数据库方面使用关系型数据库myql存储主要数据，非关系型数据库redis对热点数据进行缓存
* 安全方面使用spring-securiy+jwt对框架对访问接口的用户进行认证，对密码进行哈希加密存储，并对用户的身份进行精细化鉴权，确保接口使用的安全性
* 使用spring-boot-starter-mail实现了邮箱验证码的发送以及账号的注册
* 集成swagger便于文档的生成和接口的调试
* 使用Apache POI将考勤数据导出为EXCEL
* 使用websocket长连接使得学生考勤签到界面的信息实时更新，解决轮询消耗大量带宽的问题

## 3.项目亮点

1.项目代码命名的编写遵循阿里巴巴Java开发规范

【1】在各层方法的命名上都采用"动词+名词+by+名词"，并且动词的命名规范如下：

![img](https://img-blog.csdnimg.cn/0888dc8a8e68415eb543f569608ada43.png)![点击并拖拽以移动](data:image/gif;base64,R0lGODlhAQABAPABAP///wAAACH5BAEKAAAALAAAAAABAAEAAAICRAEAOw==)

【2】在包的命名上包名统一小写。

2.接口的命名严格遵循restful api的规范，以资源为中心，动词用get,post,put,delete方法代替，by后面的名词用传参代替。
并且get方法传参统一采用query参数，而其他方法传参统一采用json参数。

3.在数据库的设计方面，尽量避免字段冗余，除了个别的字段重复以平衡提高查询效率。

4.在持久层方面使用mybatis与mybatis-plus结合，简单查询采用mybatis-plus提高开发效率，便于代码重构，提高封装性，而复杂的多表查询则采用mybatis来简化代码，提高代码的效率。

5.采用redis对热点数据，如邮箱验证码，登录用户的信息等进行缓存，提高热点数据的存取效率。

6.项目安全性好。使用spring-securiy+jwt对框架对访问接口的用户进行认证，对密码进行哈希加密存储，
并对用户的身份进行精细化鉴权，使用的是RBAC（基于角色的访问控制），确保接口使用的安全性，避免学生调用督导教师接口这类的异常发生。

7.使用websocket长连接，每隔几秒就向数据库查询数据有无更新，如果有更新就发送给前端。使得学生考勤签到界面的信息实时更新，解决轮询消耗大量带宽的问题。

8.在业务层进行事务处理，避免异常导致数据库的操作错误。

## 4.功能亮点

1.实现了定位签到功能。
定位签到功能的基本逻辑：

* 在后端数据库中存储着课程的准确经纬度。
* 当开始考勤，后端将经纬度发送给app，app通过高德地图SDK调用GPS进行用户的定位，实现用户位置与考勤位置之间的距离的可视化展示，如果用户的经纬度在考勤范围之内，则可以点击签到。
* 点击签到后，app将用户的经纬度发送给后台，后台再次将用户的经纬度与数据库中课程的经纬度进行比较，如果在范围之内则签到成功，否则签到失败。

2.设计对督导队员操作的审核机制，教师指定督导人员进行审批。

3.业务逻辑严密，考虑对多人考勤同一课程、同一课程有多份考勤名单和同一堂课出现两次点名等多种情况。

4.支持考勤数据的导出

