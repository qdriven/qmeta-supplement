package io.qmeta.testng.util;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.Map;

/**
 * Created by patrick on 15/3/9.
 *
 * @version $Id$
 */


public class XMLDocHelper {
    private XMLDocHelper(){}
    private Document doc;
    private Element root;

    private static final Logger logger = LogManager.getLogger(XMLDocHelper.class.getName());

    public static XMLDocHelper build(String path){
        XMLDocHelper helper = new XMLDocHelper();
        SAXReader reader = new SAXReader();
        try {
            helper.doc=reader.read(ClassLoader.getSystemResourceAsStream(path));
            helper.root=helper.getDoc().getRootElement();
        } catch (DocumentException e) {
            logger.warn("load page object resource file failed, error=",e);
            //ClassLoader.getSystemClassLoader().getResource("").getPath();
            throw new RuntimeException(e);
        }

        return helper;
    }

    public String getText(String nodeName){
        return root.element(nodeName).getTextTrim();
    }

    public String getAttribute(String nodeName,String attributeName){

        return root.element(nodeName).attributeValue(attributeName);
    }

    /**
     * get node's attributes,a set of key,value pair,
     * which present attribute name, attribute value
     * @param nodeName
     * @return Map<String,String>, key:attribute name,value:attribute value
     */
    public Map<String,String> getAttributes(String nodeName){
        Map<String,String> attributeKV = Maps.newHashMap();
        for (Object attribute : root.element(nodeName).attributes()) {
            attributeKV.put(((Attribute) attribute).getName(), ((Attribute) attribute).getStringValue());
        }

        return attributeKV;
    }

    /**
     * get the children elements' name,text pair of the given element
     * @param element
     * @return
     */
    public Map<String,String> getNameAndTextMapForElement(Element element){
        Map<String,String> nameAndTextMap=Maps.newHashMap();
        for (Object e: element.elements()) {
            nameAndTextMap.put(((Element) e).getName(), ((Element) e).getTextTrim());
        }

        return nameAndTextMap;
    }

    /**
     * 获取所有的elements mapping:
     * key: elementName,used to Mapping element to page objects
     * value: text value,like id = ?
     * @return
     */
    public Map<String,String> getNameAndTextForAllElements(){
        return getNameAndTextMapForElement(root);
    }


    public Element getRoot() {
        return root;
    }

    public Document getDoc() {
        return doc;
    }

}
