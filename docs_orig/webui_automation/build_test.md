# 构建业务流程的方法

之前已经介绍了关于框架的设计思路，已经一些基本的用法，主要包括了：

- Page 类(Page Object), 这里需要大家了解的是如何定位页面元素(id,name,css,xpath)
  但是实际上都有统一的逻辑的，就是使用一个标示符号来表示元素的定位，这里需要大家了解的是xpath
- Page 类定义过程中需要了解的是不同HTML元素的种类
- 测试数据类

以下介绍一下在Page类和测试数据类定义好了之后，构建测试业务流程的一些方式.

## 构建测试流程- 手动构建

selenium提供了很多的方法可以操作页面的元素，所以如果通过selenium自己的接口的话，直接可以使用类似于如下的代码构建：

```java
  driver.findElement(By.id("username")).sendkeys("username")
  driver.findElement(By.id("loginButton")).click()
```

目前我们通过这个方式写也是支持的，不过我们希望这些业务代码需要在flow的一个类里面，来统一管理.
同时如果我们目前已经定义了页面的类，PAGE类的话，可以通过一下代码来表示：

```java
  Page page = ModifiedPageFactory.createPageObject(driver,Page.class);
  page.getUserName().sendKeys("userName");
  page.getLoginButton().click();
```

相应的流程类：

```java
  public class LoginFlows{
    public static void login(String userName,String password,WebDriver driver){
      Page page = ModifiedPageFactory.createPageObject(driver,Page.class);
      page.getUserName().sendKeys(userName);
      page.getPassword().sendKeys(password);
      page.getLoginButton().click();
    }

    public static void login(WebDriver driver, TestData data){
      Page page = ModifiedPageFactory.createPageObject(driver,Page.class);
      page.getUserName().sendKeys(data.get("userName"));
      page.getPassword().sendKeys(data.get("password"));
      page.getLoginButton().click();
    }
  }
```

## 构建测试流程- 使用WebBaseTest类

使用WebBaseTest类暂时不推荐这样做,这个是一开始的一个遗留类，不过不影响具体的使用.可以直接使用WebTestActionBuilde来构建.

实例代码：

```java
public class LoginWebFlow extends BaseWebTestAction {

    public LoginWebFlow(WebDriver driver, TestData testData) {
        super(driver, testData);
    }

    @Override
    public void execute() {
        driver.get(EnvironmentHelper.getDomainUrl());
        Page page = ModifiedPageFactory.createPageObject(driver,Page.class);
        page.getUserName().sendKeys(data.get("userName"));
        page.getPassword().sendKeys(data.get("password"));
        page.getLoginButton().click();
    }
}

```

## 构建测试流程- 使用WebTestActionBuilder

在上面的例子中我们可以看到：

```java
Page page = ModifiedPageFactory.createPageObject(driver,Page.class);
page.getUserName().sendKeys(data.get("userName"));
page.getPassword().sendKeys(data.get("password"));
page.getLoginButton().click();
```

如果使用WebTestActionBuilder的话，以上代码就和如下例子是一致的：

```java
  WebTestActionBuilder.use(Page.class,driver)
  .process(Lists.newArrayList("userName inputIfPresent",
  "password inputIfPresent","loginButton click"),data);

  //可以简写为：
  WebTestActionBuilder.use(Page.class,driver)
  .process(Lists.newArrayList("userName",
  "password","loginButton"),data);

  //如果在Page里面定义了注解
  @UIActions(
    actions = {
            @UIAction(processName = "登陆",
                    elementActionDescription = {"userCode input"
                            ,"password input"
                            ,"companyId selectIfPresent"
                            ,"submit click"})
    }
  public class Page extends ExecutblePageObject{
    ..........
  }  
)

//WebTestActionBuilder 使用：

WebTestActionBuilder.use(Page.class,driver).process()
              "登陆", testData);
```

了解了WebTestActionBuilder的基本用法之后，我们定义一个LoginFLow可以使用如下代码：

```java
public class LoginFlows{
  public static void login(String userName,String password,WebDriver driver){
    driver.get(EnvironmentHelper.getDomainUrl);
    //WebDriverHelper.get(driver,EnvironmentHelper.getDomainUrl);
    WebTestActionBuilder.use(Page.class,driver).process()
                  "登陆", testData);
  }
}
```


## 构建测试流程- 使用WebTestActionBuilder中常用函数的用法

- execute(List<Class> pages,String uiAnnotationName,WebDriver driver, TestData data):
  根据注解名字执行多个页面的操作,前提是多个页面中具有相同的UIAction名称

```java
  WebTestActionBuilder.execute(Lists.newArrayList(Page1.class,Page2.class))
                "登陆", driver,testData);
```
- executeUIAction(T page, String uiAnnotationName
            , TestData data): 根据注解名字执行一个页面的操作,前提是多个页面中具有相同的UIAction名称

```java
  WebTestActionBuilder.executeUIAction(Page.class,
                "登陆", driver,testData);
```   
- executeUIAction(T page, List<String> elementActionPairList
            , TestData data): 根据描述执行描述中的操作        

```java
WebTestActionBuilder.executeUIAction(pageInstance,Lists.newArrayList("userName",
"password","loginButton"),driver,data);
```

- createTestActionByUIAction 的一系列函数和以上的函数类似，可以直接使用executeUIAction的一系列函数替代的
  不过他返回的是一个TestAction对象，如果要让TestAction对象里面的内容跑起来，只有后面加execute（）方法就可以了

```java
  WebTestActionBuilder.createTestActionByUIAction(ageInstance,Lists.newArrayList("userName",
  "password","loginButton"),driver,data).execute();
```

那么什么时候或许能用到这个createTestActionByUIAction呢，可能的场景是：

```java

  List<TestAction> steps = Lists.newArrayList();
  stpes.add(WebTestActionBuilde.createTestActionByUIAction(Login.class,"abcAnnotation",driver,data))
  steps.add(WebTestActionBuilde.createTestActionByUIAction(Login.class,"efGAnnotation",driver,data)))
  steps.add(WebTestActionBuilde.createTestActionByUIAction(Login.class,"HILAnnotation",driver,data)))

  for(step : steps){
    step.execute()
  }

```

## 构建测试用例的DataProvider的说明

```java
@DataProvider(name = "search_data")
public Iterator<Object[]> getTestData(Method m) throws Exception{
    Map<String, Class> clazzMap = new HashMap<String, Class>();
    clazzMap.put("SearchData", BaiduSearchData.class);
    clazzMap.put("TestDescription", TestDescription.class);
    Iterator<Object[]> y = TestDescription.filterByMethod("testcase/flows/BaiduSearchTestCases.xls", m, clazzMap);
    return y;
}
```

- SearchData 是定义一个前缀，已这个开始的excel字段值都会到BaiduSearchData类的相对应的值里面
- TestDescription 同样道理
