package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.barcode.GenerateCode;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.QrCfgInfo;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import cn.com.pax.log.RecordLog;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.utils.*;
import gnu.io.UnsupportedCommOperationException;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 条码偏转测试
 *
 * @author luohl
 * @create 2017-12-14-14:35
 */
public class LeftRightOffset extends BaseEngine implements BaseMoveWork {
    private C4Socket pc2C4;
    private String sendC4Str ;
    private String revPos;
    private String pc2C4Rev;
    private SerialConnection serialConnection;
    private List<Integer> failList = new ArrayList<>();//只记录失败
    private List<String>failRecord = new ArrayList<>();
    /*
    1、移动到最优景深的位置(XXXX)
    2、发送指令通知手机显示条码
    3、通知机械臂左翻0度
    4、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
    5、记录测试结果
    6、继续测试左翻5度，重复4和5步，每5度增加一次，一直测试到45度为止
    7、然后测试右翻5度，重复4和5步，每5度增加一次，一直测试到45度位置
    8、继续测试下翻5度，重复4和5步，每5度增加一次，一直测试到45度位置
    9、最后测试右翻5度，重复4和5步，每5度增加一次，一直测试到45度位置
    10、每个角度都要测试通过才算通过，否则测试失败
    */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
        logWriter = new LogWriter(path+"\\trace\\"+caseTxt.split(".txt")[0]);
        MyButtonClick.getInstance().setLogWriter(logWriter);
        pc2C4 = SocketOperation.getPc2C4();
        C4Socket pc2Phone = SocketOperation.getPc2Phone();
        serialConnection = SerialOperation.getInstance().getConnection();
        RecordLog.Logger().info("【识读角度测试（JDCS）---角度偏转】" );
        if(serialConnection != null && pc2C4 !=null && pc2Phone!= null ){
            String pc2PhoneRev = null;
            boolean first = false;
            if (Integer.parseInt(MainFrame.jingShenJtf.getText().trim()) == -1){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else{
                try {
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
//                    logWriter.append("版本:"+(version ==null? "":version));
//                    logWriter.append("容错级别:" +(errLevel == null ? "" : errLevel));
//                    logWriter.append("模式:");
//                    logWriter.append("屏幕亮度:");
//                    logWriter.append("发送条码显示命令");
//
//
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
//                    logWriter.append("PC发送给Phone的请求:"+(Constant.SHOW + FileName)+",PC收到Phone的回应数据包:" + pc2PhoneRev);
//                    //记录一下，方便程序查看，当前的日志
//                    RecordLog.Logger().info("PC发送给Phone的请求:"+(Constant.SHOW + FileName)+",PC收到Phone的回应数据包:" + pc2PhoneRev);
//                    UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+(Constant.SHOW + FileName)+",PC收到Phone的回应数据包:" + pc2PhoneRev+"\r\n",true);
                    if (pc2PhoneRev.contains("ok")){
//                        logWriter.append("APP 端数据准备完毕，准备生成条码");
//                        UIUtils.updateTxA(recordTa,"APP 端数据准备完毕，准备生成条码\r\n",true);
//                        logWriter.append("回原点:0mm");
//                        UIUtils.updateTxA(recordTa,"回原点:0mm\r\n",true);
                        UIUtils.logColorUpdate(logWriter, "APP 端数据准备完毕，准备生成条码", recordTa,ColorUtils.BLACK);
                        UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

                        pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                        if(pc2C4Rev.contains("ok")) {

//                            logWriter.append("机械臂原点就位");
//                            UIUtils.updateTxA(recordTa,"机械臂原点就位\r\n",true);
                            UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);
                            int i;
                            i = handlePubFun(Constant.RIGHTLEFT1, first, Constant.LEFT ,content,MainFrame.jingShenJtf.getText().trim(),recordTa);
                            if (i == 2){
                                return 2;
                            }
                            i = handlePubFun(Constant.RIGHTLEFT1, !first, Constant.RITHT,content,MainFrame.jingShenJtf.getText().trim(),recordTa);
                            if (i == 2){
                                return 2;
                            }
                            i =handlePubFun(Constant.UPDOWN1, !first, Constant.UP, content,MainFrame.jingShenJtf.getText().trim(),recordTa);
                            if (i == 2){
                                return 2;
                            }
                            i= handlePubFun(Constant.UPDOWN1, !first, Constant.DOWN, content,MainFrame.jingShenJtf.getText().trim(),recordTa);
                            if (i == 2){
                                return 2;
                            }
                          //  pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                            UIUtils.recordDetail(xml,caseTxt,logWriter,failRecord);
                            return UIUtils.getResult(failList, logWriter, recordTa);
                        }else{
//                            logWriter.append("C4机械臂原点就位失败");
//                            UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                                    ",C4机械臂原点就位失败"+"\r\n",true);

                            RecordLog.Logger().error("PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                    ",C4机械臂原点就位失败");

                            UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+" PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                                    ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                            return 2;
                        }
                    }else{
                        RecordLog.Logger().error("PC发送给Phone的请求:"+FileName+",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
                                ",显示条码失败");
//                        logWriter.append("显示条码失败");
//                        UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+FileName+",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
//                                ",显示条码失败"+"\r\n",true);

                        UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+ (FileName==null?"generate code":Constant.SHOW+FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                                ",显示条码失败", recordTa,ColorUtils.RED);
                        return 1;
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

    //rightleft45 0 xxx  rightleft45 1 xxx  rightleft45 0 xxx  rightleft45 1 -xxx
    //updown45 0 xxx     updown45 1 xxx     updown45 0 xxx     updown45 1 -xxxx

    private int handlePubFun(String cmd, boolean flag, String dir ,String contend ,String OptimalValue,JEditorPane recordTa)throws Exception{
        String  negSign = "";
        if(Constant.LEFT.equals(dir)) {
//            logWriter.append("机械臂移动到下一个位置:" + dir + "0度");
//            UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:" + dir + "0度"+"\r\n",true);
            UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:" + dir + "0度", recordTa,ColorUtils.BLUE);
        }
        String str;
        if (Constant.LEFT.equals(dir)||Constant.RITHT.equals(dir)){
            str = Constant.RIGHTLEFT0 + OptimalValue;
            pc2C4Rev = pc2C4.writeReadOneLine(str);
        }else{
            str = Constant.UPDOWN0 + OptimalValue;
            pc2C4Rev = pc2C4.writeReadOneLine(str);
        }
        if (pc2C4Rev.contains("ok")){
//            logWriter.append("机械臂"+dir+"0度就位");
//            UIUtils.updateTxA(recordTa,"机械臂"+dir+"0度就位"+"\r\n",true);

            UIUtils.logColorUpdate(logWriter, "机械臂"+dir+"0度就位", recordTa,ColorUtils.BLUE);
            if(!flag){
                sendRevPos(contend,0, dir,recordTa);
            }
            //只判断一次，不拿到循环里
            if(Constant.RITHT .equals(dir)||Constant.DOWN.equals(dir)){
                negSign="-";
            }
            //公共
            for (int i = 5; i <= Constant.ANGLE; i+=5) {
                UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:"+dir+i+"度", recordTa,ColorUtils.BLUE);
//                logWriter.append("机械臂移动到下一个位置:"+dir+i+"度");
//                UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:"+dir+i+"度"+"\r\n",true);
                pc2C4Rev = pc2C4.writeReadOneLine(cmd+negSign+i);
                if (pc2C4Rev.contains("ok")){
//                    logWriter.append("机械臂"+dir+i+"度就位");
//                    UIUtils.updateTxA(recordTa,"机械臂"+dir+i+"度就位"+"\r\n",true);
                    UIUtils.logColorUpdate(logWriter, "机械臂"+dir+i+"度就位", recordTa,ColorUtils.BLUE);
                    sendRevPos(contend, i, dir,recordTa);
                }else{

                    UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + (cmd+i) + "," + "PC收到C4的【错误】回应数据包:" +
                            pc2C4Rev+"C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
                    RecordLog.Logger().error("PC发送给C4的请求:" + (cmd+i) + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev);
//                    logWriter.append("C4机械臂移动就位距离失败");
//                    UIUtils.updateTxA(recordTa,"PC发送给C4的请求:" + (cmd+i) + "," + "PC收到C4的【错误】回应数据包:" +
//                            pc2C4Rev+"\r\n",true);
                    return 2;
                }
            }
            return 0;
        }else{
            UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + str + "," + "PC收到C4的【错误】回应数据包:" +
                    pc2C4Rev+"C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
            logWriter.append("C4机械臂移动就位距离失败");
//            RecordLog.Logger().error("PC发送给C4的命令:" + str + "," + "PC接受POS命令返回码:" + pc2C4Rev);
//            UIUtils.updateTxA(recordTa,"PC发送给C4的请求:" + str + "," + "PC收到C4的【错误】回应数据包:" +
//                    pc2C4Rev+"\r\n",true);
            return 2;
        }
    }

    private void sendRevPos(String contend ,int angle,String dir,JEditorPane recordTa) throws IOException, UnsupportedCommOperationException {
        long st = System .currentTimeMillis();
        UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
        serialConnection.serailWrite(Constant.SENDPOS.getBytes());
        revPos = serialConnection.serialRead(Constant.TIMEOUT);
        long ed = System .currentTimeMillis();
        UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
        UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);

        RecordLog.Logger().info("PC收到POS返回数据包:"+revPos+"条码内容:"+contend +" "+dir+angle+"度");
//        logWriter.append("请扫码");
//        logWriter.append("扫码设备返回原始数据:"+revPos);
//        UIUtils.updateTxA(recordTa,"请扫码"+"\r\n",true);
//        UIUtils.updateTxA(recordTa,"扫码设备返回原始数据:"+revPos+"\r\n",true);
        UIUtils.logColorUpdate(logWriter, "请扫码", recordTa,ColorUtils.BLACK);
        UIUtils.logColorUpdate(logWriter, "扫码设备返回原始数据:"+revPos, recordTa,ColorUtils.BLACK);

        if("ffff".equalsIgnoreCase(revPos)){
//            logWriter.append("超时读取条码失败!");
//            UIUtils.updateTxA(recordTa,"超时读取条码失败!"+"\r\n",true);
            UIUtils.logColorUpdate(logWriter, "超时读取条码失败:"+ revPos, recordTa,ColorUtils.RED);
            failList.add(angle);//添加什么无所谓
            failRecord.add(dir+angle);
        }else{
            if (revPos.equals(contend)){
//                logWriter.append("扫码验证通过!");
//                UIUtils.updateTxA(recordTa,"扫码验证通过!"+"\r\n",true);
                UIUtils.logColorUpdate(logWriter, "扫码验证通过", recordTa,ColorUtils.BLACK);
            }else{

//                logWriter.append("扫码验证失败:扫码成功，但返回数据错误");
//                logWriter.append("期望返回:"+contend);
                UIUtils.logColorUpdate(logWriter, "扫码验证失败:扫码成功，但返回数据错误", recordTa,ColorUtils.RED);
                UIUtils.logColorUpdate(logWriter, "期望返回:"+contend+",实际返回的数据:"+revPos, recordTa,ColorUtils.RED);

//              UIUtils.updateTxA(recordTa,"扫码验证失败:扫码成功，但返回数据错误"+"\r\n",true);
//              UIUtils.updateTxA(recordTa,"期望返回:"+contend+"\r\n",true);
                failList.add(angle);
                failRecord.add(dir+angle);
            }
        }
    }
}
    