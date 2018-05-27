package org.ranjith.util.xml.merge.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.ranjith.util.xml.merge.common.CommonUtils;
import org.ranjith.util.xml.merge.common.Constnats;
import org.ranjith.util.xml.merge.exception.XMLMergeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLMergeProcessor {

    private List<String> leafNodes = new ArrayList<String>();

    public String process(String... args) {
        try {
            CommonUtils.validateInputArgs(args);

            // parse input args
            String srcFile1 = args[0];
            String srcFile2 = args[1];
            String destFile = args[2];

            CommonUtils.validateFileExist(srcFile1);
            CommonUtils.validateFileExist(srcFile2);

            if (args.length > Constnats.MANDATARY_CLI_ARGS) {
                String leafNodeArray = args[3];
                this.leafNodes = Arrays.asList(StringUtils.split(leafNodeArray, ","));
            }

            // dump source file1 to destination file
            this.dump(new File(srcFile1), new File(destFile));

            // merge file 2 into destination file
            this.merge(srcFile2, destFile);

            return "Success";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "Error";
    }

    private void dump(File src, File dest) {
        try {
            FileUtils.copyFile(src, dest);
        } catch (IOException ex) {
            throw new XMLMergeException(ex);
        }
    }

    private void merge(String srcFilePath, String destFilePath) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document sourceXml = builder.parse(srcFilePath);
            Document destXml = builder.parse(destFilePath);

            Element srcRoot = sourceXml.getDocumentElement();
            Element destRoot = destXml.getDocumentElement();

            StringBuilder currentNodePath = new StringBuilder("");
            Node mergedXml = merge(srcRoot, destRoot, destXml, currentNodePath);

            // write to xml file
            this.writeToXml(mergedXml, destFilePath);

        } catch (Exception ex) {
            throw new XMLMergeException(ex.getMessage());
        }

    }

    private Node merge(Node srcRoot, Node destRoot, Document destXml, StringBuilder currentXPath)
            throws XPathExpressionException {
        if (srcRoot != null && srcRoot.getLocalName() != null) {
            // form the xpath
            String parentNodePath = currentXPath.toString();
            StringBuilder currentNodePath = new StringBuilder(currentXPath.toString());
            currentNodePath.append("/");
            currentNodePath.append(srcRoot.getLocalName());
            if (srcRoot.hasAttributes()) {
                NamedNodeMap map = srcRoot.getAttributes();
                if (map != null && map.getLength() > 0) {
                    currentNodePath.append(prepareAttrExp(map));
                }
            }

            System.out.println("[INFO]Comparing node: " + currentNodePath);
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpr = xPath.compile("count(" + currentNodePath + ") > 0");
            Boolean elementExists = (Boolean) xPathExpr.evaluate(destXml, XPathConstants.BOOLEAN);

            // if xpath exists in dest, recursively call for sub nodes
            if (elementExists) {
                System.out.println("[INFO]xpath exists in dest, making recursive call for sub nodes");
                NodeList nodes = srcRoot.getChildNodes();
                if (nodes != null && nodes.getLength() > 0) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        merge(node, destRoot, destXml, currentNodePath);
                    }
                }
            } else {
                System.out.println("[INFO]xpath does not exist, this node will be added:" + srcRoot.getLocalName());
                // else add the element to the destination node
                // Create a duplicate node
                Node newNode = srcRoot.cloneNode(true);
                // Transfer ownership of the new node into the destination document
                destXml.adoptNode(newNode);
                if (StringUtils.isEmpty(parentNodePath)) {
                    parentNodePath = "/";
                }
                xPathExpr = xPath.compile(parentNodePath);
                // evaluate expression result on XML document
                Node node = (Node) xPathExpr.evaluate(destXml, XPathConstants.NODE);
                node.appendChild(newNode);
            }
        }
        return destXml;
    }

    private StringBuilder prepareAttrExp(NamedNodeMap map) {
        StringBuilder attrExp = new StringBuilder("[");
        for (int i = 0; i < map.getLength(); i++) {
            Node node = map.item(i);
            attrExp.append("@");
            attrExp.append(node.getLocalName());
            attrExp.append("='");
            attrExp.append(node.getTextContent());
            attrExp.append("'");

            if (i + 1 < map.getLength()) {
                attrExp.append(" and ");
            }
        }
        attrExp.append("]");
        return attrExp;
    }

    private void writeToXml(Node xmlDoc, String filePath) {
        try {
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
        } catch (Exception ex) {
            throw new XMLMergeException(ex.getMessage());
        }
    }
}
