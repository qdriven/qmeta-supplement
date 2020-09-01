# Verification Annotation 使用

新引入一个verification注解， 主要用来检查一些静态属性，如页面标题，页面url，元素的某个属性等.

## 具体使用方法

- Page 属性(如标题)的检查

如下代码表示，在执行的过程中，程序会检查页面的标题是否是基础信息

```java
@Verification(whatProperty = "title", expected = "基础信息")
public class BaseInfoPage extends ExecutablePageObject {
    public BaseInfoPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(linkText = "纠错")
    @ElementName(elementName = "纠错")
    private Link correct;
  }
```

- HtmlElement属性的检查

如下代码，表示程序在执行过程中会检查这个元素的placeholder的属性，和期望值一样
```java
@Verification(whatProperty = "placeholder",expected = "对经纪人的服务不满意？觉得房源描述不准确？" +
    "对链家有一些建议？在这里写下一切您想说的话吧，我们的客服会在第一时间跟您联系，为您解决问题呦！")
  private InputBox suggest;

```

## 如何获取所有的verification检查结果

```java
new SoftAssertion()
.addTestResult(DriverFactory.getThreadLevelTestContext().getWebTestResult())
```

DriverFactory.getThreadLevelTestContext().getWebTestResult() 会把所有执行过程的校验都会读出来，然后在测试用里面一起验证.
