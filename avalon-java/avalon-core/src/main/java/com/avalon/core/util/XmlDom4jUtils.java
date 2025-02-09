/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.module.AbstractModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


@Slf4j
public class XmlDom4jUtils {
    public static ArrayList<InputStream> loadModuleResourceFile(AbstractModule module) throws AvalonException {
        try {
            ArrayList<InputStream> files = new ArrayList<>();
            for (String s : module.getResource()) {
                String path = ClassUtils.getModulePackagePath(module);
                if (s.startsWith("/")) {
                    path = path + s;
                } else {
                    path = path + "/" + s;
                }
                URL resource = module.getClass().getClassLoader().getResource(path);
                if (ObjectUtils.isNull(resource)) {
                    continue;
                }
                log.info("loadModuleResourceFile,{}", resource.toURI());
                InputStream inputStream = resource.openStream();
                files.add(inputStream);
            }

            return files;
        } catch (Exception e) {
            throw new AvalonException("加载资源文件时发生错误: " + e.getMessage(), e);
        }
    }

    public static JsonNode openXmlFile(File file) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readTree(file);
        } catch (IOException e) {
            throw new AvalonException("加载xml文件时发生错误: " + e.getMessage(), e);
        }
    }

    /**
     * 获取根元素
     *
     * @param file
     * @return
     * @throws AvalonException
     */
    public static Element getRootElement(File file) throws AvalonException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            return document.getDocumentElement();
        } catch (Exception ex) {
            throw new AvalonException("加载xml文件时发生错误: " + ex.getMessage(), ex);
        }
    }

    public static Element getRootElement(InputStream inputStream) throws AvalonException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            return document.getDocumentElement();
        } catch (Exception ex) {
            throw new AvalonException("加载xml文件时发生错误: " + ex.getMessage(), ex);
        }
    }

    public static Document getXmlDocument(String xmlStr) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes());
            return builder.parse(inputStream);
        } catch (Exception ex) {
            throw new AvalonException("识别xml字符串出错: " + ex.getMessage(), ex);
        }
    }

    public static NodeList getXpathElement(String xmlStr) {
        try {
            xmlStr = "<inherit>" + xmlStr + "</inherit>";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes());
            Document document = builder.parse(inputStream);

            return document.getDocumentElement().getElementsByTagName("xpath");
        } catch (Exception ex) {
            throw new AvalonException("识别xml字符串出错: " + ex.getMessage(), ex);
        }
    }

    public static String getXmlString(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("indent", "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document),new StreamResult(writer));

            return writer.toString();
        } catch (TransformerException e) {
            throw new AvalonException("识别xml字符串出错: " + e.getMessage(), e);
        }
    }
}
