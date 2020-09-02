package io.qmeta.testng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by patrick on 15/3/13.
 *
 * @version $Id$
 */


@SuppressWarnings("unchecked")
public class JSONHelper extends JSON {

    private JSONHelper() {
    }

    public static <T> T toBean(String jsonString, Class<T> clazz) {

        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * parse Json to List
     * @param jsonString
     * @param type
     * @param <T>
     * @return
     */
    public static  <T> List<T> toList(String jsonString,TypeReference type){
        return (List<T>)JSON.parseObject(jsonString,type);
    }

    public static List<Map<String,String>> toMapList(String jsonString){
        List<Map<String,String>> result = Lists.newArrayList();
        try{
            result= JSONHelper.toList(jsonString.trim(), new TypeReference<List<Map<String, String>>>() {
            });
        }catch (Exception e){
            throw new RuntimeException("输入数据有错，请检查，需要是[{key:value},{key:value}]格式,此数据格式是:"+jsonString);
        }

        return result;
    }


    public static <T> T toBean(ResponseEntity response, Class<T> clazz) {

        return JSON.parseObject(response.getBody().toString(), clazz);
    }

/*
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

JsonPath (click link to try)	Result
    $.store.book[*].author	The authors of all books
    $..author	All authors
    $.store.*	All things, both books and bicycles
    $.store..price	The price of everything
    $..book[2]	The third book
    $..book[(@.length-1)]	The last book
    $..book[0,1]	The first two books
    $..book[:2]	All books from index 0 (inclusive) until index 2 (exclusive)
    $..book[1:2]	All books from index 1 (inclusive) until index 2 (exclusive)
    $..book[-2:]	Last two books
    $..book[2:]	Book number two from tail
    $..book[?(@.isbn)]	All books with an ISBN number
    $.store.book[?(@.price < 10)]	All books in store cheaper than 10
    $..book[?(@.price <= $['expensive'])]	All books in store that are not "expensive"
    $..book[?(@.author =~ /.*REES/i)]	All books matching regex (ignore case)
    $..*	Give me every thing
*/
    public static Object getValue(String jsonString,String jsonPathExpression){
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        //        if(o instanceof JSONArray){
//            if(((JSONArray) o).size()==1){
//                try{
//                return ((JSONArray) o).get(0);
//                }catch (Exception e){
//                    return ((JSONArray) o).get(0).toString();
//                }
////            }else{
////                List<JSONArray> result = Lists.newArrayList();
////                ((JSONArray) o).forEach(element->result.add((JSONArray) element));
////                return result;
//            }
//
//        }
        return JsonPath.read(document,jsonPathExpression);
    }

    /**
     * 获取第一个数组值到Map
     * @param jsonString
     * @param jsonPathExpression
     * @return
     */
    public static Map<String,String> getArrayValueToMap(String jsonString,String jsonPathExpression){

        try{
           return (Map<String,String>)((JSONArray)getValue(jsonString,jsonPathExpression)).get(0);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据表达式获取Json字段值
     * @param response
     * @param jsonPathExpression
     * @param <T>
     * @return 如果有路径存在在返回实际值,否则返回空字符串
     */
    public static <T> T getValue(ResponseEntity response,String jsonPathExpression){
        return JsonPath.read(response.getBody().toString(),jsonPathExpression);
    }

    /**
     *
     * @param jsonString
     * @param jsonPathExpression
     * @return 如果有路径存在在返回实际值,否则返回空字符串
     */
    public static  String getStringValue(String jsonString,String jsonPathExpression){
        try{
            return JsonPath.read(jsonString,jsonPathExpression).toString();
        }catch (Exception e){
            return StringHelper.EMPTY;
        }

    }

    /**
     *
     * @param response
     * @param jsonPathExpression
     * @return 如果有路径存在在返回实际值,否则返回空字符串
     */
    public static  String getStringValue(ResponseEntity response,String jsonPathExpression){
        try{
            return JsonPath.read(response.getBody().toString(),jsonPathExpression).toString();
        }catch (Exception e){
            return StringHelper.EMPTY;
        }
    }
}
