package cn.com.pax.utils;

import cn.com.pax.Constant;
import com.sun.media.sound.SoftTuning;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取表格中信息
 *
 * @author luohl
 * @create 2017-12-25-11:07
 */
public class ReadExcelCfgUtils {
    private  Map<String,String>excelCfg = new HashMap<>();

    public Map<String, String> getExcelCfg() {
        return excelCfg;
    }

    public boolean  readExcel(){
        try{
            InputStream is = new FileInputStream(Constant.EXCEL);
            XSSFWorkbook  wb = new XSSFWorkbook(is);
            XSSFSheet st = wb.getSheetAt(0);
            for (int i=1; i<=st.getLastRowNum();i++ ) {
                excelCfg.put(st.getRow(i).getCell(0).toString(), st.getRow(i).getCell(1).toString());
            }
            //System.out.println("的值是：---"+ excelCfg.size() + "，当前方法=readExcel.ReadExcelCfgUtils()");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
       return true;
    }
    public static String strConvert(String str){
        if(Constant.NUMTYPE.equals(str)){
            return "NUMERIC";
        }else if(Constant.ALPHTYPE.equals(str)){
            return "ALPHANUMERIC";
        }else if(Constant.BYTETYPE.equals(str)){
            return "BYTE";
        }else {
            return "BYTE";
        }
    }
    public static String parseContend(String str){
        StringBuilder builder = new StringBuilder();
        String strs[] = str.split("\\[{2}.+?\\]{2}");
        for (String s : strs) {
            if (s.length()!=0){
                builder.append(s.trim());
            }
        }
        return builder.toString();
    }

    public static String getTypeContend(String str){
        StringBuilder builder = new StringBuilder();
        List<String>type = new LinkedList<>();
        List<String>contend = new LinkedList<>();

        Pattern pattern = Pattern.compile("\\[{2}(.+?)\\]{2}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            type.add(strConvert(matcher.group(1).trim()));
            //System.out.println("ggg"+matcher.group(1));
        }
        String strs[] = str.split("\\[{2}.+?\\]{2}");
        for (String s : strs) {
            if (s.length()!=0){
                contend.add(s.trim());
            }
        }
        for (int i = 0; i < type.size(); i++) {
            for (int j = 0; j < contend.size(); j++) {
                if (i == j)
                builder.append("[["+type.get(i)+"]]"+contend.get(i));
            }
        }
        //以防配置写错的时候的处理。
        if (type.size()!= contend.size()){
            return null;
        }
        return builder.toString();
    }

    public static void getTypeContend(String str,List<String>type,List<String>contend){
        Pattern pattern = Pattern.compile("\\[{2}(.+?)\\]{2}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            type.add(matcher.group(1));
        }
        String strs[] = str.split("\\[{2}.+?\\]{2}");
        for (String s : strs) {
            if (s.length()!=0){
                contend.add(s);
            }
        }
    }
    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
        List<String>key = new LinkedList<>();
        List<String>contend = new LinkedList<>();
//        Map<String,String>typeContend = new LinkedHashMap<>();
//        Pattern pattern = Pattern.compile("(\\[{2}.+?\\]{2})+");
//        String str ="[[A1]]";
//        String xxx=  "[a-zA-Z0-9]{2}";
        String xxxxx = "[[数字]]6227012345678901" +
                "[[字母]]CAF2A0D4CD18B2EBA000000333010101000107020103900000010A010000004997E63325D7"+
                "7C00112233446230810314221301D261122000000956000F6227012345678901020"+
                "[[Byte]];6213531230000000061=30112200715500220?;" +
                "[[Byte]]QR码测试，个人化数据，中国汉字模式。";

        String xxxxxxx = "[[Byte]]QR码测试" +
                "[[数字]]2016" +
                "[[Byte]]V1.0！";
        String yyyy = "[[字母]]QR[[Byte]]码测试[[数字]]2016[[Byte]]V1.0！";

        String xxxxxxxxx = "[[Byte]]QR码测试";
        System.out.println(parseContend(xxxxx));

//        String xxff = "[26/Feb/2001:10:25:34 -5000]";
//        String str1 = "172.26.155.241 - - [26/Feb/2001:10:56:03 -0500] \"GET /IsAlive.htm HTTP/1.0\" 200 15";
//        (\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})：匹配前面的IP地址
//
//        \\s\\-\\s\\-\\s\\：匹配“ - -”
//       [([^]]+)\\]\\s：匹配 “[26/Feb/2001:10:56:03 -0500] ”
//        \"\\w* /[\\w.\\s/]*\"：匹配“\"GET /IsAlive.htm HTTP/1.0\"”
//       \\s\\d*\\s\\d*：匹配“ 200 15”
       // Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(\\s\\-\\s\\-\\s)(\\[([^]]+)\\])\\s(\"\\w* /[\\w.\\s/]*\")\\s\\d*\\s\\d*") ;

//        Matcher m = p.matcher(str1) ; //调用Matcher类中的matches()方法，测试是否可以匹配
//        while(m.find()){
//        System.out.println(m.group(5));}
//         if(m.matches()){
//             System.out.println("匹配成功") ;
//         } else {
//             System.out.println("不能匹配") ;
//         }
//        ReadExcelCfgUtils readExcelCfgUtils = new ReadExcelCfgUtils();
//        readExcelCfgUtils.readExcel();
//        String str = readExcelCfgUtils.getExcelCfg().get("M4");
//        System.out.println("####"+str);


        System.out.println(getTypeContend(yyyy));
        getTypeContend(getTypeContend(yyyy),key,contend);
        System.out.println(key);
        System.out.println("\n\n");
        System.out.println(contend);
        System.out.println("hhhhssff");

//        Matcher matcher = pattern.matcher(xxxxxxx);
//        while (matcher.find()){
//            System.out.println("matcher.find()"+ matcher.group());
//            key.add(matcher.group());
//        }
//        System.out.println("matcher.group()"+key.size());
//
//
//        String aa[] = xxxxx.split("\\[{2}.+?\\]{2}");
//        System.out.println(aa.length);
//        for (String s : aa) {
//            if (s.length()!=0){
//                System.out.println(s+"xxx");
//            contend.add(s);
//            }
//        }
//        String aaaa = key.get(0).substring(key.get(0).indexOf("[[")+2,key.get(0).lastIndexOf("]]"));
//        System.out.println(aaaa);

//        String aa = "[[A1]]";
//        System.out.println("的值是：---"+ "[[A1]]".equals(aa) + "，当前方法=main.ReadExcelCfgUtils()");
//        if (aa.matches("\\[{2}[a-zA-Z0-9]{2}\\]{2}")){
//            aa = aa.substring(aa.indexOf("[[")+2,aa.lastIndexOf("]]"));
//            System.out.println("的值是：---"+ aa + "，当前方法=main.ReadExcelCfgUtils()");
//            System.out.println("hha");
//        }else{
//            System.out.println("oooo");
//        }
//        ReadExcelCfgUtils readExcelCfgUtils = new ReadExcelCfgUtils();
//        readExcelCfgUtils.readExcel();
//        System.out.println("的值是：---"+readExcelCfgUtils.getExcelCfg()  + "，当前方法=main.ReadExcelCfgUtils()");
    }

}
    