package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.display.MainFrame;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.RecordLog;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.thread.ScanCode;
import cn.com.pax.utils.*;

import javax.swing.*;
import java.util.Date;

public class HorizRateMove extends  BaseEngine implements BaseMoveWork {
    /**
     * 1、发送指令通知手机显示条码
     * 2、机械臂移动到最优景深的位置
     * 3、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
     * 4、记录读取结果
     * 5、通知机械臂向左水平移动12.5cm
     * 6、继续通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
     * 7、开始以指定速度（初始值为5cm/s)，向右移动机械臂，移动25cm.
     * 8、记录向右移动读取情况
     * 9、继续通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
     * 10、然后通知机械臂向右移动机械臂，水平移动25cm
     * 11、记录向左移动读取情况
     * 12、如果6和9读取通过，则指定速度测试通过，否则测试失败，流程结束。
     * 13、如果测试通过，继续以1cm/s速度增加，跳转第6步进行测试，保持速度测试增加到20cm/s。
     * 12、通过标准为：20cm/s;(成功率：100%)"
     */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
//        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
//        logWriter = new LogWriter(path + "\\trace\\" + caseTxt.split(".txt")[0]);
//        MyButtonClick.getInstance().setLogWriter(logWriter);
//        C4Socket pc2C4 = SocketOperation.getPc2C4();
//        C4Socket pc2Phone = SocketOperation.getPc2Phone();
//        SerialConnection serialConnection = SerialOperation.getInstance().getConnection();
        RecordLog.Logger().info("【运动容错】测试");
        if (pc2C4 != null && pc2Phone != null && serialConnection != null) {
            try {
                String sendC4Str;
                String revPos;
                String pc2PhoneRev = null;
                String pc2C4Rev;

                //到时候根据自动生成条码再封装成函数

//                UIUtils.updateTxA(recordTa, "[==============    Test Start    ==============]" + "\r\n");
//                UIUtils.logColorUpdate(logWriter, "Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()),recordTa,ColorUtils.BLUE);
//                UIUtils.logColorUpdate(logWriter, caseTxt.split(".txt")[0], recordTa,ColorUtils.BLUE);
//                UIUtils.logColorUpdate(logWriter, "特征:C4", recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "数据:" + content, recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(content), recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "码制:"+ readCfgUtils.getCodeType(), recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "版本:"+(version ==null? "":version), recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "容错级别:" +(errLevel == null ? "" : errLevel), recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "模式:", recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "屏幕亮度:", recordTa,ColorUtils.WHITE);
//                UIUtils.logColorUpdate(logWriter, "发送显示条码的命令", recordTa,ColorUtils.BLACK);

                //logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()));
               // logWriter.append(caseTxt.split(".txt")[0]);
//                logWriter.append("特征:C4");
//                logWriter.append("数据:" + content);
//                logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(content));//可要不可要
//                logWriter.append("码制:"+readCfgUtils.getCodeType());
//                logWriter.append("版本:"+(version ==null? "":version));
//                logWriter.append("容错级别:" +(errLevel == null ? "" : errLevel));
//                logWriter.append("模式:");
//                logWriter.append("屏幕亮度:");
//                logWriter.append("发送显示条码的命令");

               // UIUtils.updateTxA(recordTa, "[==============    Test Start    ==============]" + "\r\n");
                // UIUtils.updateTxA(recordTa, "Date is:" + TimeUtils.getDateFormat(new Date()) + "," + "Time is:" + TimeUtils.getTimeFormat(new Date()) + "\r\n", true);
                //UIUtils.updateTxA(recordTa, caseTxt.split(".txt")[0] + "\r\n", true);
//                UIUtils.updateTxA(recordTa, "特征:C4" + "\r\n", true);
//                UIUtils.updateTxA(recordTa, "数据:" + content + "\r\n", true);
//                UIUtils.updateTxA(recordTa, "数据转为ASCII:" + AsciiConvertUtils.parseAscii(content) + "\r\n", true);
//                UIUtils.updateTxA(recordTa,"码制:"+readCfgUtils.getCodeType()+"\r\n",true);
//                UIUtils.updateTxA(recordTa,"版本:" +(version ==null? "":version)+"\r\n",true);
//                UIUtils.updateTxA(recordTa,"容错级别:"+(errLevel == null ? "" : errLevel)+"\r\n",true);
//                UIUtils.updateTxA(recordTa, "模式:" + "\r\n", true);
//                UIUtils.updateTxA(recordTa, "屏幕亮度:" + "\r\n", true);
//                UIUtils.updateTxA(recordTa, "发送显示条码的命令" + "\r\n", true);

                serialConnection.readExtraData(Constant.READEXTRATIME,Constant.MAXTIME);
                if (content == null ){
                    RecordLog.Logger().info("配置文件的数据有误，请核对后在测试！");
                    return 5;
                }
                pc2PhoneRev = cn.com.pax.utils.FileUtils.getString(readCfgUtils, logWriter ,FileName ,recordTa, pc2Phone, pc2PhoneRev, content,  version,  errLevel,
                        precision,  bg,  fg,  charSet, ratio, height,sizeUit,fixationWidth);
                if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){//针对qr码做处理
                    content = ReadExcelCfgUtils.parseContend(content);
                }
                if (pc2PhoneRev.contains("ok")) {
//                    logWriter.append("APP 端数据准备完毕，准备生成条码");
//                    UIUtils.updateTxA(recordTa, "APP 端数据准备完毕，准备生成条码" + "\r\n", true);
                    UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa,ColorUtils.BLACK);
                    UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

//                    logWriter.append("回原点:0mm");
//                    UIUtils.updateTxA(recordTa, "回原点:0mm" + "\r\n", true);

                    pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                    RecordLog.Logger().info("PC发给C4的请求:" + Constant.ORIGIN + " PC收到C4的回应数据包:" + pc2C4Rev);
                    if (pc2C4Rev.contains("ok")) {
//                        logWriter.append("机械臂原点就位");
//                        UIUtils.updateTxA(recordTa, "机械臂原点就位" + "\r\n", true);

                        UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);

//                        logWriter.append("机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm");
//                        UIUtils.updateTxA(recordTa, "机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm" + "\r\n", true);
                        UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:" + MainFrame.jingShenJtf.getText() + "mm", recordTa,ColorUtils.BLUE);

                        sendC4Str = Constant.BARCODECMD + MainFrame.jingShenJtf.getText().trim() + " 0";
                        pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                        if (pc2C4Rev.contains("ok")) {
//                            logWriter.append("机械臂" + MainFrame.jingShenJtf.getText() + "mm就位");
//                            UIUtils.updateTxA(recordTa, "机械臂" + MainFrame.jingShenJtf.getText() + "mm就位" + "\r\n", true);
                            UIUtils.logColorUpdate(logWriter, "机械臂" + MainFrame.jingShenJtf.getText() + "mm就位", recordTa,ColorUtils.BLUE);

//                            logWriter.append("开始扫码");
//                            UIUtils.updateTxA(recordTa, "开始扫码" + "\r\n", true);
                            UIUtils.logColorUpdate(logWriter, "开始扫码", recordTa,ColorUtils.BLACK);
                            //验证静态扫描条码
                            long st = System.currentTimeMillis();
                            UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                            serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                            revPos = serialConnection.serialRead(Constant.TIMEOUT);
                            long ed = System.currentTimeMillis();
                            UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                            UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                            UIUtils.logColorUpdate(logWriter, "取回终端扫码返回", recordTa,ColorUtils.BLACK);
                            //UIUtils.logUpdate(logWriter, "取回终端扫码返回", recordTa);
                           // UIUtils.logUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos, recordTa);
                            UIUtils.logColorUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:"+ revPos, recordTa,ColorUtils.BLACK);

                            RecordLog.Logger().info("原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos);
                            if ("ffff".equalsIgnoreCase(revPos)) { //可以省略，为了详细区分，先加上
                                UIUtils.logColorUpdate(logWriter, "超时读取条码失败!", recordTa,ColorUtils.RED);
                                UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                               // UIUtils.logUpdate(logWriter, "超时读取条码失败!", recordTa);
                               // UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                return 3;
                            } else {
                                if (content.equals(revPos)) {
                                    UIUtils.logColorUpdate(logWriter, "扫码验证通过!", recordTa,ColorUtils.BLUE);
                                    UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
//                                    UIUtils.logUpdate(logWriter, "扫码验证通过!", recordTa);
//                                    UIUtils.logUpdate(logWriter, "Pass", recordTa);
                                } else {
//                                    UIUtils.logUpdate(logWriter, "扫码验证失败!", recordTa);
//                                    UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                    UIUtils.logColorUpdate(logWriter, "扫码验证失败!", recordTa,ColorUtils.RED);
                                    UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                    return 3;
                                }
                            }
                            //开始动态扫码
                            UIUtils.logColorUpdate(logWriter, "左移12.5cm", recordTa,ColorUtils.BLUE);
                            UIUtils.logColorUpdate(logWriter, "已向机械臂发送指令，等待响应", recordTa,ColorUtils.BLUE);

//                            UIUtils.logUpdate(logWriter, "左移12.5cm", recordTa);
//                            UIUtils.logUpdate(logWriter, "已向机械臂发送指令，等待响应", recordTa);
                            sendC4Str = Constant.INITRATE + " " + MainFrame.jingShenJtf.getText();
                            pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                            if (pc2C4Rev.contains("ok")) {
                                //IUtils.logUpdate(logWriter, "机械臂偏左12.5cm就位", recordTa);
                                UIUtils.logColorUpdate(logWriter, "机械臂偏左12.5cm就位", recordTa,ColorUtils.BLUE);
                                boolean succFlag = true;
                                int count = 0 ;
                                for (int i = 50; i <= 200; i += 10) {
                                    for (int j = 1; j <= 2; j++) {
                                        if (j == 1) {
                                            UIUtils.logColorUpdate(logWriter, "机械臂 将右移25cm", recordTa,ColorUtils.BLUE);
                                            //UIUtils.logUpdate(logWriter, "机械臂 将右移25cm", recordTa);
                                            sendC4Str = Constant.RATECMD + " " + i + " -1";
                                        } else {
                                            UIUtils.logColorUpdate(logWriter, "机械臂 将左移25cm", recordTa,ColorUtils.BLUE);
                                           // UIUtils.logUpdate(logWriter, "机械臂 将左移25cm", recordTa);
                                            sendC4Str = Constant.RATECMD + " " + i + " 1";
                                        }
                                        UIUtils.logColorUpdate(logWriter, "已向机械臂发送指令，机械臂开始移动，速度为" + i + "mm/s,向终端发送扫码指令", recordTa,ColorUtils.BLUE);
                                        //UIUtils.logUpdate(logWriter, "已向机械臂发送指令，机械臂开始移动，速度为" + i + "mm/s,向终端发送扫码指令", recordTa);
                                       // UIUtils.logUpdate(logWriter, "开始扫码", recordTa);
                                        UIUtils.logColorUpdate(logWriter, "开始扫码", recordTa,ColorUtils.BLACK);
                                        ScanCode readCode = new ScanCode(serialConnection);
                                        new Thread(readCode).start();
                                        Thread.sleep(2000);
                                        long st1 = System.currentTimeMillis();
                                        UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st1)+"ms", recordTa,ColorUtils.OTHE);
                                        pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                                        if (pc2C4Rev.contains("ok")) {
                                            if (j == 1) {
//                                                UIUtils.logUpdate(logWriter, "机械臂 右移25cm成功", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "机械臂 右移25cm成功", recordTa,ColorUtils.BLUE);
                                            } else {
//                                                UIUtils.logUpdate(logWriter, "机械臂 左移25cm成功", recordTa);
                                                UIUtils.logColorUpdate(logWriter, "机械臂 左移25cm成功", recordTa,ColorUtils.BLUE);
                                            }
                                            while (true) {
                                                if (readCode.getReadContent() == null || readCode.getReadContent().length() > 0) {
                                                    break;
                                                } else {
                                                    Thread.sleep(50);
                                                }
                                            }
                                            revPos = readCode.getReadContent();
                                            long ed1 = System.currentTimeMillis();
                                            UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed1)+"ms", recordTa,ColorUtils.OTHE);
                                            UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed1-st1)+"ms", recordTa,ColorUtils.OTHE);
//                                          UIUtils.logUpdate(logWriter, "取回终端扫码返回", recordTa);
//                                          UIUtils.logUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos, recordTa);

                                            UIUtils.logColorUpdate(logWriter, "取回终端扫码返回", recordTa,ColorUtils.BLACK);
                                            UIUtils.logColorUpdate(logWriter, "原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos, recordTa,ColorUtils.BLACK);

                                            RecordLog.Logger().info("原始条码显示内容:" + content + "\r\n" + "扫码设备返回原始数据:" + revPos);
                                            if (revPos == null) {
                                                UIUtils.logColorUpdate(logWriter, "线程读取串口数据异常", recordTa,ColorUtils.RED);
                                                //UIUtils.logUpdate(logWriter, "线程读取串口数据异常", recordTa);

                                                RecordLog.Logger().error("线程读取串口数据异常");
                                                return 5;
                                            } else if ("ffff".equalsIgnoreCase(revPos)) { //可以省略，为了详细区分，先加上

                                                UIUtils.logColorUpdate(logWriter, "超时读取条码失败!", recordTa,ColorUtils.RED);
                                                UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                                //UIUtils.logUpdate(logWriter, "超时读取条码失败!", recordTa);
                                               // UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                                succFlag = false;
                                                count++;
                                            } else {
                                                if (content.equals(revPos)) {
//                                                    UIUtils.logUpdate(logWriter, "扫码验证通过!", recordTa);
//                                                    UIUtils.logUpdate(logWriter, "Pass", recordTa);
                                                    UIUtils.logColorUpdate(logWriter, "扫码验证通过!", recordTa,ColorUtils.BLUE);
                                                    UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
                                                } else {
                                                    UIUtils.logColorUpdate(logWriter, "扫码验证失败!", recordTa,ColorUtils.RED);
                                                    UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
//                                                    UIUtils.logUpdate(logWriter, "扫码验证失败!", recordTa);
//                                                    UIUtils.logUpdate(logWriter, "Fail", recordTa);
                                                    succFlag = false;
                                                    count++;
                                                }
                                            }
                                        } else {
                                            UIUtils.logColorUpdate(logWriter, "C4机械臂右移25cm失败!", recordTa,ColorUtils.RED);
                                            //UIUtils.logUpdate(logWriter, "C4机械臂右移25cm失败", recordTa);
                                            RecordLog.Logger().error("PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
                                            return 2;
                                        }
                                    }
                                    if (!succFlag) {
                                        if(i > 190) {
                                            UIUtils.logColorUpdate(logWriter, "", recordTa,ColorUtils.WHITE);
                                            UIUtils.logColorUpdate(logWriter, "加油!已经" + (i - 10) + "mm/s了，离标准很近了", recordTa,ColorUtils.RED);
                                            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                            xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),(i - 10) + "");
                                            xml.writeXml();
                                            return 3;
                                        }
                                        else if(i < 100) {
                                            if (count >= 2){
                                                UIUtils.logColorUpdate(logWriter, "老铁!扎心了，" + i + "mm/s都过不了啊", recordTa,ColorUtils.RED);
                                            }
                                        } else {
                                            if (count >= 2)
                                                UIUtils.logColorUpdate(logWriter, "加油!已经" + i + "mm/s了，离标准很近了", recordTa,ColorUtils.RED);
                                        }

                                        if(count >= 2) {
                                            UIUtils.logColorUpdate(logWriter, "", recordTa,ColorUtils.WHITE);
                                            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                            xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),i + "");
                                            xml.writeXml();
                                            //pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                                            return 3;
                                        }
                                    }
                                }//end for
                                //pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                                UIUtils.logColorUpdate(logWriter, "恭喜，200mm/s测试通过！", recordTa,ColorUtils.BLUE);
                                xml.addResult(caseTxt, "PASS", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"200");
                                xml.writeXml();
                                //UIUtils.logUpdate(logWriter, "恭喜，200mm/s测试通过！", recordTa);
                                //UIUtils.updateTxA(recordTa,"[==============    Test END    ==============]"+"\r\n",true);
                                return 0;
                            } else {
                                UIUtils.logColorUpdate(logWriter, "C4机械臂左移12.5cm失败", recordTa,ColorUtils.RED);
                                //UIUtils.logUpdate(logWriter, "C4机械臂左移12.5cm失败", recordTa);
                                RecordLog.Logger().error("PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
                                return 2;
                            }
                        } else {
                            UIUtils.logColorUpdate(logWriter, "C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
                            //UIUtils.logUpdate(logWriter, "C4机械臂移动就位距离失败", recordTa);
                            RecordLog.Logger().error("PC发送给POS的命令:" + sendC4Str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
                            return 2;
                        }
                    } else {// backp0
                        UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + Constant.ORIGIN + " PC收到C4的【错误】回应数据包:" + pc2C4Rev +
                                " C4机械臂原点就位失败", recordTa, ColorUtils.RED);
                        return 2;
                    }
                } else { //end pc2Phone
                    UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+ (FileName==null?"generate code":Constant.SHOW+FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                            ",显示条码失败", recordTa,ColorUtils.RED);
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                RecordLog.Logger().error("Try抛出的异常：" + e.getMessage());
                return 4;//特殊处理
            }
        } else { //只为特殊情况的时候
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}
