package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.DebugInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.RecordLog;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.thread.ScanCode;
import cn.com.pax.utils.*;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调试速度
 *
 * @author luohl
 * @create 2017-12-20-14:46
 */
public class DebugRate extends  BaseEngine implements BaseMoveWork {
    /*
     1、回到标定位置（backp0）
     2、发送指令通知手机显示条码
     3、初始化移动到最优景深位置，并向左移动125mm
     4、启动线程通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
     5、开始向右以指定速度移动到相应位置
     6、移动结束后，等待读取条码结束，记录测试结果
     7、继续启动线程通过串口发送指令通知POS扫描条码，并设置10秒超时读取串口
     8、开始向左以指定速度移动到相应位置
     9、移动结束后，等待读取条码结束，记录测试结果
     10、综合两次结果，两次通过才算，这是一次的测试结果，如果多次，继续循环到第四步
     */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
//        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
//        LogWriter logWriter = new LogWriter(path + "\\trace\\" + caseTxt.split(".txt")[0]);
//        MyButtonClick.getInstance().setLogWriter(logWriter);
//        C4Socket pc2C4 = SocketOperation.getPc2C4();
//        C4Socket pc2Phone = SocketOperation.getPc2Phone();
//        SerialConnection serialConnection = SerialOperation.getInstance().getConnection();
        DebugInfo debugInfo = (DebugInfo)readCfgUtils.getDebug();
        if (debugInfo.getMoveSpeed() == null || debugInfo.getMoveNum() == null){
            return 6;
        }
        RecordLog.Logger().info("【调试运动容错】");
        if (pc2C4 != null && pc2Phone != null && serialConnection != null) {
            String sendC4Str;
            String revPos;
            String pc2PhoneRev = null ;
            String pc2C4Rev;
            List<String> strings;
            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else{
                try {

//                    logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date()) + "Time is:" + TimeUtils.getTimeFormat(new Date()));
//                    logWriter.append(caseTxt.split(".txt")[0]);
//                    logWriter.append("特征:C4");
//                    logWriter.append("数据:" + content);
//                    logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(content));//可要不可要
//                    logWriter.append("码制:"+readCfgUtils.getCodeType());
//                    logWriter.append("版本:"+(version ==null? "":version));
//                    logWriter.append("容错级别:" +(errLevel == null ? "" : errLevel));
//                    logWriter.append("模式:");
//                    logWriter.append("屏幕亮度:");
//                    logWriter.append("发送条码显示命令");
//
//                    UIUtils.updateTxA(recordTa, "[==============    Test Start    ==============]" + "\r\n");
//                    UIUtils.updateTxA(recordTa, "Date is:" + TimeUtils.getDateFormat(new Date()) + "," + "Time is:" + TimeUtils.getTimeFormat(new Date()) + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, caseTxt.split(".txt")[0] + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, "特征:C4" + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, "数据:" + content + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(content) + "\r\n", true);
//                    UIUtils.updateTxA(recordTa,"码制:"+readCfgUtils.getCodeType()+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"版本:" +(version ==null? "":version)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"容错级别:"+(errLevel == null ? "" : errLevel)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa, "模式:" + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, "屏幕亮度:" + "\r\n", true);
//                    UIUtils.updateTxA(recordTa, "发送显示条码的命令" + "\r\n", true);

//                    UIUtils.updateTxA(recordTa,"[==============    Test Start    ==============]"+"\r\n");
//                    UIUtils.logColorUpdate(logWriter, "Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()),recordTa, ColorUtils.BLUE);
//                    UIUtils.logColorUpdate(logWriter, caseTxt.split(".txt")[0], recordTa,ColorUtils.BLUE);
//                    UIUtils.logColorUpdate(logWriter, "特征:C4", recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "数据:" + content, recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(content), recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "码制:"+ readCfgUtils.getCodeType(), recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "版本:"+(version ==null? "":version), recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "容错级别:" +(errLevel == null ? "" : errLevel), recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "模式:", recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "屏幕亮度:", recordTa,ColorUtils.WHITE);
//                    UIUtils.logColorUpdate(logWriter, "发送显示条码的命令", recordTa,ColorUtils.BLACK);

                    UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

//                    logWriter.append("回原点:0mm");
//                    UIUtils.updateTxA(recordTa, "回原点:0mm\r\n", true);
                    pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                    if (pc2C4Rev .contains("ok")){
//                        logWriter.append("机械臂原点就位");
//                        UIUtils.updateTxA(recordTa,"机械臂原点就位\r\n",true);
                        UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);

                        serialConnection.readExtraData(Constant.READEXTRATIME,Constant.MAXTIME);
                        if (content == null ){
                            RecordLog.Logger().info("配置文件的数据有误，请核对后在测试！");
                            return 5;
                        }
                        //发送图片
                        pc2PhoneRev = cn.com.pax.utils.FileUtils.getString(readCfgUtils, logWriter ,FileName ,recordTa, pc2Phone, pc2PhoneRev, content,  version,  errLevel,
                                precision, bg, fg, charSet, ratio, height,sizeUit,fixationWidth);
                        if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){//针对qr码做处理
                            content = ReadExcelCfgUtils.parseContend(content);
                        }
                        if (pc2PhoneRev.contains("ok")){
//                            logWriter.append("APP 端数据准备完毕，准备生成条码");
//                            UIUtils.updateTxA(recordTa,"APP 端数据准备完毕，准备生成条码\r\n",true);
                            UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa,ColorUtils.BLACK);

                            UIUtils.logColorUpdate(logWriter, "左移12.5cm", recordTa, ColorUtils.BLUE);

                            UIUtils.logColorUpdate(logWriter, "已向机械臂发送指令，等待响应", recordTa, ColorUtils.BLUE);
//                            UIUtils.logUpdate(logWriter, "左移12.5cm", recordTa);
//                            UIUtils.logUpdate(logWriter, "已向机械臂发送指令，等待响应", recordTa);
                            sendC4Str = Constant.INITRATE + " " + MainFrame.jingShenJtf.getText();
                            pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                            if (pc2C4Rev.contains("ok")) {
//                                UIUtils.logUpdate(logWriter, "机械臂偏左12.5cm就位", recordTa);
                                UIUtils.logColorUpdate(logWriter, "机械臂偏左12.5cm就位", recordTa, ColorUtils.BLUE);
                                List<String>succList = new ArrayList<>();
                                List<String>succTolList = new ArrayList<>();
                                succTolList.clear();
                                for (int i = 1; i <= Integer.parseInt(debugInfo.getMoveNum().trim()); i++) {
                                    succList.clear();
                                    for (int j = 1; j <= 2; j++) {
                                        if (j == 1){
//                                            UIUtils.logUpdate(logWriter, "机械臂 将右移25cm", recordTa);
                                            UIUtils.logColorUpdate(logWriter, "机械臂 将右移25cm", recordTa,ColorUtils.BLUE);
                                            sendC4Str = Constant.RATECMD + " " + debugInfo.getMoveSpeed().trim() + " -1"; //右
                                        }else {
                                            UIUtils.logColorUpdate(logWriter, "机械臂 将左移25cm", recordTa,ColorUtils.BLUE);
//                                            UIUtils.logUpdate(logWriter, "机械臂 将左移25cm", recordTa);
                                            sendC4Str = Constant.RATECMD + " " + debugInfo.getMoveSpeed().trim() + " 1";//左
                                        }
                                        UIUtils.logColorUpdate(logWriter, "已向机械臂发送指令，机械臂开始移动，速度为" + debugInfo.getMoveSpeed().trim() + "mm/s,向终端发送扫码指令", recordTa,ColorUtils.BLUE);
//                                        UIUtils.logUpdate(logWriter, "已向机械臂发送指令，机械臂开始移动，速度为" + debugInfo.getMoveSpeed().trim() + "mm/s,向终端发送扫码指令", recordTa);
//                                        UIUtils.logUpdate(logWriter, "开始扫码", recordTa);
                                        UIUtils.logColorUpdate(logWriter, "开始扫码", recordTa, ColorUtils.BLACK);
                                        ScanCode readCode = new ScanCode(serialConnection);
                                        new Thread(readCode).start();
                                        Thread.sleep(2000);
                                        long st = System.currentTimeMillis();
                                        UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);

                                        pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                                        if (pc2C4Rev.contains("ok")){
                                            if (j == 1) {
//                                                UIUtils.logUpdate(logWriter, "机械臂 右移25cm成功", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "机械臂 右移25cm成功", recordTa,ColorUtils.BLACK);
                                            } else {
//                                                UIUtils.logUpdate(logWriter, "机械臂 左移25cm成功", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "机械臂 左移25cm成功", recordTa,ColorUtils.BLACK);
                                            }
                                            while (true) {
                                                if (readCode.getReadContent() == null || readCode.getReadContent().length() > 0) {
                                                    break;
                                                } else {
                                                    Thread.sleep(50);
                                                   // System.out.println("1111111111242433333333333333");
                                                }
                                            }
                                            revPos = readCode.getReadContent();
                                            long ed = System.currentTimeMillis();
                                            UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);

//                                            UIUtils.logUpdate(logWriter, "取回终端扫码返回", recordTa);
//                                            UIUtils.logUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos, recordTa);
                                            UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                            UIUtils.logColorUpdate(logWriter, "取回终端扫码返回", recordTa,ColorUtils.BLACK);
                                            UIUtils.logColorUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos, recordTa,ColorUtils.BLACK);

                                            RecordLog.Logger().info("原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos);
                                            if (revPos == null) {
//                                                UIUtils.logUpdate(logWriter, "线程读取串口数据异常", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "线程读取串口数据异常", recordTa,ColorUtils.RED);
                                                RecordLog.Logger().error("线程读取串口数据异常");
                                                return 5;
                                            } else if ("ffff".equalsIgnoreCase(revPos)) { //可以省略，为了详细区分，先加上
//                                                UIUtils.logUpdate(logWriter, "超时读取条码失败!", recordTa);
//                                                UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "超时读取条码失败!", recordTa,ColorUtils.RED);
                                                UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                            } else {
                                                if (content.equals(revPos)) {
//                                                  UIUtils.logUpdate(logWriter, "扫码验证通过!", recordTa);
//                                                   UIUtils.logUpdate(logWriter, "Pass", recordTa);
                                                    UIUtils.logColorUpdate(logWriter, "扫码验证通过", recordTa,ColorUtils.BLUE);
                                                    UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
                                                    succList.add(i+"+"+j);
                                                    succTolList.add(i+"+"+j);
                                                } else {
//                                                    UIUtils.logUpdate(logWriter, "扫码验证失败!", recordTa);
//                                                    UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                                    UIUtils.logColorUpdate(logWriter, "扫码验证失败", recordTa,ColorUtils.RED);
                                                    UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                                }
                                            }
                                        }else{
//                                            UIUtils.logUpdate(logWriter, "C4机械臂右移25cm失败", recordTa);
                                            UIUtils.logColorUpdate(logWriter, "C4机械臂右移25cm失败", recordTa,ColorUtils.RED);
                                            RecordLog.Logger().error("PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
                                            return 2;
                                        }
                                    }//end forj
                                    if (succList.size() == 2){
//                                        UIUtils.logUpdate(logWriter,    "恭喜，第"+i+"次"+ debugInfo.getMoveSpeed().trim() + "mm/s测试通过", recordTa);
                                        UIUtils.logColorUpdate(logWriter, "恭喜，第"+i+"次"+ debugInfo.getMoveSpeed().trim() + "mm/s测试通过", recordTa,ColorUtils.BLUE);
                                    }else {//算了不弄那么详细，不具体左，右不通过
//                                        UIUtils.logUpdate(logWriter,    "Sorry，"+"第"+i+"次"+debugInfo.getMoveSpeed().trim() + "mm/s测试不通过", recordTa);
                                        UIUtils.logColorUpdate(logWriter, "Sorry，"+"第"+i+"次"+debugInfo.getMoveSpeed().trim() + "mm/s测试不通过", recordTa,ColorUtils.RED);
                                    }
                                }//end fori
                                if (succTolList.size() == Integer.parseInt(debugInfo.getMoveNum().trim())*2){
                                    xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                    xml.writeXml();
                                }else{
                                    xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),debugInfo.getMoveSpeed().trim());
                                    xml.writeXml();
                                }
                                //pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                                if (succTolList.size() == Integer.parseInt(debugInfo.getMoveNum().trim())*2){
//                                    UIUtils.logUpdate(logWriter, "恭喜，"+Integer.parseInt(debugInfo.getMoveNum().trim())+"次"+debugInfo.getMoveSpeed().trim()+"mm/s测试通过！", recordTa);
                                    UIUtils.logColorUpdate(logWriter, "恭喜，"+Integer.parseInt(debugInfo.getMoveNum().trim())+"次"+debugInfo.getMoveSpeed().trim()+"mm/s测试通过！", recordTa,ColorUtils.BLUE);
                                    return 0;
                                }else{
//                                    UIUtils.logUpdate(logWriter, "Sorry，"+(Integer.parseInt(debugInfo.getMoveNum().trim())-succTolList.size())+"次"+debugInfo.getMoveSpeed().trim()+"mm/s测试不通过！", recordTa);
                                    UIUtils.logColorUpdate(logWriter, "Sorry，"+(Integer.parseInt(debugInfo.getMoveNum().trim())-succTolList.size())+"次"+debugInfo.getMoveSpeed().trim()+"mm/s测试不通过！", recordTa,ColorUtils.RED);
                                    return 3;
                                }
                            }else{
                               // UIUtils.logUpdate(logWriter, "C4机械臂左移12.5cm失败", recordTa);
                                RecordLog.Logger().error("PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
                                UIUtils.logColorUpdate(logWriter, "PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev+",C4机械臂左移12.5cm失败", recordTa,ColorUtils.RED);
                                return 2;
                            }
                        }else {
                            RecordLog.Logger().error("PC发送给Phone的请求:" + Constant.SHOW+FileName + "," + "PC收到Phone的【错误】数据包:" + pc2PhoneRev+",显示条码失败");
//                            logWriter.append("显示条码失败");
//                            UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+Constant.SHOW+FileName+",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
//                                    ",显示条码失败"+"\r\n",true);
                            UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+ (FileName==null?"generate code":Constant.SHOW+FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                                    ",显示条码失败", recordTa,ColorUtils.RED);
                            return 1;
                        }
                    }else{
                        RecordLog.Logger().error("PC发送给C4的命令:" + Constant.ORIGIN + "," + "PC接受C4错误返回码:" + pc2C4Rev);
//                        logWriter.append("PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+ ",C4机械臂原点就位失败");
//                        UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                ",C4机械臂原点就位失败"+"\r\n",true);
                        UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                        return 2;
                    }

                }catch (Exception e){
                    RecordLog.Logger().error("抛出的异常："+ e.getMessage());
                    return 4;
                }
            }
        }else{
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}
    