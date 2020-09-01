# 流程代码生成

## 根据已经添加注解了的页面生成代码

```java
  WebUICodeGenerator.build().generateFlowCodesForAnnotatedPage("flowName",PageClass1);
```

如果单个页面已经定义好了某个UIAction的注解,可以直接使用以上代码生成,如果多个页面的话:

```java
  WebUICodeGenerator.build().generateFlowCodesForAnnotatedPage("flowName",PageClass1,PageClass2,PageClass3);
```

生成的代码如下,实际基本上这样的代码不需要去生成了:)

```java
   WebTestActionBuilder.execute(Lists.newArrayList(Page1.class,Page2.class),"flowName",driver,testdata);

```

## 根据Excel的操作描述生成代码:
