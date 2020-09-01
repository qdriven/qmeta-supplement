# 数据库验证

代码:

```java

  SoftAssertion.build().assertDBValues(AppName.ZICHAN.getName()
     , String.format("select * from T_ASSETS_MANAGERS where assetCode = '%s'", data.getAssetCode()),
      ExpectedResultSpec.build()
      .addExpectation("idle", "1")
      .addExpectation("occupier", "")
      .end()).getFinalResult();
```

说明:
assertDBValues,需要传入

- 数据库名称: 如AppName.ZICHAN.getName()
- SQL: 如:String.format("select * from T_ASSETS_MANAGERS where assetCode = '%s'", data.getAssetCode())
- 期望值: 
 如: 
```java 
 ExpectedResultSpec.build()
             .addExpectation("idle", "1")
             .addExpectation("occupier", "")
             .end())
```
以上的代码实际说的是期望结果里面,idle字段的值是1,occupier字段是空
