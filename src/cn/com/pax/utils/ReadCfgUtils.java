package cn.com.pax.utils;

import cn.com.pax.Constant;
import cn.com.pax.entity.*;
import cn.com.pax.tree.TreeOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 读取各种码制类型的配置
 *
 * @author luohl
 * @create 2017-12-23-14:45
 */
public class ReadCfgUtils {
    private String testEngine;      //景深测试 -------- 第一行   业务流程
    private String flag;            //ScanTest:0   //--------  第二行 1代表图片，0代码自动生成
    private Map<String,String>cfgInfoMap;
    private String codeType;        // 码制类型:QR Code   // 第五行 码制类型
    private Object classType;
    private String caseText;
    private Object debug;

    private Object paper;

    public Object getPaper() {
        return paper;
    }

    private List<QrCfgInfo>qrCfgInfoList;
    private List<Code39Cfg>c39CfgInfoList;
    private List<Code128Cfg>c128CfgInfoList;
    public Object getDebug() {
        return debug;
    }

    public ReadCfgUtils(String caseText){
        this.caseText = caseText;
        cfgInfoMap = new HashMap<>();
        qrCfgInfoList = new LinkedList<>();
        c39CfgInfoList = new LinkedList<>();
        c128CfgInfoList = new LinkedList<>();
    }

    public List<Code39Cfg> getC39CfgInfoList() {
        return c39CfgInfoList;
    }

    public List<Code128Cfg> getC128CfgInfoList() {
        return c128CfgInfoList;
    }

    public List<QrCfgInfo> getQrCfgInfoList() {
        return qrCfgInfoList;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getTestEngine() {
        return testEngine;
    }

    public String getFlag() {
        return flag;
    }

    public Map<String, String> getCfgInfoMap() {
        return cfgInfoMap;
    }

    public Object getClassType() {
        return classType;
    }

    public boolean readCfg() throws IOException {
        //简单考虑，没有想得更细(如出现同一样的名字。。。。)，名字有待商榷
        Map<String,String>dirPathMap = TreeOperation.dirPath;
//        String path ;
//        if (caseText.contains("TS")){
//            path = Constant.SCRIPDEBUGTPATH ;
//        }else{
//            path = Constant.SCRIPTPATH ;
//        }
//        List<String> cfgList = FileUtils.readLines(new File(path + caseText.split(".txt")[0].split("[^a-zA-Z]+?")[0].trim() + File.separator + caseText),
//                "utf-8");
        List<String> cfgList = FileUtils.readLines(new File(dirPathMap.get(caseText)),"utf-8");
        testEngine = cfgList.get(0).trim();
        //flag = cfgList.get(1).split(":")[1].trim();
        for (int i = 1; i < cfgList.size(); i++) {
            String []strStr= cfgList.get(i).split(":",2);
            if (strStr.length == 2 && StringUtils.isNotBlank(strStr[1]))
                cfgInfoMap.put(strStr[0].trim(),strStr[1].trim());
        }
        flag = cfgInfoMap.get(Constant.SCANTEST);
        if ("1".equals(flag)){
            ImageInfo imageInfo = new ImageInfo();
            if (cfgInfoMap.containsKey(Constant.IMG) && cfgInfoMap.containsKey(Constant.IMGCONTEND)){//&&cfgInfoMap.containsKey(Constant.CODETYPE)
                if (StringUtils.isNotBlank(cfgInfoMap.get(Constant.IMG)) && StringUtils.isNotBlank(cfgInfoMap.get(Constant.IMGCONTEND))){//&&StringUtils.isNotBlank(cfgInfoMap.get(Constant.CODETYPE))
                    imageInfo.setImgName(cfgInfoMap.get(Constant.IMG));
                    imageInfo.setContent(cfgInfoMap.get(Constant.IMGCONTEND));
                    setCodeType(cfgInfoMap.get(Constant.CODETYPE));
                    classType = imageInfo;
                }else {
                    return false;
                }
            }else{
                return  false;
            }
        }else if("0".equals(flag)){
                if (cfgInfoMap.containsKey(Constant.CODETYPE) && StringUtils.isNotBlank(cfgInfoMap.get(Constant.CODETYPE))){
                    setCodeType(cfgInfoMap.get(Constant.CODETYPE));
                    if (cfgInfoMap.get(Constant.CODETYPE).equals(Constant.GENERTYPE)&& !caseText.contains("QR_CCCS")){
                        QrCfgInfo qrCfgInfo = new QrCfgInfo();
                        if (cfgInfoMap.containsKey(Constant.VERSION)&& cfgInfoMap.containsKey(Constant.ERRLEVEL)
                                && cfgInfoMap.containsKey(Constant.CODEDATA) && cfgInfoMap.containsKey(Constant.PRECISION)){
                            if (StringUtils.isNotBlank(cfgInfoMap.get(Constant.VERSION))&& StringUtils.isNotBlank(cfgInfoMap.get(Constant.ERRLEVEL))
                            && StringUtils.isNotBlank(cfgInfoMap.get(Constant.CODEDATA))&& StringUtils.isNotBlank(cfgInfoMap.get(Constant.PRECISION))){
                                qrCfgInfo.setVersion(cfgInfoMap.get(Constant.VERSION));
                                qrCfgInfo.setErrLevel(cfgInfoMap.get(Constant.ERRLEVEL));
                                String str = cfgInfoMap.get(Constant.PRECISION).split("[a-zA-Z]+")[0];
                                qrCfgInfo.setPrecision(str);
                                qrCfgInfo.setGenerateData(cfgInfoMap.get(Constant.CODEDATA));
                                //可选字段
                                setCharSet(qrCfgInfo, cfgInfoMap);
                                //qrCfgInfo.setCharSet(cfgInfoMap.get(Constant.CHARSET)==null ? "BYTE":cfgInfoMap.get(Constant.CHARSET).equals("数字")?"NUMERIC":"BYTE");
                                qrCfgInfo.setBackground(cfgInfoMap.get(Constant.BG)==null ? "ffffff":cfgInfoMap.get(Constant.BG));
                                qrCfgInfo.setForeground(cfgInfoMap.get(Constant.FG)==null ? "000000":cfgInfoMap.get(Constant.FG));
                                qrCfgInfo.setUnitLength(cfgInfoMap.get(Constant.PRECISION).split(str)[1]);
                                qrCfgInfo.setBrightValue(cfgInfoMap.get(Constant.BRIGHT)==null ? "value":cfgInfoMap.get(Constant.BRIGHT));
                                classType = qrCfgInfo;

                            }else{
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }else if(cfgInfoMap.get(Constant.CODETYPE).equals(Constant.CODE_128)&& !caseText.contains("Code128_CCCS")){
                        if (cfgInfoMap.containsKey(Constant.CODEDATA) && cfgInfoMap.containsKey(Constant.PRECISION)){
                            if(StringUtils.isNotBlank(cfgInfoMap.get(Constant.CODEDATA))&& StringUtils.isNotBlank(cfgInfoMap.get(Constant.PRECISION))){
                                Code128Cfg code128Cfg = new Code128Cfg();
                                code128Cfg.setData(cfgInfoMap.get(Constant.CODEDATA));
                                String str = cfgInfoMap.get(Constant.PRECISION).split("[a-zA-Z]+")[0];
                                code128Cfg.setPrecision(str);
                                code128Cfg.setBgcolor(cfgInfoMap.get(Constant.BG)==null? "ffffff":cfgInfoMap.get(Constant.BG));
                                code128Cfg.setFgcolor(cfgInfoMap.get(Constant.FG)==null? "000000":cfgInfoMap.get(Constant.FG));
                                code128Cfg.setHeight("16");
                                code128Cfg.setUnitLength(cfgInfoMap.get(Constant.PRECISION).split(str)[1]);
                                code128Cfg.setFixationWidth(cfgInfoMap.get(Constant.FIXATIONWIDTH)==null? "0":cfgInfoMap.get(Constant.FIXATIONWIDTH));
                                code128Cfg.setBrightValue(cfgInfoMap.get(Constant.BRIGHT)==null ? "value":cfgInfoMap.get(Constant.BRIGHT));
                                classType = code128Cfg;

                            }else{
                                return false;
                            }
                        }else{
                           return false;
                        }
                    }else if(cfgInfoMap.get(Constant.CODETYPE).equals(Constant.CODE_39)&& !caseText.contains("Code39_CCCS")) {
                        if (cfgInfoMap.containsKey(Constant.CODEDATA) && cfgInfoMap.containsKey(Constant.PRECISION) && cfgInfoMap.containsKey(Constant.RATIO)) {
                            if (StringUtils.isNotBlank(cfgInfoMap.get(Constant.CODEDATA)) && StringUtils.isNotBlank(cfgInfoMap.get(Constant.PRECISION))
                                    && StringUtils.isNotBlank(cfgInfoMap.get(Constant.RATIO))) {
                                Code39Cfg code39Cfg = new Code39Cfg();
                                code39Cfg.setData(cfgInfoMap.get(Constant.CODEDATA));
                                String str = cfgInfoMap.get(Constant.PRECISION).split("[a-zA-Z]+")[0];
                                code39Cfg.setPrecision(str);
                                code39Cfg.setRatio(cfgInfoMap.get(Constant.RATIO));
                                code39Cfg.setBgcolor(cfgInfoMap.get(Constant.BG) == null ? "ffffff" : cfgInfoMap.get(Constant.BG));
                                code39Cfg.setFgcolor(cfgInfoMap.get(Constant.FG) == null ? "000000" : cfgInfoMap.get(Constant.FG));
                                code39Cfg.setHeight("16");
                                code39Cfg.setUnitLength(cfgInfoMap.get(Constant.PRECISION).split(str)[1]);
                                code39Cfg.setFixationWidth(cfgInfoMap.get(Constant.FIXATIONWIDTH) == null ? "0" : cfgInfoMap.get(Constant.FIXATIONWIDTH));
                                code39Cfg.setBrightValue(cfgInfoMap.get(Constant.BRIGHT) == null ? "value" : cfgInfoMap.get(Constant.BRIGHT));
                                classType = code39Cfg;

                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
//                    }else if(cfgInfoMap.get(Constant.CODETYPE).equals(Constant.GENERTYPE)&&caseText.contains("CCCS")) { //自动生成，就弱检查了
//                        for (int i = 1; i <= (cfgInfoMap.size() - 1) / 7; i++) {
//                            QrCfgInfo qrCfgInfo = new QrCfgInfo();
//                            qrCfgInfo.setVersion(cfgInfoMap.get(Constant.VERSION + i));
//                            qrCfgInfo.setErrLevel(cfgInfoMap.get(Constant.ERRLEVEL + i));
//                            qrCfgInfo.setPrecision(cfgInfoMap.get(Constant.PRECISION + i));
//                            qrCfgInfo.setGenerateData(cfgInfoMap.get(Constant.CODEDATA + i));
//                            //可选字段
//                            setCharSet(qrCfgInfo, cfgInfoMap);
//                            //qrCfgInfo.setCharSet(cfgInfoMap.get(Constant.CHARSET+i)==null? "BYTE":cfgInfoMap.get(Constant.CHARSET+i).equals("数字")?"NUMERIC":"BYTE");
//                            qrCfgInfo.setBackground(cfgInfoMap.get(Constant.BG + i) == null ? "000000" : cfgInfoMap.get(Constant.BG + i));
//                            qrCfgInfo.setForeground(cfgInfoMap.get(Constant.FG + i) == null ? "ffffff" : cfgInfoMap.get(Constant.FG + i));
//                            qrCfgInfoList.add(qrCfgInfo);
//                            classType = qrCfgInfo;
//                        }
//                    }
                    //拓展后续的类型
                }else{
                    return false;
                }
        }
        if (caseText.contains("QR_CCCS")){ //自动生成，就弱检查了
            for (int i = 1; i <= (cfgInfoMap.size()-1)/7;i++) {
                QrCfgInfo qrCfgInfo = new QrCfgInfo();
                qrCfgInfo.setVersion(cfgInfoMap.get(Constant.VERSION +i));
                qrCfgInfo.setErrLevel(cfgInfoMap.get(Constant.ERRLEVEL+i));
                qrCfgInfo.setPrecision(cfgInfoMap.get(Constant.PRECISION+i));
                qrCfgInfo.setGenerateData(cfgInfoMap.get(Constant.CODEDATA+i));
                //可选字段
                setCharSet(qrCfgInfo, cfgInfoMap);
                //qrCfgInfo.setCharSet(cfgInfoMap.get(Constant.CHARSET+i)==null? "BYTE":cfgInfoMap.get(Constant.CHARSET+i).equals("数字")?"NUMERIC":"BYTE");
                qrCfgInfo.setBackground(cfgInfoMap.get(Constant.BG+i)==null? "000000":cfgInfoMap.get(Constant.BG+i));
                qrCfgInfo.setForeground(cfgInfoMap.get(Constant.FG+i)==null? "ffffff":cfgInfoMap.get(Constant.FG+i));
                classType = qrCfgInfo;
                qrCfgInfoList.add(qrCfgInfo);
            }
        }else if (caseText.contains("Code39_CCCS")){
            for (int i = 1; i <= (cfgInfoMap.size()-4)/2;i++){
                Code39Cfg code39Cfg = new Code39Cfg();
                code39Cfg.setBgcolor(cfgInfoMap.get(Constant.BG) == null ? "ffffff" : cfgInfoMap.get(Constant.BG));
                code39Cfg.setFgcolor(cfgInfoMap.get(Constant.FG) == null ? "000000" : cfgInfoMap.get(Constant.FG));

                code39Cfg.setData(cfgInfoMap.get(Constant.CODEDATA+i));
                String str = cfgInfoMap.get(Constant.PRECISION+i).split("[a-zA-Z]+")[0];
                code39Cfg.setPrecision(str);
                code39Cfg.setRatio("2");
                code39Cfg.setHeight("16");
                code39Cfg.setUnitLength(cfgInfoMap.get(Constant.PRECISION+i).split(str)[1]);
                code39Cfg.setFixationWidth(cfgInfoMap.get(Constant.FIXATIONWIDTH) == null ? "0" : cfgInfoMap.get(Constant.FIXATIONWIDTH));
                code39Cfg.setBrightValue(cfgInfoMap.get(Constant.BRIGHT) == null ? "value" : cfgInfoMap.get(Constant.BRIGHT));
                classType = code39Cfg;
                c39CfgInfoList.add(code39Cfg);
            }

        }else if(caseText.contains("Code128_CCCS")){
            for (int i = 1; i <= (cfgInfoMap.size()-4)/2;i++){
                Code128Cfg code128Cfg = new Code128Cfg();
                code128Cfg.setBgcolor(cfgInfoMap.get(Constant.BG)==null? "ffffff":cfgInfoMap.get(Constant.BG));
                code128Cfg.setFgcolor(cfgInfoMap.get(Constant.FG)==null? "000000":cfgInfoMap.get(Constant.FG));
                code128Cfg.setData(cfgInfoMap.get(Constant.CODEDATA+i));
                String str = cfgInfoMap.get(Constant.PRECISION+i).split("[a-zA-Z]+")[0];
                code128Cfg.setPrecision(str);
                code128Cfg.setHeight("16");
                code128Cfg.setUnitLength(cfgInfoMap.get(Constant.PRECISION+i).split(str)[1]);
                code128Cfg.setFixationWidth(cfgInfoMap.get(Constant.FIXATIONWIDTH)==null? "0":cfgInfoMap.get(Constant.FIXATIONWIDTH));
                code128Cfg.setBrightValue(cfgInfoMap.get(Constant.BRIGHT)==null ? "value":cfgInfoMap.get(Constant.BRIGHT));
                classType = code128Cfg;
                c128CfgInfoList.add(code128Cfg);
            }

        }

        if (caseText.contains("BZCS")){
            PaperInfo paperInfo = new PaperInfo();
            System.out.println(cfgInfoMap.get(Constant.IMGCONTEND));
            paperInfo.setContend(cfgInfoMap.get(Constant.IMGCONTEND));
            System.out.println("paperInfo"+paperInfo);
            paper = paperInfo;

        }
        //获取debug的配置，如果是debug,那么默认必须有，否则，程序不匹配，故不做判断，直接写
        DebugInfo debugInfo = new DebugInfo();
        debugInfo.setMoveNum(cfgInfoMap.get(Constant.NUM));
        debugInfo.setMoveSpeed(cfgInfoMap.get(Constant.SPEED));
        debugInfo.setRotateType(cfgInfoMap.get(Constant.ROTATETYPE));
        debugInfo.setRotateAngle(cfgInfoMap.get(Constant.ROTATEANGEL));
        debugInfo.setRotate360(cfgInfoMap.get(Constant.ROTATE_360));
        debug = debugInfo ;
        return  true;
    }

    private void setCharSet(QrCfgInfo qrCfgInfo, Map<String,String>cfgInfoMap){
        if (cfgInfoMap.get(Constant.CHARSET)!= null){
            qrCfgInfo.setCharSet(ReadExcelCfgUtils.strConvert(cfgInfoMap.get(Constant.CHARSET)));
        }else{
            qrCfgInfo.setCharSet("BYTE");
        }
    }
    public static void main(String[] args)throws IOException {
//        String str ="前景色:";
//        String [] aa = str.split(":");
//        System.out.println(str.split(":",2)[0]+" "+str.split(":",2)[1]);
//        //System.out.println(aa.length);
         Map<String,String>cfgMap = new LinkedHashMap<>();
//      List<QrCfgInfo>list = new ArrayList<>();
        List<String> cfgList = FileUtils.readLines(new File("BCTC\\Debug\\条码测试\\CCCS\\Code39_CCCS001.txt"),"utf-8");
        for (int i = 2; i < cfgList.size(); i++) {
           if (cfgList.get(i).split(":",2).length == 2 && StringUtils.isNotBlank(cfgList.get(i).split(":",2)[1]))
                cfgMap.put(cfgList.get(i).split(":")[0].trim(),cfgList.get(i).split(":")[1].trim());
        }
//        if (str.split(":",2).length == 2 && StringUtils.isNotBlank(str.split(":",2)[1]))
//             cfgMap.put(str.split(":")[0].trim(),str.split(":")[1].trim());
        System.out.println(cfgMap.size());
        System.out.println((cfgMap.size()-3)/2);
        //System.out.println((cfgList.size()-3)/7);
        //System.out.println((cfgMap.size()-1)/7);
       // System.out.println(cfgMap);

       // String xx = "JDCS001Arf45_1.txt";
        //String xx = "JDCS001_1ABC.txt";
        //xx = xx.split(".txt")[0].split("[^a-zA-Z]+?")[0].trim();
        //System.out.println(xx);
        String st ;//
        //st = "15mil";
        st = "15px";
//        System.out.println(st.split("[a-z]+")[0]);
//        System.out.println( st.split(st.split("\\d+")[1])[0]+"   "+st.split("\\d+")[1]);

    }
}
    