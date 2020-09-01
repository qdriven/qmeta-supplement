# DateHelper使用
DateHelper里面包含了一些常用的处理时间的公用方法，下面介绍一下常用的方法。

## Format Date

- formatDate, 通过传入intervalDays生成和参考时间间隔天数的日期

以下代码生成比当前日期晚30天的日子
```java
 DateHelper.formatDate(new Date(),30)

 输出： 2015-07-23
```

- convertFromLong，long值转换为Date型
- isSameDate，是否是同一天


## 常用处理日期的第三方包：
- Joda
- Apache common-langs3
- JDK Calendar

