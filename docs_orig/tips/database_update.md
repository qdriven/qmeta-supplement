# 数据修改和数据回滚

代码:

```java

   @Test(dataProvider = "Asset_data",description = "设置资产闲置")
     public void testSetAssetToIdle(EmployeeTestData user,AssetTestData data,
                                    TestDescription td){
 
         String sql = String.format("update dbo.T_ASSETS_MANAGERS set idle=0,occupier='%s'" +
                 "where  assetCode='%s' and status='正常'", user.getUserCode(), data.getAssetCode());
 
         TestContextHolder.addDataFixture(DataFixture.build(AppName.ZICHAN.getName()
                 , sql, "id"));
 
         WebDriver driver= loginAndReturnDriver(user);
         AssetsFlows.setAssetToIdle(driver, data);
 
         SoftAssertion.build().assertDBValues(AppName.ZICHAN.getName()
                 , String.format("select * from T_ASSETS_MANAGERS where assetCode = '%s'", data.getAssetCode()),
                 ExpectedResultSpec.build()
                         .addExpectation("idle", "1")
                         .addExpectation("occupier", "")
                         .end()).getFinalResult();
 
     }
```

说明: 在某些场景下面,需要在测试开始的时候更新数据库的某个值已确认数据可以被使用,然后当测试完成后更新回初始状态,比如可能修改权限来保证测试可以继续,
测试结束就恢复到原来状态以确保不影响其他人的测试

通过一下代码申明需要更新什么的数据,还有更新表的主键是什么就可以完成一开始更新,测试结束后回滚的操作:
*ps: 目前只支持单表,以及没有内嵌SQL(nested SQL)的处理*
```java
 String sql = String.format("update dbo.T_ASSETS_MANAGERS set idle=0,occupier='%s'" +
                 "where  assetCode='%s' and status='正常'", user.getUserCode(), data.getAssetCode());
 
 TestContextHolder.addDataFixture(DataFixture.build(AppName.ZICHAN.getName()
                 , sql, "id"));
```

在测试完成后,修改的数据会自动回滚到修改之前的数据,理论上如果测试出现异常,回滚也会执行
