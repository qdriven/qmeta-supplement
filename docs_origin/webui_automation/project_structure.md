# 项目概要

自动化测试框架在使用了JAVA8的一些功能，所以需要使用JAVA8的运行环境；同时用MAVEN来管理依赖的，所以在各个应用的中的如果需要引用自动化测试框架，只要将自动化测试的jar引入就可以


## 项目环境

* JAVA8 去Oracle网站下载最新的JDK安装即可
* MAVEN3的配置请参考:[MAVEN Windows配置](http://maven.oschina.net/help.html)
* JAVA开发工具,推荐使用[intellij idea](https://www.jetbrains.com/idea/download/)的社区版(community)

## 各个业务项目的主要文件结构
对于各个业务项目来说，建议整个名字项目的maven坐标为：

```xml
 <groupId>com.Domain.automation</groupId>
    <artifactId>automation-项目名称</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.Domain.automation</groupId>
            <artifactId>automation-common</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

添加MAVEN 文件的更新机制 以下设置了项目从哪里去依赖的JAR包，以及更新机制:

```xml
<repositories>
        <repository>
            <id>snapshots</id>
            <url>http://nexus.Domain.org/nexus/content/repositories/snapshots/</url>
            <snapshots>
                <updatePolicy>always</updatePolicy>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>releases</id>
            <url>http://nexus.Domain.org/nexus/content/repositories/releases/</url>
       </repository>
 </repositories>
```

对于每个业务系统的测试项目来说，建议都采用以下的目录结构：

- apis
- data
- flows
- pages

以上是四个主要的目录，在每一个目录相面可以按照功能块再划分,
关于一个项目的目录结构可以参考svn上面,Domain-automation下面automation-demo项目.
