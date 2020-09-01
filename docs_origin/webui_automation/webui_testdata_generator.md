# 测试数据代码生成

当页面类生成之后,可以通过页面类来生成你的测试数据类,假设你的页面类定义如下:

```java

public class AssetApplicationPage extends ExecutablePageObject {
    public AssetApplicationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath="//*[@id='htTree']/li/a[@id]")
    @ElementName(elementName = "所属组织")
    private List<HtmlElement> orgLists;

    @FindBy(xpath="//*[@value='选择分类']")
    @ElementName(elementName = "选择分类")
    private Button category;

    @FindBy(xpath="(//div[@id='categorySelect'])[1]/a")
    @ElementName(elementName = "资产种类")
    private List<HtmlElement> categoryLists;

    @FindBy(xpath="(//div[@id='categorySelect'])[2]/a")
    @ElementName(elementName = "资产详细种类")
    private List<HtmlElement> assetDetailCategory;

    @FindBy(id="makeSureType")
    @ElementName(elementName = "确定")
    private Button setAssetCategory;

    @FindBy(id="cancelType")
    @ElementName(elementName = "取消")
    private Button cancelAssetCategory;

    @FindBy(name="details[0].quantity")
    @ElementName(elementName = "数量")
    private InputBox quantity;

    @FindBy(name="details[0].comment")
    @ElementName(elementName = "备注")
    private InputBox comment;
    .........
}    
```

使用如下代码就可以生成测试数据的代码类的成员:

```java
 WebUICodeGenerator.build().generateTestDataClass(AssetApplicationPage.class);
```

