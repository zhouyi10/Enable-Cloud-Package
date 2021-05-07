package com.enableets.edu.sdk.ppr.xml.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/24
 **/
public class XPathParser {

    private final Document document;

    private XPath xpath;

    public XPathParser(Document document){
        buildXpath();
        this.document = document;
    }

    public XPathParser(String xml){
        buildXpath();
        this.document = createDocument(new InputSource(new StringReader(xml)));
    }

    public XPathParser(Reader reader){
        buildXpath();
        this.document = createDocument(new InputSource(reader));
    }

    public XPathParser(InputStream inputStream){
        buildXpath();
        this.document = createDocument(new InputSource(inputStream));
    }

    private Document createDocument(InputSource inputSource){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(null);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {

                }

                @Override
                public void error(SAXParseException exception) throws SAXException {

                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {

                }
            });
            return builder.parse(inputSource);
        }catch (Exception e){
            throw new PPRXPathException("XPathParser build fail!");
        }
    }

    public String evalString(String expression){
        return evalString(document, expression);
    }

    public String evalString(Object root, String expression){
        return (String) evaluate(expression, root, XPathConstants.STRING);
    }

    public Boolean evalBoolean(String expression){
        return (Boolean) evalBoolean(document, expression);
    }

    public Boolean evalBoolean(Object root, String expression){
        return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
    }

    public Integer evalInteger(String expression){
        return evalInteger(document, expression);
    }

    public Integer evalInteger(Object root, String expression){
        return Integer.valueOf(evalString(root, expression));
    }

    public Long evalLong(String expression) {
        return evalLong(document, expression);
    }

    public Long evalLong(Object root, String expression) {
        return Long.valueOf(evalString(root, expression));
    }

    public Float evalFloat(String expression) {
        return evalFloat(document, expression);
    }

    public Float evalFloat(Object root, String expression) {
        return Float.valueOf(evalString(root, expression));
    }

    public XNode evalNode(String expression){
        return evalNode(document, expression);
    }

    public XNode evalNode(Object root, String expression){
        Node node = (Node) evaluate(expression, root, XPathConstants.NODE);
        if (node == null) return null;
        return new XNode(this, node);
    }

    public List<XNode> evalNodes(String expression){
        return evalNodes(document, expression);
    }

    public List<XNode> evalNodes(Object root, String expression){
        List<XNode> xNodes = new ArrayList<>();
        NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            xNodes.add(new XNode(this, nodes.item(i)));
        }
        return xNodes;
    }

    private Object evaluate(String expression, Object root, QName returnType){
        try {
            return xpath.evaluate(expression, root, returnType);
        } catch (Exception e) {
            throw new PPRXPathException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

    private void buildXpath(){
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
    }
}
