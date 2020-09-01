# JSONHelper 使用
进行接口测试的时候，难免会使用到JSON，下面介绍一下使用JSONHelper来回去JSON的值

## JSONHelper介绍
JSONHelper借用了JSONPath和FASTJSON，可以方便的实现JSON返回和JAVA BEAN的转换以及获取JSON返回中的某些字段的值。

## JSONHelper例子
一下例子是用来解释使用类xpath/cssselector的方式来获取JSON的值
如果假设返回的JSON如下例：

```json
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```


使用以下是JSONPath的表示式,来获取不同的值

|JsonPath 表达式| Result |
|:----------- | :-----------: |
|$.store.book[*].author|The authors of all books|
|expensive| expensive's value|
|store| store's value|
|store.book| book in store |
$..author	| All authors
$.store.*	| All things, both books and bicycles
$.store..price	| The price of everything
$..book[2]	| The third book
$..book[2].category	| The third book's category value
$..book[(@.length-1)] | The last book
$..book[0,1]	| The first two books
$..book[:2]	| All books from index 0 (inclusive) until index 2 (exclusive)
$..book[1:2]|	All books from index 1 (inclusive) until index 2 (exclusive)
$..book[-2:] |	Last two books
$..book[2:]	| Book number two from tail
$..book[?(@.isbn)]|	All books with an ISBN number
$.store.book[?(@.price < 10)]|	All books in store cheaper than 10
$..book[?(@.price <= $['expensive'])]|All books in store that are not "expensive"
$..book[?(@.author =~ /.*REES/i)]|All books matching regex (ignore case)
$..*	| everything
    

### JSON 值 

```java
			 private String jsonString = "{\n" +
			            "    \"store\": {\n" +
			            "        \"book\": [\n" +
			            "            {\n" +
			            "                \"category\": \"reference\",\n" +
			            "                \"author\": \"Nigel Rees\",\n" +
			            "                \"title\": \"Sayings of the Century\",\n" +
			            "                \"price\": 8.95\n" +
			            "            },\n" +
			            "            {\n" +
			            "                \"category\": \"fiction\",\n" +
			            "                \"author\": \"Evelyn Waugh\",\n" +
			            "                \"title\": \"Sword of Honour\",\n" +
			            "                \"price\": 12.99\n" +
			            "            },\n" +
			            "            {\n" +
			            "                \"category\": \"fiction\",\n" +
			            "                \"author\": \"Herman Melville\",\n" +
			            "                \"title\": \"Moby Dick\",\n" +
			            "                \"isbn\": \"0-553-21311-3\",\n" +
			            "                \"price\": 8.99\n" +
			            "            },\n" +
			            "            {\n" +
			            "                \"category\": \"fiction\",\n" +
			            "                \"author\": \"J. R. R. Tolkien\",\n" +
			            "                \"title\": \"The Lord of the Rings\",\n" +
			            "                \"isbn\": \"0-395-19395-8\",\n" +
			            "                \"price\": 22.99\n" +
			            "            }\n" +
			            "        ],\n" +
			            "        \"bicycle\": {\n" +
			            "            \"color\": \"red\",\n" +
			            "            \"price\": 19.95\n" +
			            "        }\n" +
			            "    },\n" +
			            "    \"expensive\": 10\n" +
			            "}";

```

### 将Json转化为Java Bean

```java
	    @Test
	    public void testToBean() throws Exception {

	        String test = JSONHelper.toBean("{'test':'abcd'}",String.class);
	        assertNotNull(test);
	    }
```

### 根据JsonPath获取相同字段值到一个List

```java
	    @Test
	    public void testGetValue_ForList() throws Exception {
	        //getting JSONArray as List
	        List<String> value = (List<String>) JSONHelper.getValue(jsonString,".store.book[*].author");
	        System.out.println(value);
	        assertThat(value.size(), is(4));
	    }
```

### 根据JsonPath获取出一个List<Map>类型的值

```java

	    @Test
	    public void testGetValue_ForListMap() throws Exception {
	        //getting JSONArray as List
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,".store.book[*]");
	        System.out.println(value);
	        assertThat(value.size(), is(4));
	    }
	    
	    @Test
	    public void testGetValue_ForValueMap() throws Exception {
	        //getting JSONArray as List
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,".store.*");
	        System.out.println(value);
	        assertThat(value.size(), is(2));
	    }


	    @Test
	    public void testGetValue_ForValueFromRoot() throws Exception {
	        //getting JSONArray as List
	        List<Map<String,String>> value = (List<Map<String,String>>) JSONHelper.getValue(jsonString,"$.*");
	        System.out.println(value);
	        assertThat(value.size(), is(2));
	    }
	    
	    @Test
	    public void testGetValue_GetByIndex(){
	        Map<String,String> value = JSONHelper.getValueToMap(jsonString,"$..book[2]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }	    

	    @Test
	    public void testGetValue_GetListByIndex(){
	        List<Map<String,String>> value = (List<Map<String, String>>) JSONHelper.getValue(jsonString,"$..book[2]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }
	    
	    @Test
	    public void testGetValue_GetByIndexSlice(){
	        HashMap value = (HashMap) JSONHelper.getValue(jsonString,"$.store.book[1:2]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }	    
```

### 根据JsonPath获取出一个值
```java
	    @Test
	    public void testGetValue_GetByPath(){
	        String value = (String) JSONHelper.getValue(jsonString,"$.store.book[2].title");
	        System.out.println(value);
	        assertThat(value.length(),greaterThan(0));
	    }

```

### 以下是根据不同JsonPath获取不同值的方法

```java

	    @Test
	    public void testGetValue_GetBySlice(){
	        HashMap value = (HashMap) JSONHelper.getValue(jsonString,"$.store.book[-1:]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }

	    @Test
	    public void testGetValue_GetBySliceIndex(){
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,"$.store.book[-2:]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }

	    @Test
	    public void testGetValue_GetByAttribute(){
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,"$.store.book[?(@.price < 10)]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }

	    @Test
	    public void testGetValue_GetByExpress(){
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,"$..book[?(@.price <= $['expensive'])]");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }

	    @Test
	    public void testGetValue_GetAll(){
	        List<Map> value = (List<Map>) JSONHelper.getValue(jsonString,"$..*");
	        System.out.println(value);
	        assertThat(value.size(),greaterThan(0));
	    }
```


## 


## 常用处理JSON的第三方包
- fastjson
- jackson
- jsonpath
- gson

