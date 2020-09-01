# SELENIUM管理页面元素

在WEB自动化测试中,一个最基本的功能是就是操作页面元素,输入数据.
下面就介绍一下不同管理页面元素的方式,以及我们框架是如何管理页面元素的.

## Selenium的操作

以下是一些常见的SELENIUM 操作的例子,

```java
//找到输入框元素
WebElement element = driver.findElement(By.id("password"));
//在输入框中输入内容：
element.sendKeys("123456");
//将输入框清空：
element.clear();
//获取输入框的文本内容：
element.getText();
```

那么如果复杂一点的页面或者业务流程,通过SELENIUM的操作去控制的话,操作的代码可能是这样的:

```java
//找到输入框元素
WebElement element = driver.findElement(By.id("password"));
//在输入框中输入内容：
element.sendKeys("123456");
//将输入框清空：
element.clear();
//获取输入框的文本内容：
element.getText();
WebElement element1 = driver.findElement(By.id("password"));
//在输入框中输入内容：
element1.sendKeys("123456");
//将输入框清空：
element1.clear();
//获取输入框的文本内容：
element1.getText();
```

那么如果这样的处理有什么不好的地方吗?

- 元素位置定义可能散落在不同的文件里面,一旦页面元素出现变化,不好维护
- 代码量不小
- 代码的组织不好

## Selenium Page Object模式

为了解决更好的解决代码组织的,Selenium提供了一个Page Object的方式,来管理页面,就是将页面的元素统一
放在一个页面类里面,同时也可以包涵页面多个元素的操作,这样写的方式就把页面元素在统一的地方管理起来了,
如果页面元素有变化,只要在一处修改就可以了

```java
public class GoogleSearchPage {
    // Here's the element
    private WebElement q;

    public void searchFor(String text) {
        // And here we use it. Note that it looks like we've
        // not properly instantiated it yet....
        q.sendKeys(text);
        q.submit();
    }
} 
```

同时Selenium 提供了一种Page Factory的方式来构建页面类:
通过将页面元素的定义放在FindBy的注解里面,通过PageFactory的方式来创建页面类,同时由于页面元素
是懒加载的方式生成的,也就是说在实例化页面类元素的时候,就算页面还没有出现,页面元素没有出现都不会出现
NoSuchElement的错,只有在真正使用这个元素的时候(因为是通过Proxy创建的),
会先自动去findElement再进行具体的操作指令

```java
public class GoogleSearchPage {
    // The element is now looked up using the name attribute
    @FindBy(how = How.NAME, using = "q")
    private WebElement searchBox;

    public void searchFor(String text) {
        // We continue using the element just as before
        searchBox.sendKeys(text);
        searchBox.submit();
    }

    public void searchFor(String text) {
    GoogleSearchPage page＝ PageFactory.initElements(new ChromeDriver(), GoogleSearchPage.class);
    }
}
```

以上的两种方法都是为了更好的组织页面,通过这两种方式组织之后,代码可能是这样的了:

```java
    GoogleSearchPage page＝ PageFactory.initElements(new ChromeDriver(), GoogleSearchPage.class);
    page.getSearchBox.click();
    page.getSearchBox.sendKeys("12345");
    page.searchFor("1234564")
```

这样其实已经挺好了,唯一的问题可能就是代码量还是有点多,同时如果有多个页面的时候操作页面元素的代码会更多.
从更抽象的角度来说,页面元素的每一个操作,其实都是在页面元素上输入数据(提交,按钮,点就link这种可以认为数据是为空).
同时对于实际的操作者来说,有两个隐含的惯例:

- 操作这些元素还有一个暗含的逻辑就是,输入某个数据,就表示了操作某个元素(数据和页面元素有一个对应关系).
- 同时大部分的元素他的操作都是一个固定的操作,比如Button一般情况下就是click(页面元素和操作有个对应关系)

而对于自动化测试来说,往往会进行数据驱动,就是使用不同的数据来表述不同测试用例,那么考虑到数据实际上和元素还有一个隐含的对应关系
实际上数据也可以驱动页面元素的操作.

基于以上的几个惯例,就产生了目前的框架的一个基本原理:

1. 页面元素通过元素命名相同来和数据做对应匹配
2. 定义元素的默认操作
3. 通过数据来间接操作元素(基于1,2)
4. 通过页面元素来组织流程(元素的顺序,元素对应数据的有无,元素的默认操作或者指定操作)

## 目前框架的原理介绍

以下是目前框架的一些基本概念:

1. 页面基础元素(封装了的一些HTML元素),提供基础的元素操作,同时定义默认的元素操作
2. 页面元素定义(Page Object),描述页面元素位置
3. 测试数据,接收输入数据
4. 根据数据定义的流程,执行业务流程的执行器(annotation,或者自定义)

那么如何理解:

```java

//根据数据定义的流程,执行业务流程的执行器(annotation,或者自定义)
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
//页面元素定义
public class AddFangYuanSecondStepPage extends ExecutablePageObject {
    public AddFangYuanSecondStepPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(name="property_propertySource")
    @ElementName(elementName="property.propertySource")
    private Radio propertySource;  //Radio 页面基础元素
    @FindBy(name="property_status")
    @ElementName(elementName="property.status")
    private Radio status;
    @FindBy(name="property_shape")
    @ElementName(elementName="property.shape")
```

测试数据: 这里的测试数据变量的命名和页面元素的变量命名如果是一样的,那么元素和数据的隐含的对应关系就建立了

```java
public class TestData{
    private String propertySource;  //此处propertySource 如果AddFangYuanSecondStepPage 在相同的业务流程中
                                    //和页面页面基础元素propertySource 就会自动建立
    private String status;
    // ignore getter/setter
}
```
业务流程:

```java
public static void addProperty(WebDriver driver, TestData testData){
        ModifiedPageFactory.createPageObject(driver, AddFangYuanSecondStepPage.class)
        .processUIAction("新增房源", testData);
    }
//or 
      WebTestActionBuilder.usePage(AddFangYuanSecondStepPage.class,driver).process("新增房源",testData);
      WebTestActionBuilder.execute(Lists.newArrayList(FangYuanPropertyPage.class),"新增房源",testData,driver);
      
      //entrustDetail,edit 是页面的元素名称,entrustDetail有和数据对应上了
      WebTestActionBuilder.createTestActionByElementActionList(FangYuanPropertyPage.class,
      Lists.newArrayList("entrustDetail","edit"), driver, testData).execute();
```

当然通过原来Page Object里面的方式去实现也是可以的,只不过通过这样的方式来写,就是节约不少代码量. 
类似于findElement,或者其它的操作就看不太到了.


以上是目前框架实现的一个基本原理,里面如何通过名字对应实际上都是通过JAVA的反射实现的.同时这样的做的好处其实还有:

- 可以方便的进行统一处理,由于页面元素和页面大部分都是通过WebTestActionBuilder内部创建的,那么在创建页面后,或者创建元素前如果有需要进行特别处理
  就是可以统一在一处进行特别处理了,目前没有这样的需求,但是如果有需要,改动也不会太大的了,举个例子来说,如果你想检查某些元素的显示文本是什么,其实我们只要
  加注解的方式,比如我们可以重新定义一个注解 @CheckText(expectedText="abcds"), 那么我们可以在WebTestActionBuilder加一个处理这个注解方法,
  在元素创建之后,检查他的文本是不是abcds,类似的这些不变的内容,可以通过加个注解来解决,可以不需要在不同的代码添加一些Assertion
- 可以在新页面打开时进行特别的处理,比如打开一个新的页面请求就录制页面的请求,那么也可以比较统一处理,原理和上面的一样

