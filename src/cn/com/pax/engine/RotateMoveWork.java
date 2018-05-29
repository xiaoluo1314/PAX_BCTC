package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.RecordLog;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.utils.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 360旋转
 *
 * @author luohl
 * @create 2017-12-18-20:20
 */
public class RotateMoveWork extends  BaseEngine implements BaseMoveWork {
    /*
    1、移动到最优景深的位置
    2、发送指令通知手机显示条码
    3、通知机械臂旋转0度
    4、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
    5、记录测试结果
    6、继续测试旋转10度，重复4和5步，每10度增加一次，一直测试到350度为止
    7、每个角度都要测试通过才算通过，否则测试失败
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
        RecordLog.Logger().info("【360旋转测试】");
        if (pc2C4!= null && pc2Phone != null &&serialConnection != null) {
            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else {
                try{
                    String sendC4Str;
                    String revPos;
                    String pc2PhoneRev = null;
                    String pc2C4Rev;
                    List<Integer>failList = new ArrayList<>();
                    List<String>failRecord = new ArrayList<>();
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

//                    logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date())+" " + "Time is:" + TimeUtils.getTimeFormat(new Date()));
//                    logWriter.append(caseTxt.split(".txt")[0]);
//                    logWriter.append("特征:C4");
//                    logWriter.append("数据:" + content);
//                    logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(content));//可要不可要
//                    logWriter.append("码制:");
//                    logWriter.append("版本:"+ (version ==null? "":version));
//                    logWriter.append("容错级别:" + (errLevel == null ? "" : errLevel));
//                    logWriter.append("模式:");
//                    logWriter.append("屏幕亮度:");
//                    logWriter.append("发送显示条码的命令");
//
//
//                    UIUtils.updateTxA(recordTa,"Date is:" + TimeUtils.getDateFormat(new Date())+"," + "Time is:" + TimeUtils.getTimeFormat(new Date())+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,caseTxt.split(".txt")[0]+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"特征:C4"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"数据:" + content+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"数据转为ASCII:" + AsciiConvertUtils.parseAscii(content)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"码制:"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"版本:" +(version ==null? "":version)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"容错级别:"+(errLevel == null ? "" : errLevel)+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"模式:"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"屏幕亮度:"+"\r\n",true);
//                    UIUtils.updateTxA(recordTa,"发送显示条码的命令"+"\r\n",true);
                    failList.clear();
                    failRecord.clear();
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
//                        logWriter.append("回原点:0mm");
//                        UIUtils.updateTxA(recordTa,"APP 端数据准备完毕，准备生成条码"+"\r\n",true);
//                        UIUtils.updateTxA(recordTa,"回原点:0mm"+"\r\n",true);

                        UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa,ColorUtils.BLACK);
                        UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

                        pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                        RecordLog.Logger().info("PC发给C4的请求:"+Constant.ORIGIN +" PC收到C4的回应数据包:" + pc2C4Rev);

                        if (pc2C4Rev.contains("ok")){

//                            logWriter.append("C4机械臂回原点就位");
//                            logWriter.append("C4机械臂开始移动到景深"+MainFrame.jingShenJtf.getText().trim()+"mm位置!");
//                            UIUtils.updateTxA(recordTa,"C4机械臂开始移动到景深"+MainFrame.jingShenJtf.getText().trim()+"mm位置!"+"\r\n",true);

                            UIUtils.logColorUpdate(logWriter, "C4机械臂回原点就位", recordTa,ColorUtils.BLUE);
                            UIUtils.logColorUpdate(logWriter, "C4机械臂开始移动到景深"+MainFrame.jingShenJtf.getText().trim()+"mm位置!", recordTa,ColorUtils.BLUE);

                            pc2C4Rev = pc2C4.writeReadOneLine(Constant.INIT360 + MainFrame.jingShenJtf.getText().trim());//
                            if (pc2C4Rev.contains("ok")){
//                                logWriter.append("C4机械臂移动到景深位置处成功!");
//                                UIUtils.updateTxA(recordTa,"C4机械臂移动到景深位置处成功!"+"\r\n",true);

                                UIUtils.logColorUpdate(logWriter, "C4机械臂移动到景深位置处成功!", recordTa,ColorUtils.BLUE);

                                for (int i =0;i<=350;i+=10){
//                                    logWriter.append("C4机械臂移动到下一个位置:"+i+"度");
//                                    UIUtils.updateTxA(recordTa,"C4机械臂移动到下一个位置:"+i+"度"+"\r\n",true);

                                    UIUtils.logColorUpdate(logWriter, "C4机械臂移动到下一个位置:"+i+"度", recordTa,ColorUtils.BLUE);

                                    pc2C4Rev = pc2C4.writeReadOneLine(Constant.ROTATE360 + i);

                                   if(pc2C4Rev.contains("ok")){

//                                       logWriter.append("C4机械臂"+i+"度就位");
//                                       UIUtils.updateTxA(recordTa,"C4机械臂"+i+"度就位"+"\r\n",true);

                                       UIUtils.logColorUpdate(logWriter, "C4机械臂"+i+"度就位", recordTa,ColorUtils.BLUE);
                                       long st = System .currentTimeMillis();
                                       UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                                       serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                                       revPos = serialConnection.serialRead(Constant.TIMEOUT);
                                       long ed = System.currentTimeMillis();
                                       UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                                       UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                       int fail  = UIUtils.recordLog(logWriter,revPos, content,recordTa);
                                       if(fail !=0 ){
                                           failList.add(fail);
                                           failRecord.add(i+"");
                                       }
                                   }else{
//                                       logWriter.append("PC发送给C4的请求:"+(Constant.ROTATE360 + i)+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+",C4机械臂旋转"+i+"度失败！");
//                                       UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+(Constant.ROTATE360 + i)+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                               ",C4机械臂旋转"+i+"度失败！"+"\r\n",true);

                                       UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+(Constant.ROTATE360 + i)+
                                               ",PC收到C4的【错误】回应数据包:"+pc2C4Rev+",C4机械臂旋转"+i+"度失败！", recordTa,ColorUtils.RED);

                                       return 2;
                                   }
                                }
                                UIUtils.recordDetail(xml,caseTxt,logWriter,failRecord);
                                //pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                                return UIUtils.getResult(failList, logWriter, recordTa);
                            }else{
                                UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.INIT360 + MainFrame.jingShenJtf.getText().trim()+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                        ",C4机械臂移动到景深位置处失败！", recordTa,ColorUtils.RED);

//                                logWriter.append("C4机械臂移动到景深位置处失败!");
//                                UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.INIT360 + MainFrame.jingShenJtf.getText().trim()+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                        ",C4机械臂移动到景深位置处失败！"+"\r\n",true);
                                return 2;
                            }

                        }else{
                            UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                    ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
//                            logWriter.append("C4机械臂原点就位失败");
//                            UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                    ",C4机械臂原点就位失败"+"\r\n",true);
                            return 2;
                        }
                    }else{
                        UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+ (FileName==null?"generate code":Constant.SHOW+FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                                ",显示条码失败", recordTa,ColorUtils.RED);

//                        logWriter.append("显示条码失败");
//                        UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+FileName+",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
//                                ",显示条码失败"+"\r\n",true);
                        return 1;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    RecordLog.Logger().error("Try抛出的异常："+ e.getMessage());
                    return 4;//特殊处理
                }
            }
        }else{
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}
    