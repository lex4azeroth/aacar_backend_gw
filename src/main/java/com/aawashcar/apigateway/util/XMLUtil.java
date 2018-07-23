package com.aawashcar.apigateway.util;


import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XMLUtil {

    /**
     * 将 XML 字符串转换为 Java 对象
     * 
     * @param clazz 要转换对象的 class
     * @param xml 待转换的 xml
     * @return 转换后的对象
     */
    public static Object xmlToBean(Class<?> clazz, String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller us = jc.createUnmarshaller();
            return us.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
