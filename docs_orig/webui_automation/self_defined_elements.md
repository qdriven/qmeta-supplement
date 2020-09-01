# 自定义元素

为了方便的操作元素，框架实现了一些自定义的HTML元素，来方便操作. 同时每一个自定义元素都有一个默认的操作，
默认的操作基本可以满足一般性的需求. 总体上我们可以给这些元素操作分个类：

- 基本元素

    * Link
    * Button
    * InputBox
    * CheckBox
    * Radio
    * Image
    * SelectList
    * Table

- 定制的HTML元素

    * UploadElement
    * TreeElement
    * DatePicker
    * TagElement
    * DropDownElement
    * CheckBoxContainerElement

- 特殊的操作

    * WebTestActionBuilder.CLOSE_POPUP
    * WebTestActionBuilder.CLOSE_TAB
    * WebTestActionBuilder.SWITCH_TAB
    * WebTestActionBuilder.WAIT_LOAD
    * WebTestActionBuilder.SCROLL_TO_BOTTOM

- 自定义组合操作

    * 自己编写自定义元素
    * Page类中编写元素组合操作  

## 基本元素

基本元素操作比较容易理解，这些就是不同的标准HTML元素，一般都使用默认的HTML元素操作就可以了，默认操作如下：

|元素|默认操作|
|----|------|
|CheckBox|selectByVisibleText|
|Radio|selectByVisibleText|
|selectList|selectByVisibleText|
|InputBox|inputIfPresent|
｜Others|clickIfPresent|

***方法名后面有IfPresent的都表示如果元素存在就点击，如果不存在就跳过，跳过的过程都有重试的动作，速度可能较慢***

其他方法可能也会常用，如InputBox 的inputAndSelect，针对的是输入内容然后在选择选项

```java
@UIAction(processName = "editCustomerVisitMemo",
           elementActionDescription = {
                   "watchType",
                   "managerWithSee",
                   "confirmStatus", "startAt",
                   "startHour", "startMinute", "estateName inputAndSelect", "buildingName inputAndSelect"
                   , "roomNo inputAndSelect", "newHouseName inputAndSelect",
                   "inquiryReply", "saveVisitMemo"})
```

如何使用这些元素，步骤如下：
- PageObject 定义元素
- 编写业务流程，指定元素和动作

样例代码：

```java
@UIActions(actions={@UIAction(processName="addCustomer",
        elementActionDescription={"flagPrivate","status",
                "inquirySource","addsourceOther","addSource","custGrade",
                "custName","custTel","tags","remark input","submit"}),
        @UIAction(processName="addCustomerTag",
                elementActionDescription={"addTag","customLabel","submitTag"
                        })
})

public class AddCustomerPage extends ExecutablePageObject {

    @FindBy(linkText="添加联系人")
    @ElementName(elementName = "添加联系人")
    private Button addCustomer;

    @FindBy(xpath = "//a[@ng-click='addLabel()']")
    @ElementName(elementName = "添加联系人")
    private Button submitTag;

    @FindBy(xpath="//button[@ng-click='submit()']")
    @ElementName(elementName = "提交")
    private Button submit;

    @FindBy(xpath="//input[@name='flagPrivate']")
    @ElementName(elementName="录入类型")
    private Radio flagPrivate;

    @FindBy(xpath="//input[@name='status']")
    @ElementName(elementName="状态")
    private Radio status;
    @FindBy(xpath="//input[@name='inquirySource']")
    @ElementName(elementName="inquirySource")
    private Radio inquirySource;
    ......
  }
```

UIAction中： 例如： addCustomer 这个元素是Button 类型，所以默认动作就是clickIfPresent，所以在执行过程中，程序会点击这个按钮，而不需要自己写findElement(By).click()这样的代码

执行addCustomer业务的代码如下，多个页面同时都有这个ProcessName： addCustomer的都可以直接运行
```java
WebTestActionBuilder.execute(Lists.newArrayList(MyCustomerSearchPage.class,
       AddCustomerPage.class, CustomerNeedsPage.class)
       , "addCustomer", driver, testData);
```

额外需要说明的是: CheckBox,Radio这样的元素如果是同一个类型的选项，只需要定义一个元素，如inquirySource，它可能有很多选项，但是在页面元素中只需要定义一个Radio元素，执行时具体选择什么样的元素有inquirySource的数据决定

## 定制的HTML元素

* UploadElement: 文件上传，定位到```<input type="file"> ```类型的HTML元素，默认操作为上传文件
* TreeElement： 主要为组织架构树的选择项，默认操作为select，输入的数据为： orgId1-orgID2-...-
* DatePicker: 时间选择，输入数据格式为YYYY－MM－DD
* TagElement： Tag元素的选择
* DropDownElement： 有些按钮需要先点击一下，然后选择下拉菜单的某个元素
* CheckBoxContainerElement： 需要点击一下，然后再选择CheckBox

## 特殊的操作
* WebTestActionBuilder.CLOSE_POPUP: 关闭弹出框
* WebTestActionBuilder.CLOSE_TAB： 关闭Tab
* WebTestActionBuilder.SWITCH_TAB： 切换Tab
* WebTestActionBuilder.WAIT_LOAD： 等待
* WebTestActionBuilder.SCROLL_TO_BOTTOM： 滚动到页面底部

使用的代码示例：

```java
@UIAction(processName = "editCustomerVisitMemo",
           elementActionDescription = {
                   "watchType",
                   WebTestActionBuilder.SWITCH_TAB,
                   "inquiryReply", "saveVisitMemo"})
```

## 自定义组合操作

- 自己编写自定元元素
- Page类中编写元素组合操作

### 自定义组合操作: 自己编写自定元元素

每个项目组可以根据不同的情况，自己实现自定义的HTML组件，实现方法很简单，只用继承HTMLElement类就可以了，然后自己实现方法

```java
public class NewButton extends HtmlElement {
  public NewButton(String name, WebElement element) {
      super(name, element);
  }

  public NewButton(WebElement element) {
      super(element);
  }

  public void clickById(String id) {
      List elements = DriverFactory.get().findElements(super.getBy());
      WebElement element = (WebElement)CollectionsHelper.filter(elements, (webElement) -> {
          return id.equals(webElement.getAttribute("id"));
      });
      element.click();
  }
}
```

在Page类里面定义元素成这个新建的元素类型，然后clickById就在UIAction里面用了```name clickById```

### 自定义组合操作: Page类中编写元素组合操作

可以在Page类里面自己编写元素组合的操作，比如：

```java
  public void dailyReportData(String dailyReportData){
    this.diilyReportElement.input(dailyReportData);
    this.diilyReportElement2.input(dailyReportData);
    this.diilyReportElement3.input(dailyReportData);
  }
```

然后```dailyReportData```这个名字也可以在```UIAction```中使用了，同时他输入的数据会和TestData里面的```dailyReportData```相对应.

## List 类型的元素处理

```java
@UIAction(processName = "editCustomerVisitMemo",
           elementActionDescription = {
                   "type",
                   WebTestActionBuilder.SWITCH_TAB,
                   "inquiryReply", "saveVisitMemo"})

private List<Link> type;

```

TestData:

|Header|Value|
|----|------|
|TestData.type[0]|abcds|
