package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.barcode.GenerateCode;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import cn.com.pax.log.RecordLog;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.utils.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 条码制式识读能力（ZCCS）
 *
 * @author luohl
 * @create 2017-12-13-15:30
 */
public class BarCodeMoveWork extends  BaseEngine implements BaseMoveWork {

    /*
    1、发送指令通知手机显示条码（如果能自动生成最好）
    2、机械臂移动到最优景深的位置
    3、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
    4、记录结果
     */
    /**
     *
     * @param caseTxt
     * @return 0 >80 PASS 1 PC与Phone显示图片有问题，2 PC与C4通讯有问题 3 <80mm FAIL  4 IOEXCEPTION或整个通讯异常  5 最优景深未测或者为设置
     */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
//        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
//        logWriter = new LogWriter(path+"\\trace\\"+caseTxt.split(".txt")[0]);
//        MyButtonClick.getInstance().setLogWriter(logWriter);
//        C4Socket pc2C4 = SocketOperation.getPc2C4();
//        C4Socket pc2Phone = SocketOperation.getPc2Phone();
//        SerialConnection serialConnection = SerialOperation.getInstance().getConnection();
        RecordLog.Logger().info("【常规测试（ZCCS）】" );
        if(serialConnection!= null && pc2C4!=null && pc2Phone!= null ){
            String sendC4Str;
            String revPos;
            String pc2PhoneRev = null;
            String pc2C4Rev;
           // int depth = FileUtils.calcOptimalDepth(path+ File.separator+Constant.OPTIMADEPTH);
            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else{
                //发送命令显示图片
                try{
//                    UIUtils.updateTxA(recordTa,"[==============    Test Start    ==============]"+"\r\n");
//                    UIUtils.logColorUpdate(logWriter, "Date is:" + TimeUtils.getDateFormat(new Date()) + " " + "Time is:" + TimeUtils.getTimeFormat(new Date()),recordTa,ColorUtils.BLUE);
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

//                    logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date()) + "Time is:" + TimeUtils.getTimeFormat(new Date()));
//                    logWriter.append(caseTxt.split(".txt")[0]);
//                    logWriter.append("特征:C4");
//                    logWriter.append("数据:" + content);
//                    logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(content));//可要不可要
//                    logWriter.append("码制:"+readCfgUtils.getCodeType());
//                    logWriter.append("版本:"+ (version ==null? "":version));
//                    logWriter.append("容错级别:" + (errLevel == null ? "" : errLevel));
//                    logWriter.append("模式:");
//                    logWriter.append("屏幕亮度:");
//                    logWriter.append("发送条码显示命令");
//
//                    UIUtils.updateTxA(recordTa,"[==============    Test Start    ==============]"+"\r\n");
//                    UIUtils.updateTxA(recordTa,"Date is:" + TimeUtils.getDateFormat(new Date())+"," + "Time is:" + TimeUtils.getTimeFormat(new Date())+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,caseTxt.split(".txt")[0]+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"特征:C4"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"数据:" + content+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"数据转为ASCII:" + AsciiConvertUtils.parseAscii(content)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"码制:"+readCfgUtils.getCodeType()+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"版本:" +(version ==null? "":version)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"容错级别:"+(errLevel == null ? "" : errLevel)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"模式:"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"屏幕亮度:"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"发送显示条码的命令"+"\r\n",true);

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
                    if (pc2PhoneRev.contains("ok")){
//                        logWriter.append("APP 端数据准备完毕，准备生成条码");
//                        UIUtils.updateTxA(recordTa,"APP 端数据准备完毕，准备生成条码\r\n",true);
//                        logWriter.append("回原点:0mm");
//                        UIUtils.updateTxA(recordTa,"回原点:0mm\r\n",true);

                        UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa,ColorUtils.BLACK);
                        UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

                        pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);

                        if(pc2C4Rev.contains("ok")){
//                            logWriter.append("机械臂原点就位");
//                            UIUtils.updateTxA(recordTa,"机械臂原点就位\r\n",true);
                            UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);

//                            logWriter.append("机械臂移动到下一个位置:"+MainFrame.jingShenJtf.getText()+"mm");
//                            UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:"+MainFrame.jingShenJtf.getText()+"mm\r\n",true);

                            UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:"+MainFrame.jingShenJtf.getText()+"mm", recordTa,ColorUtils.BLUE);

                            sendC4Str = Constant.BARCODECMD + MainFrame.jingShenJtf.getText().trim() +" 0";
                            pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                            if (pc2C4Rev.contains("ok")){
//                                logWriter.append("机械臂"+MainFrame.jingShenJtf.getText()+"mm就位");
//                                UIUtils.updateTxA(recordTa,"机械臂"+MainFrame.jingShenJtf.getText()+"mm就位"+"\r\n",true);

                                UIUtils.logColorUpdate(logWriter, "机械臂"+MainFrame.jingShenJtf.getText()+"mm就位", recordTa,ColorUtils.BLUE);
                                UIUtils.logColorUpdate(logWriter, "请扫码", recordTa,ColorUtils.BLACK);

//                                logWriter.append("请扫码");
//                                UIUtils.updateTxA(recordTa,"请扫码"+"\r\n",true);
                                long st = System.currentTimeMillis();
                                UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                                serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                                revPos = serialConnection.serialRead(Constant.TIMEOUT);
                                long ed = System.currentTimeMillis();
                                UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                                UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                RecordLog.Logger().info("原始条码显示内容:"+content+","+"扫码设备返回原始数据:"+revPos);
//                                logWriter.append("原始条码显示内容:"+content+","+"扫码设备返回原始数据:"+revPos);
//                                UIUtils.updateTxA(recordTa,"原始条码显示内容:"+content+","+"扫码设备返回原始数据:"+revPos+"\r\n",true);

                                UIUtils.logColorUpdate(logWriter, "原始条码显示内容:"+content+","+"扫码设备返回原始数据:"+revPos, recordTa,ColorUtils.BLACK);

                              //  ParseXml xml = new ParseXml(MainFrame.getPath().split(Constant.CONFIG)[0]+ File.separator+Constant.CASEXML);
                              //  pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                                if ("ffff".equalsIgnoreCase(revPos)){ //可以省略，为了详细区分，先加上
//                                    logWriter.append("超时读取条码失败!");
//                                    logWriter.append("Fail");
//                                    UIUtils.updateTxA(recordTa,"超时读取条码失败!\r\n",true);
//                                    UIUtils.updateTxA(recordTa,"Fail\r\n",true);
                                    UIUtils.logColorUpdate(logWriter, "超时读取条码失败,返回的数据:"+revPos, recordTa,ColorUtils.RED);
                                    UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                    xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                    xml.writeXml();
                                    return 3;
                                }else{
                                    if (content.equals(revPos)){
//                                       logWriter.append("扫码验证通过!");
//                                       logWriter.append("Pass");
//                                       UIUtils.updateTxA(recordTa,"扫码验证通过!\r\n",true);
//                                       UIUtils.updateTxA(recordTa,"Pass\r\n",true);
                                        UIUtils.logColorUpdate(logWriter, "扫码验证通过", recordTa, ColorUtils.BLUE);
                                        UIUtils.logColorUpdate(logWriter, "Pass", recordTa, ColorUtils.BLUE);
                                        xml.addResult(caseTxt, "PASS", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                        xml.writeXml();
                                        return 0;
                                    }else{
                                        UIUtils.logColorUpdate(logWriter, "扫码验证失败,返回的数据:"+revPos, recordTa,ColorUtils.RED);
                                        UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                                        xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                        xml.writeXml();
                                        return 3;
                                    }
                                }
                            }else{
                                RecordLog.Logger().error("PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev);
                                UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev+",C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
                                return 2;
                            }

                        }else{
//                            logWriter.append("PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+ ",C4机械臂原点就位失败");
//                            UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                    ",C4机械臂原点就位失败"+"\r\n",true);

                            RecordLog.Logger().error("PC发送给C4的命令:" + Constant.ORIGIN + "," + "PC接受C4错误返回码:" + pc2C4Rev);
                            UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                    ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                            return 2;
                        }
                    }else{
                        RecordLog.Logger().error("PC发送给Phone的请求:" + (content != null ? "generate code":Constant.SHOW + FileName) + "," + "PC收到Phone的【错误】数据包:" + pc2PhoneRev + ",显示条码失败");
//                        logWriter.append("显示条码失败");
//                        UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+Constant.SHOW+FileName+",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
//                                ",显示条码失败"+"\r\n",true);
                        UIUtils.logColorUpdate(logWriter,"PC发送给Phone的请求:"+ (content != null ?"generate code":Constant.SHOW + FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                                ",显示条码失败", recordTa, ColorUtils.RED);
                        return 1;
                    }
                }catch (Exception e){
                    RecordLog.Logger().error("抛出的异常："+ e.getMessage());
                    return 4;
                }
            }
        } else {
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}
    