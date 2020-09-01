# 自动化测试代码基本规范

- 命名

  JAVA 主要是采用驼峰命名的方式，每个单词的开始字母大写. 所有的命名尽量用英文名.

    * 类命名： 首字母大写，其他按照驼峰命名的方式

      HomePage -> OK
      homePage -> 不推荐
    * 方法名： 首字母小写，其他按照驼峰命名的方式

      addProperties() -> OK
      AddProperties() -> 不推荐
    * 成员变量： 首字母小写，其他按照驼峰命名的方式

      addedProperties -> OK
      AddedProperties -> 不推荐
    * 静态的final变量： 可以全部大写

- 不同类的组织

  main：

    - data: 数据类
    - flows: 业务流程类
    - pages: 页面类
    - api: api 访问客户端代码
    - resources: 资源文件，如需要上传的文件

  test：
  
    - flows: 业务流程测试类
    - api: api测试类
    - resources: 测试用例，测试数据，需要上传的文件，可以用子目录的方式来归类不同的文件

- 代码分支: Trunk/Master
  
  保证SVN的Trunk和Master分支上的代码没有编译错误
 