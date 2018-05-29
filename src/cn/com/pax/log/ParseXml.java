package cn.com.pax.log;

import cn.com.pax.utils.FileUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ParseXml {

    private Document doc;
    private File xmlFile;

    public ParseXml(String filepath) {
        SAXReader reader = new SAXReader();
        doc = null;
        try {
            xmlFile = new File(filepath);
            doc = reader.read(xmlFile);
        } catch(DocumentException e) {
            e.printStackTrace();
        }
    }

    public List<Node> searchXml(String xpathstr) {
        if(doc == null) throw new NullPointerException("searchXml(): doc is null");
        List<Node> listNodes = doc.selectNodes(xpathstr);
        return listNodes;
    }

    public String searchOneNode(String xpath) {
        List<Node> nodes = searchXml(xpath);
        if(nodes != null && nodes.size() > 0) {
            Element element = (Element) (nodes.get(0));
            return element.attributeValue("name");
        }
        return null;
    }

    public List<String> serchMoreNode(String xpath) {
        List<Node> nodes = searchXml(xpath);
        List<String> list = new ArrayList<>();
        if(nodes != null && nodes.size() > 0) {
            for(Node nn : nodes) {
                Element ee = (Element)nn;
                list.add(ee.attributeValue("name"));
            }
        }
        return list;
    }

    public Map<String, List<String>> serchChilden(String xpath, String child) {
        List<Node> nodes = searchXml(xpath);
        Map<String, List<String>> mList = new HashMap<>();
        if(nodes != null && nodes.size() > 0) {
            for(Node nn : nodes) {
                Element ee = (Element)nn;
                List<Node> childs = ee.selectNodes(child);
                List<String> chList = new ArrayList<>();
                for(Node ch: childs) {
                    chList.add(((Element)ch).attributeValue("name"));
                }
                mList.put(ee.attributeValue("name"), chList);
            }
        }
        return mList;
    }

    public Map<String, String> queryCaseResult() {
        if(doc == null) throw new NullPointerException("queryCaseResult(): doc is null");
        List<Node> lists = searchXml("/project/dir/dir/dir/file");
        Map<String, String> results = new HashMap<>();
        for(Node node: lists) {
            Element element = (Element) node;
            String resultStr = element.attributeValue("result");
            if(resultStr != null)
                results.put(element.attributeValue("name"), resultStr);
        }
        return results;
    }

//    public Map<String, String> queryCaseDetail() {
//        if(doc == null) throw new NullPointerException("queryCaseResult(): doc is null");
//        List<Node> lists = searchXml("/project/dir/dir/dir/file");
//        Map<String, String> results = new HashMap<>();
//        for(Node node: lists) {
//            Element element = (Element) node;
//            String  detailStr= element.attributeValue("detail");
//            if(detailStr != null)
//                results.put(element.attributeValue("name"), detailStr);
//        }
//        return results;
//    }

    public  Map<String, List<String>> queryCaseLogResult() {
        if(doc == null) throw new NullPointerException("queryCaseLogResult(): doc is null");
        List<Node> lists = searchXml("/project/dir/dir/dir/file");
        Map<String, List<String>> results = new LinkedHashMap<>();
        for(Node node: lists) {
            Element element = (Element) node;
            String resultStr = element.attributeValue("result");
            // System.out.println("element.elements(case);的值是：---"+  element.elements() + "，当前方法=queryCaseLogResult.ParseXml()");
            if(resultStr != null)
                if ( element.elements().size() > 0){
                    List<Element>elements = element.elements();
                    for (Element element1 : elements) {
                        String detail = null;
                        if ("".equals(element1.attributeValue("detail"))){
                            detail = "null";
                        }else{
                            detail = element1.attributeValue("detail");
                        }
                        String str = element1.attributeValue("name")+","+element1.attributeValue("result")+","+ detail;
                        if (results.containsKey(element.attributeValue("name"))){
                            List <String> list = results.get(element.attributeValue("name"));
                            list.add(str);
                        }else{
                            List<String> tempList = new ArrayList<>();
                            tempList.add(str);
                            results.put(element.attributeValue("name"), tempList);
                        }
                    }
                }
        }
       // System.out.println(results);
        return results;
    }

    public void addResult(String caseName, String testResult, String logFileName) {
        if(doc == null) throw new NullPointerException("addResult(): doc is null");
        List<Node> lists = searchXml("/project/dir/dir/dir/file");
        for(Node node: lists) {
            Element element = (Element)node;
           if(element.attributeValue("name").equalsIgnoreCase(caseName)) {
                element.addAttribute("result", testResult);
                element.addElement("case").addAttribute("name", logFileName)
                        .addAttribute("result", testResult);
           }
       }
    }

    public void addResult(String caseName, String testResult, String logFileName, String detail) {
        if(doc == null) throw new NullPointerException("addResult(): doc is null");
        List<Node> lists = searchXml("/project/dir/dir/dir/file");
        for(Node node: lists) {
            Element element = (Element)node;
            if(element.attributeValue("name").equalsIgnoreCase(caseName)) {
                element.addAttribute("result", testResult).addAttribute("detail", detail);
                element.addElement("case").addAttribute("name", logFileName)
                        .addAttribute("result", testResult).addAttribute("detail", detail);
            }
        }
    }

//    public static Document createDocument(String projectName, String firstDir, String secondDir, List<String> typeList, Map<String, List<String>> caseMap) {
//        Document document = DocumentHelper.createDocument();
//        Element root = document.addElement("project");
//        root.addAttribute("name", projectName);
//
//        Element firstEl = root.addElement("dir")
//                .addAttribute("name", firstDir);
//
//        Element secondEl = firstEl.addElement("dir")
//                .addAttribute("name", secondDir);
//
//        for(String typeStr: typeList) {
//            Element thirdEL = secondEl.addElement("dir").addAttribute("name", typeStr);
//            for(String caseStr: caseMap.get(typeStr)) {
//                thirdEL.addElement("file").addAttribute("name", caseStr);
//            }
//        }
//        return document;
//    }

    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        return document;
    }

    public static Element createFirstEL(Document document, String projectName, String firstDir) {
        Element root = document.addElement("project");
        root.addAttribute("name", projectName);
        Element firstEl = root.addElement("dir")
                .addAttribute("name", firstDir);
        return firstEl;
    }

    public static Element createSecondEL(Element firstEL, String secondDir) {
        Element secondEl = firstEL.addElement("dir")
                .addAttribute("name", secondDir);
        return secondEl;
    }

    public static void createChildren(Element secondEl, List<String> typeList, Map<String, List<String>> caseMap) {
        for(String typeStr: typeList) {
            Element thirdEL = secondEl.addElement("dir").addAttribute("name", typeStr);
            for(String caseStr: caseMap.get(typeStr)) {
                thirdEL.addElement("file").addAttribute("name", caseStr);
            }
        }
    }

    public static void writeXml(Document document, File xmlName) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream(xmlName), format);
        writer.write(document);
        writer.close();
    }

    public void writeXml() throws IOException {
        if(doc == null) throw new NullPointerException("writeXml(): doc is null");
        writeXml(doc, xmlFile);
    }

    public static void main(String[] args) throws Exception{
//        ParseXml xml = new ParseXml("456.xml");
//        Map<String, List<String>> tt = xml.serchChilden("/project/dir/dir/dir", "file");
//        System.out.println(tt);
//        List<Node> nodes = xml.searchXml("//book");
//        for(Node node: nodes) {
//            Element element =  (Element)node;
//            System.out.println(element.getName());
//            List<Node> nn = element.selectNodes("title");
//            if(nn.size() > 0) {
//                System.out.println(((Element)nn.get(0)).getStringValue());
//            }
//        }

        //query result;
  //      ParseXml xml = new ParseXml("456.xml");
  //      System.out.println(xml.queryCaseResult());

        //update test result
        ParseXml xml = new ParseXml("789.xml");
        xml.addResult("ZCCS002.txt", "PASS", "ZCCS002-10-01-12-14-56-45.html");
        xml.addResult("TSPZ001.txt", "PASS", "TSPZ001-10-02-14-14-45-01.html");
        xml.writeXml();

        //generate new document
        List<String> typeList = new ArrayList<>();
        Map<String, List<String>> caseMap = new HashMap<>();

        List<String> typeList2 = new ArrayList<>();
        Map<String, List<String>> caseMap2 = new HashMap<>();

        String projectName = "Pax-A920-2017-12-06";
        String firstDir = "BCTC";
        String secondDir1 = "条码测试";
        String secondDir2 = "条码调试";

        //File travelDir = new File("." + File.separator + firstDir + File.separator + secondDir1);
        FileUtils.xmlConfig("." + File.separator + firstDir + File.separator + secondDir1, typeList, caseMap);
        //File travelDir2 = new File("." + File.separator + firstDir + File.separator + secondDir2);
        FileUtils.xmlConfig("." + File.separator + firstDir + File.separator + secondDir2, typeList2, caseMap2);

        Document document = ParseXml.createDocument();
        Element firstEl = ParseXml.createFirstEL(document,projectName, firstDir);
        Element secondE1 = ParseXml.createSecondEL(firstEl, secondDir1);
        Element secondE2 = ParseXml.createSecondEL(firstEl, secondDir2);
        ParseXml.createChildren(secondE1, typeList, caseMap);
        ParseXml.createChildren(secondE2, typeList2, caseMap2);
        writeXml(document, new File("789.xml"));

        //寻找file node
//        ParseXml parseXml = new ParseXml("123.xml");
//        List<Node> lists = parseXml.searchXml("/project/dir/dir/dir/file");
//        for(Node node: lists) {
//            Element element = (Element)node;
//            if(element.attributeValue("name").equalsIgnoreCase("BZCS241.txt")) {
//                element.addAttribute("result", "FAIL");
//                element.addElement("case").addAttribute("name", "BZCS241-10-01-12-14-56-45.html")
//                        .addAttribute("result", "FAIL");
//            }
//        }
//        parseXml.writeXml();
    }

}
