# WebDriver自定义操作:

## 打开网页

``` java
 WebDriver driver = new ChromeDriver();   
 driver.get(String url)
 driver.get("http://www.w3school.com.cn/") 
```

## 查找Web元素

```java
WebElement element = driver.findElement(By.id("password"));
List<WebElement> elements = driver.findElements(By.id("password"));
```

## 输入框操作

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


## 下拉框操作

```java
select.selectByIndex(int num)

num---选项对应的索引值

select.selectByValue(String str)

str---选项对应的value属性值

select.selectByVisibleText(String str)

str---选项值

例如:

//找到下拉选择框的元素：

Select select = new Select(driver.findElement(By.id("select")));

//选择对应的选择项：

select.selectByVisibleText("mediaAgencyA");

//或

select.selectByValue("MA_ID_001");

//不选择对应的选择项：

select.deselectAll();

select.deselectByValue("MA_ID_001");

select.deselectByVisibleText("mediaAgencyA");

//或者获取选择项的值：

select.getAllSelectedOptions();

select.getFirstSelectedOption();
```



## 单选项(Radio Button)操作

```java

//找到单选框元素：

WebElement bookMode =driver.findElement(By.id("BookMode"));

//选择某个单选项：

bookMode.click();

//清空某个单选项：

bookMode.clear();

//判断某个单选项是否已经被选择：

bookMode.isSelected();

```


## 多选项(checkbox)操作

```java
多选项的操作和单选的差不多,例如：

WebElement checkbox =driver.findElement(By.id("myCheckbox"));

checkbox.click();

checkbox.clear();

checkbox.isSelected();

checkbox.isEnabled();
```


## 按钮(button)操作

```java
//找到按钮元素

WebElement saveButton = driver.findElement(By.id("save"));

//点击按钮

saveButton.click();

//判断按钮是否enable

saveButton.isEnabled ();
```


## 浏览器行为模拟

```java
driver.navigate().back()
//该方法的作用是模拟单击浏览器的后退按钮。
driver.navigate().forward()
// 该方法的作用是刷新当前页面
driver.navigate(). refresh()
```


## WebDriver 退出 

```java
// 该方法的作用是模拟用户单击浏览器窗口上或者标签的关闭按钮。
driver.close()
//退出driver,并关闭所有相关的窗口
driver.quit()
```


## 组合动作的实现

```java
Actions action = new Actions(driver);
action.doubleClick(element);
action.perform();   
```


## 设置WebDriver的延时

```java

driver.manage().timeouts().implicitlyWait(long time, TimeUnit unit);

方法作用：因为Load页面需要一段时间，如果页面还没加载完就查找元素不一定能查找到元素,所以需要设置一个超时时间
这个等待时间只是用在findElement/findElements上面

参数：

    time---等待时间

    unit ---时间单位

driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

```

关于wait,这片文章不错[implicit-waits-explicit-waits](http://www.bizalgo.com/2012/01/14/timing-races-selenium-2-implicit-waits-explicit-waits/)
事实上如果期望使用implicitlyWait解决所有的timing的问题是做不到的(期望一个方法处理有的问题没有做的到方法)
,效率也不高,而使用WebDriver的FluentAPI(Expectation)能够根据不同的场景处理.


## 提交表单

```java
element.submit()
方法作用：提交一个指定的表单，适用于没有提交按钮的表单。
```



## 浏览器多窗口处理

在进行Web测试时，还会弹出一些子窗口，并且在多个窗口之间进行切换操作。

```java

//当进行了某个操作，将会有新窗口弹出时，如果要到新开的页面上进行操作，需要切换到新窗口。

 //Switch to new window opened 

for(String winHandle : driver.getWindowHandles()){ 

System. out.println( "+++" + winHandle); 

driver.switchTo().window(winHandle); 

}  

// WindowHandles可以认为是每个窗口的标示
```

具体的使用可以参考WebDriverHelper[WebDriverHelper.md]里面的switchTab,closeTab的实现


## Frames之间的切换

```java
有时候,切换前最好先执行：

driver.switchTo().defaultContent();

切换(第一次)到某个frame：

driver.switchTo().frame("leftFrame");

从一个frame切换(第二次)到另一个frame：

driver.switchTo().defaultContent();

driver.switchTo().frame("mainFrame");

比如,在富文本框里输入文本,然后提交

//根据iframe id

driver.switchTo().frame( "ueditor_0");

//抓取元素类

driver.findElement(By. xpath("//body[@class='view']")).sendKeys( "测试前台反馈输入 test input" );

//切换到默认的frame

driver.switchTo().defaultContent();

driver.findElement(By. xpath("//input[@value='提交']")).click();

```


## 操作对话框

```java
Alert alert = driver.switchTo().alert();

alert.accept() --- 确定

alert.dismiss() --- 取消

alert.getText() --- 提示信息
```


## 上传文件

```java
WebElement adFileUpload = driver.findElement(By.id("WAP-upload"));

String filePath = "C:\\test\\uploadfile\\media_ads\\test.jpg";

adFileUpload.sendKeys(filePath);

或者

setClipboardData("E:\\autotest\\hw.jpg");

driver.findElement(By.name("goods_img")).click();

        try {

Thread.sleep(1000);

} catch (InterruptedException e) {

// TODO Auto-generated catch block

e.printStackTrace();

}

 //   

 uploadFile(robot);  
```
    

## 用来设置剪贴板上的数据

以下实际上和Selenium无关,主要调用java awt 里面的api来做系统层的操作,不过实际的使用过程中可以借鉴来解决
很多不好解决的操作

```java

public void setClipboardData(String string) {

StringSelection stringSelection = new StringSelection(string);

Toolkit.getDefaultToolkit().getSystemClipboard()

.setContents(stringSelection, null);

  }

该方法用来上传附件

 public void uploadFile(Robot robot) {

       robot.keyPress(KeyEvent.VK_CONTROL);

       robot.keyPress(KeyEvent.VK_V);

       robot.keyRelease(KeyEvent.VK_V);

       robot.keyRelease(KeyEvent.VK_CONTROL);

       robot.keyPress(KeyEvent.VK_ENTER);

       robot.keyRelease(KeyEvent.VK_ENTER);

  }

```

## 不通过WebDriver来打开某个浏览器

```java 

   java.awt.Desktop dp = java.awt.Desktop.getDesktop();
   // 判断系统桌面是否支持要执行的功能
   if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
   // 获取系统默认浏览器打开链接
   dp.browse(uri);
 }
```



