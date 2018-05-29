package cn.com.pax.report;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class XmlToExcel {
    private static XmlToExcel tplm = null;
    private Configuration cfg = null;

    private XmlToExcel() throws IOException{
        cfg = new Configuration(Configuration.VERSION_2_3_27);
        // 注册tmlplate的load路径
        // cfg.setClassForTemplateLoading(this.getClass(), "/template/");
        cfg.setDirectoryForTemplateLoading(new File(ReportCreate.dirPath));
    }

    private static Template getTemplate(String name) throws IOException {
        if (tplm == null) {
            tplm = new XmlToExcel();
        }
        return tplm.cfg.getTemplate(name);
    }

    public static void process(String templatefile, Map param, Writer out) throws IOException, TemplateException {
        // 获取模板
        Template template = XmlToExcel.getTemplate(templatefile);
        template.setOutputEncoding("UTF-8");
        // 合并数据
        template.process(param, out);
        if (out != null) {
            out.close();
        }
    }

    public static void outDocx(File documentFile, String docxTemplate, String toFilePath) throws IOException {
        File docxFile = new File(docxTemplate);
        ZipFile zipFile = new ZipFile(docxFile);
        Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
        ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(toFilePath));
        int len = -1;
        byte[] buffer = new byte[1024];
        while (zipEntrys.hasMoreElements()) {
            ZipEntry next = zipEntrys.nextElement();
            InputStream is = zipFile.getInputStream(next);
            // 把输入流的文件传到输出流中 如果是word/document.xml由我们输入
            zipout.putNextEntry(new ZipEntry(next.toString()));
            if ("word/document.xml".equals(next.toString())) {
                InputStream in = new FileInputStream(documentFile);
                while ((len = in.read(buffer)) != -1) {
                    zipout.write(buffer, 0, len);
                }
                in.close();
            } else {
                while ((len = is.read(buffer)) != -1) {
                    zipout.write(buffer, 0, len);
                }
                is.close();
            }
        }
        zipout.close();
    }
}
