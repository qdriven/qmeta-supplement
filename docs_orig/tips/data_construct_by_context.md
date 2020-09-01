# 测试数据管理-dataComposeAfter,TestDataBuilder

目前测试数据类都需要继承TestData类,TestData里面有一个方法叫dataComposeAfter,
这里说说这个方法的用法,以及如何结合TestDataBuilder一起使用

## 用途
在实际测试的过程中可能会遇到如下一写情况,比如创建案源,有些数据是二手出租时候有,有些测试数据是二手买卖才有,
有些数据是不行用过一次就不能有了,以上情况为了更好的维护数据,可以通过dataComposeAfter来解决,dataComposeAfter实际上
是开方了一个根据一些原始数据来生成其他数据的一个接口.

## 用法

场景: 添加案源
案源数据:         
* 一个私客数据,私客其实最好不是写死的,所以可以通过SQL来取得
* 如果是二手出租,只需要二手出租相关的数据就可以
* 如果是二手买卖相关的,只需要二手买卖数据就可以了

一下是这种情况下的一个实现:
*  ```InquiryDataUtils.getInquiryInfoByUserCode``` 通过SQL去获取私客信息
*  根据tradingType来区分不同场景需要的是那些数据 

```java

public CaseTestDate extends TestData{
    private String tradingType;
    private String signedType;
    private String newHouseProjectId;
    private String newHouseProjectNo;
    private String houseSourceNo;
    private String estateAddress;
    private String estateBuilding;
    private String estateRoom;
    private String buildingCertificateAddress = "测试地址产证";
    private String inquiryId;
    private String customerNo;
    private String userCode;
    private String caseId;
    private String caseNo;

    @Override
    public void dataComposeAfter() {
        this.inquiryId = InquiryDataUtils.getInquiryInfoByUserCode(this.userCode).get("inquiryId").toString();
        this.customerNo = InquiryDataUtils.getInquiryInfoByUserCode(this.userCode).get("inquiryNo").toString();
        if (!StringHelper.isNotEmptyOrNotBlankString(this.tradingType)) {
            this.tradingType = "二手买卖";
        }
        if (isSecondHand()) {
            this.set(PropertyDataUtils.getAvailableProperty());
        } else {
            this.newHouseProjectNo = PropertyDataUtils.getAvailableNewHouse().get("estateName").toString();
            this.estateBuilding = String.valueOf(RandomHelper.getFixedLengthInt(1, 5));
            this.estateRoom = String.valueOf(RandomHelper.getFixedLengthInt(1, 5));
        }
    }
  }
```

## 结合DataProvider的使用

在DataProvider中不需要额外做什么,dataComposerAfter会被执行:

```java
@DataProvider(name = "CaseTestData")
    public Iterator<Object[]> getTestData(Method m) throws Exception {
        Map<String, Class> clazzMap = new HashMap<>();
        clazzMap.put("Case", CaseTestData.class);
        clazzMap.put("TestDescription", TestDescription.class);
        Iterator<Object[]> y = TestDescription.filterByMethod("testcase/flows/BMSCaseTestCases.xls", m, clazzMap);
        return y;
    }
```

代码最后返回的y中的CaseTestData实例都已经执行过dataComposeAfter了

## 结合TestDataBuilder使用

TestDataBuilder 的build方法也会执行dataComposeAfter

```java
  OrderTestData order = (OrderTestData) TestDataBuilder.forDataType(OrderTestData.class)
                .add("caseNo", caseInfo.getCaseNo()).build();
```


