# 写步骤的几种写法

关于如何写页面类的，比较固定但是写操作步骤的比较灵活.以下是常见的几种写法：

- 直接调用页面不同的页面元素
- 直接编写代码
- 直接创建页面元素并执行
- Annotation

## 直接调用不同的页面元素

以下是房源修改房源属性的例子：

```java
   public static void 修改装修(WebDriver driver, TestData testData) {
        handleFY(driver, testData);
        WebTestActionBuilder.createTestActionByElementActionList(PropertyPage.class, Lists.newArrayList("propertyDecoration", "edit"),
                driver, testData).execute();
        WebTestActionBuilder.createTestActionByElementActionList(ChangePropertyDecorationPage.class, Lists.newArrayList("propertyDecoration",
                "devices", "submit"), driver, testData).execute();
    }
```

直接调用页面元素名字就可以，如果多个页面就分开写就可以，这个代码片段就把修改装修的业务操作完成，如果在页面跳转的过程中，需要切换tab页面或者其他的，可以在页面和页面之间增加，如下例：

```java
  public static void 独家出租委托(WebDriver driver,TestData testData){
        buildIgnoredFields(testData);
        PropertyFlows.handleFY(driver,testData);
        WebTestActionBuilder.createTestActionByElementActionList(FangYuanPropertyPage.class,
                Lists.newArrayList("entrustDetail","edit"), driver, testData).execute();
        WebTestActionBuilder.createTestActionByElementActionList(PropertyApplyPage.class,
                Lists.newArrayList("exclusiveRentApply")
                ,driver,testData).execute();
        WebDriverHelper.swithTab(driver);
        WebTestActionBuilder.createTestActionByElementActionList(EntrustApplyPage.class,
                Lists.newArrayList("empNo selectFirst"
                        ,"entrustEndDate","entrustEndType","exclusiveRentDocList"
                        ,"signedPersonRentCredentialsDocList"
                        ,"propertyRightsRentCredentialsDocList"
                        ,"otherRentCredentialsDocList"
                        ,"submit"),driver, testData).execute();
    }
```

## 直接编写代码
这个写法和原来的写法比较类似，先找到元素，然后进行操作：

```java
  PropertyDescPage page = ModifiedPageFactory.createPageObject(driver, PropertyDescPage.class);
        page.getBasicInfo().click();
        page.getNewPropertyDesc().click();
        .......
```

## 直接创建元素并进行操作

```java
 public static void logout(WebDriver driver){
        driver.get(EnvironmentHelper.getHomeUrl());
        WebDriverHelper.click(driver, By.xpath("//div[@class='navTop']//a[text()='退出']"));
    }
```

## 适用注解进行操作

1. 创建页面注解
```java
@UIActions(actions={@UIAction(processName="新增房源",elementActionDescription={"contactName",
        "relationship","residence","contactPhone"
        ,"contactTel","propertySource"
        ,"status","price","handPrice","rentPrice"
        ,"shape","countF","countT","countW","countGarden","countCellar","square","floor","floorAll"
        ,"currentStatus","rentExpireDate","rentAvailableDate"
        ,"currentStatusRemark","propertyLook","whenToLookHouse","keyRemark","isAuction"
        ,"countGarage","countParks","officeGrade","realRate","floorHeight","airConditionCosts",
        "depth","floorHeight","transferFee","managementFee","electricPower"
        ,"propertyDirection","waterToward","buyinDate"
        ,"propertyDecoration","flagLoan","remark","submit"})
})
public class AddFangYuanSecondStepPage extends ExecutablePageObject {
    public AddFangYuanSecondStepPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(name="property_propertySource")
    @ElementName(elementName="property.propertySource")
    private Radio propertySource;
    @FindBy(name="property_status")
    @ElementName(elementName="property.status")
    private Radio status;
    @FindBy(name="property_shape")
    @ElementName(elementName="property.shape")
```

2. 写流程类，或者可以直接写到方法中

```java
public static void addPropertyDesc(WebDriver driver, TestData testData){
        PropertyFlows.handleFY(driver,testData);
        ModifiedPageFactory.createPageObject(driver, PropertyDescPage.class).processUIAction("添加房源描述", testData);
    }
```



