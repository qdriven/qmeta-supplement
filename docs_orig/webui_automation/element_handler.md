# 控件处理

以下是目前框架中控件支持的一些操作和说明:

|控件|默认操作|可用操作|说明|
|---- |------|---- |-----|
|HtmlElement|click|toString,clear,getName,getLocation,setName,getSize,getLocationName,isEnabled,getAttribute,getTagName,clickIfPresent,moveTo,click,sendKeys,sendKeys,clearAndInput,isSelected,getText,findElements,findElement,isDisplayed,getCssValue,getWrappedElement,setWrappedElement,setLocationName,getBy,setBy,triggerClick,getLocatorExpression,setLocatorExpression,highlight,input,submit,|说明|
|SelectList|selectByVisibleText|selectByIndex,getSelect,getOptions,getAllSelectedOptions,getFirstSelectedOption,hasSelectedOption,selectFirst,selectByGivenAttributeValue,deselectAll,deselectByValue,deselectByIndex,deselectByVisibleText,selectByVisibleText,isMultiple,|说明|
|Table|click|getHeadings,getFirstRowAsHeading,getHeadingsAsString,getRows,getValueByTdTitle,getRowsAsString,getColumns,getColumnsAsString,getCellAt,getRowsMappedToHeadings,getRowsMappedToHeadings,getRowsAsStringMappedToHeadings,getRowsAsStringMappedToHeadings|说明|
|NgRepeatableElement|clearAndInput|inputOne,getRepeatTriggerXpathLocator,setRepeatTriggerXpathLocator,getInitCount,setInitCount,getOffsetXPathForRepeatableRoot,setOffsetXPathForRepeatableRoot,clearAndInput,|说明|
|InputBox|clearAndInput|inputAllSameBox,inputIfPresent,inputAndSelect,inputIfVisible,|说明|
|Radio|selectByVisibleText|hasSelectedButton,selectByIndex,selectIfPresent,getSelectedButton,getButtons,selectByValue,selectByVisibleText,selectButton,|说明|
|Image|click|getAlt,getSource,|说明|
|TreeElement|select|select,|说明|
|EditableRow|click||说明|
|DropDownElement|select|selectForPopSelect,selectByLink,selectByLinkId,select,|说明|
|Button|click||说明|
|CheckBox|selectByVisibleText|set,getText,getLabelText,deselect,getLabel,getSelectedButton,getButtons,selectByValue,selectByVisibleText,multipleSelectByVisibleText,selectButton,select,|说明|
|ListPictureElement|select|selectAll,deSelectAll,getAllList,deSelect,select,|说明|
|UploadElement|upload|fakeUpload,upload,|说明|
|DatePicker|selectDate|selectDate,|说明|
|PictureItem|selectPicType|delete,getAllPictureItem,getLastPictureItem,addDesc,getFirstPictureItem,deleteLast,deleteFirst,deleteByIndex,addDescForLast,addDescForFirst,selectPicTypeForLast,selectPicType,selectPicTypeForFirst,|说明|
|Link|click|getLink,|说明|

说明: 以上所有的元素都具有HtmlElement的方法
