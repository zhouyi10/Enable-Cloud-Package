package com.enableets.edu.sdk.ppr.xml.xpath;

import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Properties;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/24
 **/
public class XNode {

    private final Node node;
    private final String name;
    private final String body;
    private final Properties attributes;
    private final XPathParser xpathParser;

    public XNode(XPathParser xpathParser, Node node) {
        this.xpathParser = xpathParser;
        this.node = node;
        this.name = node.getNodeName();
        this.attributes = parseAttributes(node);
        this.body = parseBody(node);
    }

    public XNode newXNode(Node node){
        return new XNode(xpathParser, node);
    }

    public String evalString(String expression){
        return xpathParser.evalString(node, expression);
    }

    public Boolean evalBoolean(String expression){
        return xpathParser.evalBoolean(node, expression);
    }

    public Integer evalInteger(String expression){
        return xpathParser.evalInteger(node, expression);
    }

    public Long evalLong(String expression){
        return xpathParser.evalLong(node, expression);
    }

    public Float evalFloat(String expression){
        return xpathParser.evalFloat(node, expression);
    }

    public XNode evalNode(String expression){
        return xpathParser.evalNode(node, expression);
    }

    public List<XNode> evalNodes(String expression){
        return xpathParser.evalNodes(node, expression);
    }

    public String getAttribute(String name){
        return attributes.getProperty(name);
    }

    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
                attributes.put(attribute.getNodeName(), attribute.getNodeValue());
            }
        }
        return attributes;
    }

    private String parseBody(Node node) {
        String data = getBodyData(node);
        if (data == null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                data = getBodyData(child);
                if (data != null) {
                    break;
                }
            }
        }
        return data;
    }

    private String getBodyData(Node child) {
        if (child.getNodeType() == Node.CDATA_SECTION_NODE
                || child.getNodeType() == Node.TEXT_NODE) {
            return ((CharacterData) child).getData();
        }
        return null;
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public Properties getAttributes() {
        return attributes;
    }

    public XPathParser getXpathParser() {
        return xpathParser;
    }
}
