package cn.com.pax.utils;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.com.pax.Constant;
import cn.com.pax.barcode.GenCodeNew;
import cn.com.pax.barcode.GenerateCode;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import cn.com.pax.log.RecordLog;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * 文件操作相关的类
 *
 * @author luohl
 * @create 2017-12-05-9:54
 */
public class FileUtils {

    private static String IP_C4 ;
    private static String PORT_C4;
    private static String IP_PHONE ;
    private static String PORT_PHONE;

    public static String getIpC4() {
        return IP_C4;
    }

    public static String getPortC4() {
        return PORT_C4;
    }

    public static String getIpPhone() {
        return IP_PHONE;
    }

    public static String getPortPhone() {
        return PORT_PHONE;
    }

    private static  List<String> typeList = new ArrayList<String>();
    private static  Map<String, List<String>> caseMap = new HashMap<>();

    public static List<String> getTypeList() {
        return typeList;
    }

    public static Map<String, List<String>> getCaseMap() {
        return caseMap;
    }

    public static void readCfg(){
        Properties commConfig = new Properties();
        try {
            commConfig.load(new FileReader(Constant.CFG));
            IP_C4 = commConfig.getProperty("IP_C4");
            PORT_C4 = commConfig.getProperty("PORT_C4");
            IP_PHONE = commConfig.getProperty("IP_PHONE");
            PORT_PHONE = commConfig.getProperty("PORT_PHONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  void writeComm2File(String key,String value){
        File scanDepthConfig = new File(Constant.CFG);
        if (!scanDepthConfig.exists()) {
            try {
                scanDepthConfig.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Properties prop = new Properties();
            FileInputStream fin = new FileInputStream(scanDepthConfig);
            prop.load(fin);
            fin.close();
            //System.out.println(prop);
            prop.setProperty(key, value);
            FileOutputStream fout = new FileOutputStream(scanDepthConfig);
            prop.store(fout, "Communiction Config");
            fout.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    public static String absolutePath(String path){
        String absolutePath = new File(path).getAbsolutePath();
        return absolutePath.substring(0,absolutePath.length()-1);
    }
    public static boolean createDir(String dir){
        File file = new File(dir);
        boolean flag = true;
        if(!file.exists()){
            flag = file.mkdir();
        }
        return flag;
    }
    public static void writeConfig(String path,String contend){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));
            bufferedWriter.write(contend);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @NotNull
    public static String readConfig(String path){
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            String s;
            while ((s = bufferedReader.readLine())!= null){
                stringBuilder.append(s+"\r\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 写景深到配置文件
     * @param minDis
     * @param maxdis
     * @param flag
     * @throws IOException
     */
    public static void writeOptimalDepth(int minDis,int maxdis,int flag) throws  IOException{
        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
        StringBuffer stringBuffer = new StringBuffer();
        String selectFile;
        if (flag == 1){
            selectFile = Constant.OPTIMADEPTH1; //一维
        }else{
            selectFile =   Constant.OPTIMADEPTH;
        }
        File file =new File(path + File.separator + selectFile);
        if (!file.exists()) {
            stringBuffer.append("最小景深" + "   " + "最大景深"+"\r\n");
        }
        if (minDis >0 && maxdis > 0){
            stringBuffer.append(minDis + "            " + maxdis + "\r\n");
            org.apache.commons.io.FileUtils.writeStringToFile(file, stringBuffer.toString(),
                    "UTF-8", true);
        }

    }

    /**
     * 计算最优景深
     * @param str
     * @return
     * @throws IOException
     */
    public static int calcOptimalDepth(String str,int flag) {
        try {
            String path =str.split(Constant.CONFIG)[0];
            String selectFile;
            if (flag == 1){
                selectFile = Constant.OPTIMADEPTH1; //一维
            }else{
                selectFile =   Constant.OPTIMADEPTH;
            }

            File file =new File(path +File.separator+selectFile);
            //System.out.println("file=" + file);
            if (file.exists()){
                List<String> list = org.apache.commons.io.FileUtils.readLines(file,"utf-8");
                //System.out.println("list=" + list);
                List<Integer>minList = new ArrayList<>();
                List<Integer>maxList= new ArrayList<>();
                for (int i = 1; i < list.size(); i++) {
                    if (StringUtils.isNotBlank(list.get(i).trim())){
                        minList.add(Integer.parseInt(list.get(i).trim().replaceAll(" +",",").split(",")[0].trim()));
                        maxList.add(Integer.parseInt(list.get(i).trim().replaceAll(" +",",").split(",")[1].trim()));
                    }
                }
                //System.out.println(Collections.max(minList));
               // System.out.println(Collections.min(maxList));
                if (Collections.max(minList)>Collections.min(maxList)){
                    return -1;
                }
                return (Collections.max(minList)+Collections.min(maxList))/2;
            }else{
               // System.out.println("file.exists()=" + file.exists());
                return -1;
            }
        }catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    //写入xml
    public static void xmlConfig(String path,List<String> typeList, Map<String, List<String>> caseMap){
        File file = new File(path) ;
        for(File dirN: file.listFiles()) {
            typeList.add(dirN.getName());
            if(dirN.isDirectory()) {
                File[] files = dirN.listFiles();
                if(files != null) {
                    List<String> fileNameList = new ArrayList<>();
                    for(File fileN: dirN.listFiles()) {
                        if(fileN.isFile())
                            fileNameList.add(fileN.getName());
                    }
                    caseMap.put(dirN.getName(), fileNameList);
                }
            }
        }
    }

    public static List<String> getFileName(String path){
        File file = new File(path);
        List<String>NameList = new ArrayList<>();
        for (File file1 : file.listFiles()) {
            if(file1.isFile()){
                NameList.add(file1.getName());
            }
        }
        return NameList;
    }

    //获取工程名字
    public static List<String> getProjectName(){
        File file = new File(".");
        List<String>projectNameList = new ArrayList<>();
        for (File file1 : file.listFiles()) {
            if(file1.isDirectory()&&file1.getName() .contains(Constant.TESTTYPE)){
                projectNameList.add(file1.getName());
               // System.out.println("file.listFiles()的值是：---"+ file1.getName() + "，当前方法=getProjectName.FileUtils()");
            }
        }
        return projectNameList;
    }

    public static String getString(ReadCfgUtils readCfgUtils, LogWriter logWriter, String FileName,JEditorPane recordTa, C4Socket pc2Phone, String pc2PhoneRev,
                             String content, String version, String errLevel,
                             String precision, String bg, String fg, String charSet,String ratio,String height,String sizeUnit,String  fixWid) throws IOException {
        String cmd = null ;
        if ("0".equals(readCfgUtils.getFlag())&& Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){
            cmd = GenCodeNew.displayQrCode(content, version, errLevel, precision, bg, fg, charSet, sizeUnit);
            pc2PhoneRev = pc2Phone.writeReadOneLine(cmd);
//            cmd = "content:"+content+"version:"+ version +"charSet:"+ charSet +"margin:"+"2"+"errLevel:"+errLevel+"precision:"+
//                    precision+"sizeUnit:" + sizeUnit + "bg:"+ bg +"fg:"+ fg +"degree:"+ "180";
//            pc2PhoneRev = GenCodeNew.displayQrCode(pc2Phone, content, version, charSet,"2", errLevel, precision,sizeUnit, bg, fg, "180");
//            logWriter.append("PC发送给Phone的请求:"+(GenerateCode.process(content, version, errLevel, precision, bg, fg, charSet))+",PC收到Phone的回应数据包:" + pc2PhoneRev);
//            UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+(GenerateCode.process(content, version, errLevel, precision, bg, fg, charSet))+",PC收到Phone的回应数据包:" + pc2PhoneRev+"\r\n",true);
          //  UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+cmd+",PC收到Phone的回应数据包:" + pc2PhoneRev, recordTa,ColorUtils.BLACK);

        }else if("0".equals(readCfgUtils.getFlag())&& Constant.CODE_128.equals(readCfgUtils.getCodeType())){
            cmd = GenCodeNew.displayCode128(content, fixWid, sizeUnit, precision, bg, fg, height);
            System.out.println(content+" "+ fixWid+" "+sizeUnit+" "+ precision+" "+bg +" "+fg+" "+ height );
            pc2PhoneRev = pc2Phone.writeReadOneLine(cmd);
            System.out.println(pc2PhoneRev);

//            cmd = "contend:"+content +"height:"+ height+ "bg:" + bg+"fg:"+ fg + "precision:" + precision +"sizeUnit:"+sizeUnit +"fixWid:"+fixWid;
//            pc2PhoneRev = GenCodeNew.displayCode128(pc2Phone, content, height, bg, fg,precision,sizeUnit,fixWid);

        }else if("0".equals(readCfgUtils.getFlag())&& Constant.CODE_39.equals(readCfgUtils.getCodeType())){
            cmd = GenCodeNew.displayCode39(content, fixWid, sizeUnit ,ratio, precision, bg, fg, height);
            pc2PhoneRev = pc2Phone.writeReadOneLine(cmd);

//            cmd = "contend:"+content+"ratio:"+ratio+ "degree:"+ "180"+ "height:" + height+ bg+"fg:"+ fg + "precision:" + precision+"sizeUnit:"+sizeUnit +"fixWid:"+fixWid;
//            pc2PhoneRev = GenCodeNew.displayCode39(pc2Phone, content, ratio, "180", height, bg, fg,precision,sizeUnit,fixWid);

        }else if("1".equals(readCfgUtils.getFlag())){
            cmd = Constant.SHOW + FileName;
            pc2PhoneRev = pc2Phone.writeReadOneLine(Constant.SHOW + FileName);
//            logWriter.append("PC发送给Phone的请求:"+(Constant.SHOW + FileName)+",PC收到Phone的回应数据包:" + pc2PhoneRev);
//            UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+(Constant.SHOW + FileName)+",PC收到Phone的回应数据包:" + pc2PhoneRev+"\r\n",true);
        }

        UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+cmd, recordTa,ColorUtils.BLACK);
        RecordLog.Logger().info("PC发送给Phone的请求:"+ cmd +",PC收到Phone的回应数据包:" + pc2PhoneRev);
        return pc2PhoneRev;
    }

    public static void main(String[] args)throws  Exception {
//        FileUtils.readCfg();
//        System.out.println("File.separator的值是：---"+ File.separator + "，当前方法=main.FileUtils()");
//        System.out.println(FileUtils.absolutePath(".").substring(0,FileUtils.absolutePath(".").length()-1));
//        System.out.println(FileUtils.readConfig(FileUtils.absolutePath(".").substring(0,FileUtils.absolutePath(".").length()-1)+File.separator+
//        "银联认证--受理终端条码识读能力测试-百富计算机技术（深圳）有限公司-S900-2017-12-06-16-45-04"+File.separator+ Constant.CONFIG));
//        String projectName = "银联认证--受理终端条码识读能力测试-百富计算机技术（深圳）有限公司-S900-2017-12-06-16-45-04";
//        String firstDir = "BCTC";
//        String Debug = "Debug";
//        String secondDir = "条码测试";
//        String str = ".\\银联认证--受理终端条码识读能力测试-百富计算机技术(深圳)有限公司-S900-2017-12-18-14-22-47\\";
//        System.out.println(calcOptimalDepth(str));
//        FileUtils.xmlConfig("." + File.separator + firstDir + File.separator + Debug + File.separator+secondDir);
        //ParseXml xml = new ParseXml("456.xml");
       // Document document = ParseXml.createDocument(projectName, firstDir, secondDir, typeList, caseMap);
       // System.out.println("caseMap的值是：---"+ caseMap + "，当前方法=main.FileUtils()");
       // System.out.println("typeList的值是：---"+ typeList + "，当前方法=main.FileUtils()");
        //ParseXml.writeXml(document, new File("456.xml"));

//        String str = ".\\银联认证--受理终端条码识读能力测试-百富计算机技术（深圳)有限公司-S900-2017-12-16-16-00-02\\CaseList.xml";
//        ParseXml xml = new ParseXml(str);
//        xml.addResultLog("ZCCS002.txt", "PASS", "ZCCS002-10-01-12-14-56-45.txt");
//        xml.addResultLog("ZCCS002.txt", "Fail", "ZCCS002-10-01-12-14-56-45.txt");
//        xml.addResultLog("ZCCS002.txt", "PASS", "ZCCS002-10-01-12-14-56-45.txt");
//        xml.addResultLog("ZCCS002.txt", "PASS", "ZCCS002-10-01-12-14-56-45.txt");
//        xml.writeXml();
        getProjectName();
    }
}
    