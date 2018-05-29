package cn.com.pax.report;

import cn.com.pax.log.ParseXml;
import freemarker.template.TemplateException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportCreate {
    public static final String dirPath = ".\\doc\\";
    public static void build(Map<String, String> colMap, EnvInfo info, String resultFile, String toFilePath) throws IOException, TemplateException{
        // xml的文件名
        String xmlTemplate = "new_doc.xml";
        // docx的路径和文件名
        String docxTemplate = dirPath + "new_template.docx";
        // 填充完数据的临时xml
        String xmlTemp = dirPath + "\\temp.xml";
        // 目标文件名

        Writer w = new FileWriter(new File(xmlTemp));
        // 1.需要动态传入的数据
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("info", info);

        ParseXml xml = new ParseXml(resultFile);
        List<TemplateCls> temInfo = new ArrayList<>();
        List<Node> nodes = xml.searchXml("/project/dir/dir/dir");
        int i = 1;
        for(Node node: nodes) {
            Element element = (Element) node;
            String name = element.attribute("name").getValue();
            List<Node> childs = element.selectNodes("file");
            List<ResultInfo> resultInfos = new ArrayList<>();
            for(Node ch: childs) {
                Element el = (Element)ch;
                if(el.attribute("result") != null) {
                    String nm = el.attributeValue("name");
                    String res = el.attributeValue("result");
                    String detail = el.attributeValue("detail");
                    if(detail == null) detail="";
                        resultInfos.add(new ResultInfo(nm, res, detail));
                }
            }
            TemplateCls ttInfo = new TemplateCls(i, name, colMap.get(name), resultInfos);
            temInfo.add(ttInfo);
            i++;
        }

        p.put("temp", temInfo);
        XmlToExcel.process(xmlTemplate, p, w);
        w.close();
        // 3.把填充完成的xml写入到docx中
        XmlToExcel.outDocx(new File(xmlTemp), docxTemplate, toFilePath);
    }
}
