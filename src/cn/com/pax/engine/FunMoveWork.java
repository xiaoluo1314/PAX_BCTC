package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;
import cn.com.pax.log.RecordLog;
import cn.com.pax.utils.ColorUtils;
import cn.com.pax.utils.ReadCfgUtils;
import cn.com.pax.utils.ReadExcelCfgUtils;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;

/**
 * 功能测试
 *
 * @author luohl
 * @create 2018-01-24-16:24
 */
public class FunMoveWork extends BaseEngine implements BaseMoveWork {
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
        RecordLog.Logger().info("【功能测试】" );
        if(serialConnection!= null && pc2C4!=null && pc2Phone!= null ){
            String sendC4Str;
            String revPos;
            String pc2PhoneRev = null;
            String pc2C4Rev;
            int dis = 0;
//            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1){
//                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
//                return 5;
//            }else {
                try{
                    serialConnection.readExtraData(Constant.READEXTRATIME,Constant.MAXTIME);
                    if (content == null ){
                        RecordLog.Logger().info("配置文件的数据有误，请核对后在测试！");
                        return 5;
                    }
                    if (!"value".equals(bright)){
                        pc2PhoneRev = pc2Phone.writeReadOneLine(Constant.SETBG +bright);
                        if (!pc2PhoneRev.contains("ok")){
                            RecordLog.Logger().info("配置文件的数据有误，请核对后在测试！");
                            return 7;
                        }
                    }
                    pc2PhoneRev = cn.com.pax.utils.FileUtils.getString(readCfgUtils, logWriter ,FileName ,recordTa, pc2Phone, pc2PhoneRev, content,  version,  errLevel,
                            precision,  bg,  fg,  charSet, ratio, height,sizeUit,fixationWidth);

                    if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){//针对qr码做处理
                        content = ReadExcelCfgUtils.parseContend(content);
                    }
                    if (pc2PhoneRev.contains("ok")){
                        UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa, ColorUtils.BLACK);
                        UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);
                        pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                        if (pc2C4Rev.contains("ok")){
                            while (true){
                                sendC4Str = Constant.DEPTHCMD + "0 " + dis; //dofone 0 30 读条码 30mm
                                pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                                UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:"+ dis +"mm", recordTa,ColorUtils.BLUE);
                                if (pc2C4Rev.contains("ok")){
                                    UIUtils.logColorUpdate(logWriter, "机械臂"+dis+"mm就位", recordTa,ColorUtils.BLUE);
                                    long st = System.currentTimeMillis();
                                    UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);

                                    serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                                    revPos = serialConnection.serialRead(Constant.TIMEOUT);
                                    long ed = System.currentTimeMillis();
                                    UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);

                                    UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                    int i = UIUtils.recordLog(logWriter,revPos, content, recordTa);
                                    if (i == 0){
                                        xml.addResult(caseTxt, "PASS", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                        xml.writeXml();
                                        return 0;
                                    }
                                    if (dis >= 100){
                                        xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
                                        xml.writeXml();
                                        return 3;
                                    }
                                    dis += 10;
                                }else{
                                    UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev+",C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
                                    return 2;
                                }
                            }
                        }else{
                            UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN + ",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                    ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                            return 2;
                        }

                    }else{
                        RecordLog.Logger().error("PC发送给Phone的请求:" + (FileName == null?"generate code":Constant.SHOW + FileName) + "," + "PC收到Phone的【错误】数据包:" + pc2PhoneRev + ",显示条码失败");
                        UIUtils.logColorUpdate(logWriter,"PC发送给Phone的请求:"+ (FileName == null ?"generate code":Constant.SHOW + FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                                ",显示条码失败", recordTa, ColorUtils.RED);
                        return 1;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    RecordLog.Logger().error("Try抛出的异常："+ e.getMessage());
                    return 4;//特殊处理
                }finally {
                    pc2Phone.writeReadOneLine(Constant.SETBG + 200);
                }
           // }
        }else{
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }
}


