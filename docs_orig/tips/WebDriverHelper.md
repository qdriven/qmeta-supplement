# WebDriverHelper

WebDriverHelper是用来操作WebElment的一个工具类，操作WebElement都是用了Expectations来处理，
Expectations的作用是如果元素不可以操作的时候,都会自动等待一段时间(```TIMEOUT```)直到元素可以操作为止,以下是Expectations的实例代码：

```java

  public static WebDriverWait getDefaultWait(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUTINSECONDS, POLLING_INTERVAL);
        wait.ignoring(WebDriverException.class);
        return wait;
    }

    public static boolean click(WebDriver driver,WebElement element){
        try{
            getDefaultWait(driver).until(elementToBeClickable(element)).click();
            return true;
        }catch (WebDriverException e){
            return false;
        }
    }
```

通过使用wait对象来进行等待，知道元素可以操作才进行操作，如果元素太长时间不出现，就是使用超时来抛出异常.

## WebDriverHelper 常用的一些函数

|函数 | 作用|
|-----|-----|
|click| 点击事件|
|input|输入时间|
|handleAlert|处理弹出框，可以确认，也可以放弃，由参数handler控制|
|simulateMouseOver|模拟mouseOver事件|
|swithTab|切换tab到当且tab|
|closeTab|关闭当前tab，同时切换回之前操作的tab|
|closeTabByTitle|根据tab的title关闭tab|
|hover| 模拟hover事件|
|scrollToBottom| 滚动到底部|
|scrollToTop|滚动到顶部|

有更详细的操作可以参考源码,或者查看方法名
