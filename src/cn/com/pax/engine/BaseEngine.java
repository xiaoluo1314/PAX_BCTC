package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.Code128Cfg;
import cn.com.pax.entity.Code39Cfg;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.utils.*;

import javax.swing.*;
import java.io.File;
import java.util.Date;
import java.util.Map;

/**
 * 基类公共部分  还可以提取公共部分，先不提取了
 *
 * @author luohl
 * @create 2017-12-26-9:13
 */
public class BaseEngine {

    protected LogWriter logWriter ;
    protected String FileName = null;
    protected String content = null;//共用
    protected String version = null;
    protected String errLevel = null;
    protected String precision = null;
    protected String ratio = null;

    protected String sizeUit = null;
    protected String fixationWidth = null;
    protected String height = null;
    protected String charSet = null;
    protected String bg = null;
    protected String fg = null;
    protected String bright = null;

    protected C4Socket pc2C4;
    protected C4Socket pc2Phone;
    protected SerialConnection serialConnection;
    protected ParseXml xml;
    protected Map<String, String> excelCfgMap;

    public void readCFG(ReadCfgUtils readCfgUtils,String caseTxt, JEditorPane recordTa) {
        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
        xml = new ParseXml(path + File.separator+Constant.CASEXML);
        //String path = MainFrame.getPath().substring(0,MainFrame.getPath().lastIndexOf("\\")-1);
        logWriter = new LogWriter(path+"\\trace\\"+caseTxt.split(".txt")[0]);
        MyButtonClick.getInstance().setLogWriter(logWriter);
        pc2C4 = SocketOperation.getPc2C4();
        pc2Phone = SocketOperation.getPc2Phone();
        serialConnection = SerialOperation.getInstance().getConnection();

        ReadExcelCfgUtils readExcelCfg = new ReadExcelCfgUtils();
        boolean excelFlag = readExcelCfg.readExcel();
        excelCfgMap = readExcelCfg.getExcelCfg();
        // if ()// 不想判断了
        if("1".equals(readCfgUtils.getFlag())){
            ImageInfo imageInfo = (ImageInfo)readCfgUtils.getClassType();
            FileName =  imageInfo.getImgName();
            content = imageInfo.getContent();
            if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
                content =  content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
            }
            bright ="value";
        }else if("0".equals(readCfgUtils.getFlag())) {
            if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())) {
                QrCfgInfo qrCfgInfo = (QrCfgInfo) readCfgUtils.getClassType();
                content = qrCfgInfo.getGenerateData();
                if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
                    if (content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")).contains("M"))
                        content = ReadExcelCfgUtils .getTypeContend(excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]"))));
                    else {
                        content = "[[" + qrCfgInfo.getCharSet() + "]]" + excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
                    }
                }else{
                    content = "[[" + qrCfgInfo.getCharSet() + "]]" + qrCfgInfo.getGenerateData();
                }
                version = qrCfgInfo.getVersion();
                errLevel = qrCfgInfo.getErrLevel();
                precision = qrCfgInfo.getPrecision();
                charSet = qrCfgInfo.getCharSet();
                bg = qrCfgInfo.getBackground();
                fg = qrCfgInfo.getForeground();
                sizeUit = qrCfgInfo.getUnitLength();
                bright = qrCfgInfo.getBrightValue();
            }else if(Constant.CODE_128.equals(readCfgUtils.getCodeType())){
                Code128Cfg code128Cfg = (Code128Cfg) readCfgUtils.getClassType();
                content = code128Cfg.getData();
                if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
                    content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
                }
                precision = code128Cfg.getPrecision();
                bg = code128Cfg.getBgcolor();
                fg = code128Cfg.getFgcolor();
                height = code128Cfg.getHeight();
                sizeUit = code128Cfg.getUnitLength();
                fixationWidth = code128Cfg.getFixationWidth();
                bright = code128Cfg.getBrightValue();
            }else if(Constant.CODE_39.equals(readCfgUtils.getCodeType())){
                Code39Cfg code39Cfg = (Code39Cfg) readCfgUtils.getClassType();
                content = code39Cfg.getData();
                if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
                    content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
                }
                precision = code39Cfg.getPrecision();
                ratio = code39Cfg.getRatio();
                bg = code39Cfg.getBgcolor();
                fg = code39Cfg.getFgcolor();
                height = code39Cfg.getHeight();
                sizeUit = code39Cfg.getUnitLength();
                fixationWidth = code39Cfg.getFixationWidth();
                bright = code39Cfg.getBrightValue();
            }
            //预留其他
        }
        {
            try {
                UIUtils.startEnd("[==============    Test Start    ==============]",recordTa);
                //UIUtils.updateJtP(recordTa,"<p style=\"color:000000\">[==============    Test Start    ==============]</p>");
                UIUtils.logColorUpdate(logWriter, "Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()),recordTa, ColorUtils.BLUE);
                UIUtils.logColorUpdate(logWriter, caseTxt.split(".txt")[0], recordTa,ColorUtils.BLUE);
                UIUtils.logColorUpdate(logWriter, "特征:C4", recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "数据:" + content, recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(content), recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "码制:"+ readCfgUtils.getCodeType(), recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "版本:"+(version == null? "": version), recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "容错级别:" +(errLevel == null ? "" : errLevel), recordTa,ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "模式:"+(charSet == null ? "" :charSet), recordTa, ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "屏幕亮度:", recordTa, ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "窄宽比:" + (ratio == null ? "" : ratio) , recordTa, ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "高度:" + (height == null ? "" : height), recordTa, ColorUtils.WHITE);
                UIUtils.logColorUpdate(logWriter, "发送显示条码的命令", recordTa,ColorUtils.BLACK);

            }catch (Exception e){
                e.printStackTrace();
            }
        }





    }
}
    