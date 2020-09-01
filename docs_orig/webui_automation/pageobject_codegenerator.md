# 页面元素代码类生成--优化后

以如下页面为例,来生成一下基础的页面:
![img](../pics/add_1.jpg)

保存页面的HTML源码到source.txt文件,可以页面片段也可以是整个页面,注意最好在审查页面元素里面复制源码:

```java
  WebUICodeGenerator.build().generatePageObjectClass("source.txt");
```
运行后可以得到如下结果：

```java
@ElementName(elementName="property.propertyUsage")
private Radio propertyUsage;
@FindBy(name="property.estateName")
@ElementName(elementName = "property.estateName")
private InputBox estateName;
@FindBy(name="property.address")
@ElementName(elementName = "property.address")
private InputBox address;
@FindBy(name="property.roomNo")
@ElementName(elementName = "property.roomNo")
private InputBox roomNo;
@FindBy(name="property.permitNo")
@ElementName(elementName = "property.permitNo")
private InputBox permitNo;
```
