package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.Code128Cfg;
import cn.com.pax.entity.Code39Cfg;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.RecordLog;
import cn.com.pax.utils.ColorUtils;
import cn.com.pax.utils.ReadCfgUtils;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 识别正确率压力测试
 *
 * @author luohl
 * @create 2017-12-21-9:13
 */
public class ReadAccuracyStress extends BaseEngine implements BaseMoveWork {
    private List<QrCfgInfo> qrList;
    private List<Code39Cfg> c39List;
    private List<Code128Cfg> c128List;
    /*
             1、机械臂移动到最优景深的位置
             2、发送指令通知手机显示随机的条码
             3、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
             4、记录条码测试结果
             5、继续循环到第2步，保证不停变换条码，共测试10000次
             6、测试结束统计正确率
             */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
        if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){
            qrList = readCfgUtils.getQrCfgInfoList();
        }else if (Constant.CODE_39.equals(readCfgUtils.getCodeType())){
            c39List = readCfgUtils.getC39CfgInfoList();
        }else if (Constant.CODE_128.equals(readCfgUtils.getCodeType())){
            c128List = readCfgUtils.getC128CfgInfoList();
        }
        boolean flag = false;
//        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
//        LogWriter logWriter = new LogWriter(path + "\\trace\\" + caseTxt.split(".txt")[0]);
//        MyButtonClick.getInstance().setLogWriter(logWriter);
//        C4Socket pc2C4 = SocketOperation.getPc2C4();
//        C4Socket pc2Phone = SocketOperation.getPc2Phone();
//        SerialConnection serialConnection = SerialOperation.getInstance().getConnection();
        RecordLog.Logger().info("【成功率测试】");
        if (pc2C4 != null && pc2Phone != null && serialConnection != null) {

            String sendC4Str;
            String revPos;
            String pc2PhoneRev = null;
            String pc2C4Rev;
            int length = 0;
            int type  = 0;
            String xx = null;
            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else{
                try {
                    serialConnection.readExtraData(Constant.READEXTRATIME,Constant.MAXTIME);

                    List<Integer>succList = new ArrayList<>();
                    succList.clear();
                    if (qrList!= null){
                        length = qrList.size();
                        type = 1;
                    }else if(c39List!= null){
                        length = c39List.size();
                        type = 2;
                    }else if(c128List!= null){
                        length = c128List.size();
                        type = 3;
                    }
                    for (int i = 0; i < length; i++) {
                        try {

//                        logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date()) + "Time is:" + TimeUtils.getTimeFormat(new Date()));
//                        logWriter.append(caseTxt.split(".txt")[0]);
//                        logWriter.append("特征:C4");
//                        logWriter.append("数据:" + qrCfgInfo.getGenerateData());
//                        logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(qrCfgInfo.getGenerateData()));//可要不可要
//                        logWriter.append("码制:"+readCfgUtils.getCodeType());
//                        logWriter.append("版本:"+qrCfgInfo.getVersion());
//                        logWriter.append("容错级别:"+qrCfgInfo.getErrLevel());
//                        logWriter.append("模式:");
//                        logWriter.append("屏幕亮度:");
//                        logWriter.append("发送条码显示命令");
//
//                        UIUtils.updateTxA(recordTa, "[==============    Test Start    ==============]" + "\r\n");
//                        UIUtils.updateTxA(recordTa, "Date is:" + TimeUtils.getDateFormat(new Date()) + "," + "Time is:" + TimeUtils.getTimeFormat(new Date()) + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, caseTxt.split(".txt")[0] + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "特征:C4" + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "数据:" + qrCfgInfo.getGenerateData() + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(qrCfgInfo.getGenerateData()) + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "码制:" + readCfgUtils.getCodeType()+ "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "版本:" +qrCfgInfo.getVersion()+ "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "容错级别:"+qrCfgInfo.getErrLevel() + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "模式:" + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "屏幕亮度:" + "\r\n", true);
//                        UIUtils.updateTxA(recordTa, "发送显示条码的命令" + "\r\n", true);

//                        UIUtils.updateTxA(recordTa,"[==============    Test Start    ==============]"+"\r\n");
//                        UIUtils.logColorUpdate(logWriter, "Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()),recordTa, ColorUtils.BLUE);
//                        UIUtils.logColorUpdate(logWriter, caseTxt.split(".txt")[0], recordTa,ColorUtils.BLUE);
//                        UIUtils.logColorUpdate(logWriter, "特征:C4", recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "数据:" + qrCfgInfo.getGenerateData(), recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(qrCfgInfo.getGenerateData()), recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "码制:"+ readCfgUtils.getCodeType(), recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "版本:"+qrCfgInfo.getVersion(), recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "容错级别:" +qrCfgInfo.getErrLevel(), recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "模式:", recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "屏幕亮度:", recordTa,ColorUtils.WHITE);
//                        UIUtils.logColorUpdate(logWriter, "发送显示条码的命令", recordTa,ColorUtils.BLACK);

                            if (!flag)
                            {
//                            logWriter.append("回原点:0mm");
//                            UIUtils.updateTxA(recordTa, "回原点:0mm\r\n", true);
                                UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

                                //pc2PhoneRev = pc2Phone.writeReadOneLine(GenerateCode.process(qrCfgInfo.getGenerateData(), qrCfgInfo.getVersion(),qrCfgInfo.getErrLevel(),qrCfgInfo.getPrecision(),
                                // qrCfgInfo.getBackground(),qrCfgInfo.getForeground(),qrCfgInfo.getCharSet()));

                                pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                                if(pc2C4Rev.contains("ok")) {
//                                    logWriter.append("机械臂原点就位");
//                                    UIUtils.updateTxA(recordTa, "机械臂原点就位\r\n", true);

//                                logWriter.append("机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm");
//                                UIUtils.updateTxA(recordTa, "机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm\r\n", true);

                                    UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);
                                    UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm", recordTa,ColorUtils.BLUE);

                                    sendC4Str = Constant.BARCODECMD + MainFrame.jingShenJtf.getText().trim() + " 0";
                                    pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                                    if (!pc2C4Rev.contains("ok")) {
                                        RecordLog.Logger().error("PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev);
//
                                        UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" +
                                                pc2C4Rev+",C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);

                                        return 2;
                                    }
                                    flag = true;
                                }else {
                                    RecordLog.Logger().error("PC发送给C4的命令:" + Constant.ORIGIN + "," + "PC接受C4错误返回码:" + pc2C4Rev);
//                                logWriter.append("PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+ ",C4机械臂原点就位失败");
//                                UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                        ",C4机械臂原点就位失败"+"\r\n",true);
                                    UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                            ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                                    return 2;
                                }
                            }
                            UIUtils.logColorUpdate(logWriter, "机械臂移动到:" + MainFrame.jingShenJtf.getText() + "mm"+"就位", recordTa,ColorUtils.BLUE);
//                            if (content == null ){
//                                RecordLog.Logger().info("配置文件的数据有误，请核对后在测试！");
//                                return 5;
//                            }
                            if (type == 1) {
                                QrCfgInfo qrCfgInfo = qrList.get(i);
                                xx = qrCfgInfo.getGenerateData();
                                content = "[[" + qrCfgInfo.getCharSet() + "]]" + qrCfgInfo.getGenerateData();
                                version = qrCfgInfo.getVersion();
                                precision = qrCfgInfo.getPrecision();
                                bg = qrCfgInfo.getBackground();
                                fg = qrCfgInfo.getForeground();
                                charSet = qrCfgInfo.getCharSet();
                                errLevel = qrCfgInfo.getErrLevel();
                                sizeUit = "mil";
                            }else if(type == 2){
                                Code39Cfg code39Cfg = c39List.get(i);
                                content = code39Cfg.getData();
                                xx = code39Cfg.getData();
//                                if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
//                                    content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
//                                }
                                precision = code39Cfg.getPrecision();
                                ratio = code39Cfg.getRatio();
                                bg = code39Cfg.getBgcolor();
                                fg = code39Cfg.getFgcolor();
                                height = code39Cfg.getHeight();
                                sizeUit = code39Cfg.getUnitLength();
                                fixationWidth = code39Cfg.getFixationWidth();
                                bright = code39Cfg.getBrightValue();
                            }else if(type == 3){
                                Code128Cfg code128Cfg = c128List.get(i);
                                xx = code128Cfg.getData();
                                content = code128Cfg.getData();
//                                if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
//                                    content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
//                                }
                                precision = code128Cfg.getPrecision();
                                bg = code128Cfg.getBgcolor();
                                fg = code128Cfg.getFgcolor();
                                height = code128Cfg.getHeight();
                                sizeUit = code128Cfg.getUnitLength();
                                fixationWidth = code128Cfg.getFixationWidth();
                                bright = code128Cfg.getBrightValue();
                            }
                            pc2PhoneRev = cn.com.pax.utils.FileUtils.getString(readCfgUtils, logWriter ,FileName ,recordTa, pc2Phone, pc2PhoneRev, content,  version,  errLevel,
                                    precision,  bg,  fg,  charSet, ratio, height,sizeUit,fixationWidth);

                            if (pc2PhoneRev.contains("ok")){
                                long st = System .currentTimeMillis();
                                UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                                serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                                revPos = serialConnection.serialRead(Constant.TIMEOUT);
                                long ed = System.currentTimeMillis();
                                UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                                UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                int ret =UIUtils.recordLog(logWriter,revPos,xx,recordTa);
                                if (ret == 0 ){
//                                UIUtils.logUpdate(logWriter,  "第"+(i+1)+"次扫描成功",  recordTa);
                                    UIUtils.logColorUpdate(logWriter, "第"+(i+1)+"次扫描成功", recordTa,ColorUtils.BLUE);
                                }else{
//                                UIUtils.logUpdate( logWriter,  "第"+(i+1)+"次扫描失败",  recordTa);
                                    UIUtils.logColorUpdate(logWriter, "第"+(i+1)+"次扫描失败", recordTa,ColorUtils.RED);
                                    succList.add(i);
                                }
                            }else{
                                RecordLog.Logger().error("PC发送给Phone的请求:" + xx + "," + "PC收到Phone的【错误】数据包:" + pc2PhoneRev+",显示条码失败");
//                            logWriter.append("显示条码失败");
//                            UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+qrCfgInfo.getGenerateData()+",PC收到Phone的【错误】数据包:"+ pc2PhoneRev+
//                                    ",显示条码失败"+"\r\n",true);
                                UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+xx+",PC收到Phone的【错误】数据包:"+ pc2PhoneRev+
                                        ",显示条码失败", recordTa,ColorUtils.RED);
                                return 1;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
//                            if (i < qrList.size()-1) //最后一次在函数返回之后在清
//                                 GenCodeNew.clearQrCode(pc2Phone);
                        }

                    }
                    if (succList.size()>0){
                        UIUtils.testResult(logWriter,recordTa,1);
                        return 3;
                    }else{
                        UIUtils.testResult(logWriter,recordTa,0);
                        return 0;
                    }
                } catch (Exception e) {
                    RecordLog.Logger().error("抛出的异常："+ e.getMessage());
                    return 4;
                }
            }
        }else {
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}
    