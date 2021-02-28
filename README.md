最简单的基于Spring Boot/Cloud使用Oauth2分布式鉴权教程.

代码我尽量给出了最简单的形式,只需要你有最基础的Spring Boot基础就能看懂.

新手请按照文件夹顺序一个一个的看,authorization_server是授权服务,resource_server是资源服务,使用test项目中的用例执行测试

每个项目中的文件都非常少,请阅读代码中的注释,相信你一定能看得懂.

-------------------------------------------

大致解释一下本项目的运行方法.

这里以simple_token为例子来讲解.

下载项目并解压之后得到文件夹Spring-Oauth2-Toturials-master

打开intellij,引入项目Spring-Oauth2-Toturials-master\1_simple_token\authorization_server

运行成功无异常后,使用intellij的File-Open打开项目Spring-Oauth2-Toturials-master\1_simple_token\resource_server

这时你应该有两个intellij窗口,分别运行一个项目.

确认两个项目都运行无异常后,再次使用intellij的File-Open打开项目Spring-Oauth2-Toturials-master\1_simple_token\test

test项目不需要运行,它不是一个服务,你可以在test项目中的src\test\java\lee\TT.java中找到单元测试.然后跟着测试类里的注释,一个一个地跑单元测试就行了.

祝学习顺利.

-------------------------------------------

视频课程已发布

1.最简单的使用SpringOAuth2进行分布式权限管理:https://www.bilibili.com/video/BV1p5411N7Po

2.在SpringOAuth2中使用JwtToken和数据库持久化:https://www.bilibili.com/video/BV1Py4y1a7Nu

3.在SpringCloud微服务架构中使用SpringOAuth2(完结):https://www.bilibili.com/video/BV1Ky4y1e7JJ
