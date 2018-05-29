package cn.com.pax.utils;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.com.pax.Constant;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 报告头映射关系
 *
 * @author luohl
 * @create 2018-01-18-20:46
 */
public class ReportMap {

    public  Map<String,String> getReportMap(){
        Map<String ,String>colMap = new HashMap<>();
        try {
            Properties properties=new Properties();
            InputStreamReader is = new InputStreamReader(new FileInputStream(Constant.REPORTCFG),"utf-8");
            properties.load(is);
            Enumeration propertyNames = properties.propertyNames();
            while(propertyNames.hasMoreElements()) {
                String key = (String) propertyNames.nextElement();
                String value = properties.getProperty(key);
                colMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return colMap;
    }
    public static void main(String[] args) {
        ReportMap reportMap = new ReportMap();
        System.out.println(reportMap.getReportMap());
    }
}
    