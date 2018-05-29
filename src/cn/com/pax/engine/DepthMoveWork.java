package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;

import cn.com.pax.log.LogWriter;
import cn.com.pax.log.RecordLog;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.utils.*;
import  org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 景深测试业务逻辑
 *
 * @author luohl
 * @create 2017-12-12-10:00
 */
public class DepthMoveWork extends BaseEngine implements BaseMoveWork {
    /*
            1、发送指令通知手机显示条码
            2、机械臂回原点后，后退移动到10mm处
            3、通过串口发送指令通知POS扫描条码、并设置10秒超时读取串口
            4、判断读取结果，如果成功，记录景深开始范围，记录成功标识；如果读取失败，无动作；
            5、继续往后移动10mm,判断是否大于200mm,如果超过，记录测试结果，流程结束。
            6、如果有成功标识，再次读取失败，则记录景深结束范围，流程结束；判断景深范围是否大于8cm，如果大于，则测试通过，否则失败；其他情况则继续步骤3测试
             */
    /**
     *
     * @param caseTxt
     * @return 0 >80 PASS 1 PC与Phone显示图片有问题，2 PC与C4通讯有问题 3 <80mm FAIL  4 IOEXCEPTION或整个通讯异常
     */
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa)  {
        super.readCFG(readCfgUtils,caseTxt, recordTa);
        // 不用判断网络了吧
        RecordLog.Logger().info("【景深测试】");
        int moveDis = 10;
        int minDis = 0;
        int maxDis = 0;
        boolean testOver = false;
        boolean flag = true;
        if (pc2C4!= null && pc2Phone != null &&serialConnection != null) try { //可提取到父类，暂时不提取了

            String sendC4Str;
            String revPos;
            String pc2PhoneRev =null;
            String pc2C4Rev;

          //到时候根据自动生成条码再封装成函数
//            logWriter.append("Date is:" + TimeUtils.getDateFormat(new Date())+" " + "Time is:" + TimeUtils.getTimeFormat(new Date()));
//            logWriter.append(caseTxt.split(".txt")[0]);
//            logWriter.append("特征:C4");
//            logWriter.append("数据:" + content);
//            logWriter.append("数据转为ASCII:" + AsciiConvertUtils.parseAscii(content));//可要不可要
//            logWriter.append("码制:"+readCfgUtils.getCodeType());
//            logWriter.append("版本:"+(version ==null? "":version));
//            logWriter.append("容错级别:" +(errLevel == null ? "" : errLevel));
//            logWriter.append("模式:");
//            logWriter.append("屏幕亮度:");
//            logWriter.append("发送显示条码的命令");


               /* UIUtils.updateTxA(recordTa,"Date is:" + TimeUtils.getDateFormat(new Date())+"," + "Time is:" + TimeUtils.getTimeFormat(new Date())+"\r\n",true);
                UIUtils.updateTxA(recordTa,caseTxt.split(".txt")[0]+"\r\n",true);
                UIUtils.updateTxA(recordTa,"特征:C4"+"\r\n",true);
                UIUtils.updateTxA(recordTa,"数据:" + content+"\r\n",true);
                UIUtils.updateTxA(recordTa,"数据转为ASCII:" + AsciiConvertUtils.parseAscii(content)+"\r\n",true);
                UIUtils.updateTxA(recordTa,"码制:"+readCfgUtils.getCodeType()+"\r\n",true);
                UIUtils.updateTxA(recordTa,"版本:" +(version ==null? "":version)+"\r\n",true);
                UIUtils.updateTxA(recordTa,"容错级别:"+(errLevel == null ? "" : errLevel)+"\r\n",true);
                UIUtils.updateTxA(recordTa,"模式:"+"\r\n",true);
                UIUtils.updateTxA(recordTa,"屏幕亮度:"+"\r\n",true);
                UIUtils.updateTxA(recordTa,"发送显示条码的命令"+"\r\n",true);*/

            serialConnection.readExtraData(Constant.READEXTRATIME, Constant.MAXTIME);
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
//                logWriter.append("APP 端数据准备完毕，准备生成条码");
//                UIUtils.updateTxA(recordTa,"APP 端数据准备完毕，准备生成条码"+"\r\n",true);
//                logWriter.append("回原点:0mm");
//                UIUtils.updateTxA(recordTa,"回原点:0mm"+"\r\n",true);
                UIUtils.logColorUpdate(logWriter, "回原点:0mm", recordTa,ColorUtils.BLUE);

                pc2C4Rev = pc2C4.writeReadOneLine(Constant.ORIGIN);
                RecordLog.Logger().info("PC发给C4的请求:"+Constant.ORIGIN +" PC收到C4的回应数据包:" + pc2C4Rev);
                if (pc2C4Rev.contains("ok")) {

                    doTest:
                    do {
                        while (true) {
                            if (flag) {
                                sendC4Str = Constant.DEPTHCMD + "0 10"; //dofone 0 10 读条码 0mm
                                pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
//                                logWriter.append("机械臂原点就位");
//                                UIUtils.updateTxA(recordTa,"机械臂原点就位"+"\r\n",true);
                                UIUtils.logColorUpdate(logWriter, "机械臂原点就位", recordTa,ColorUtils.BLUE);
                                if (pc2C4Rev.contains("ok")){
//                                    logWriter.append("机械臂移动到下一个位置:10mm");
//                                    UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:10mm"+"\r\n",true);

                                    UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:10mm", recordTa,ColorUtils.BLUE);
                                    UIUtils.logColorUpdate(logWriter, "机械臂10mm就位", recordTa,ColorUtils.BLUE);
//                                    logWriter.append("机械臂10mm就位");
//                                    UIUtils.updateTxA(recordTa,"机械臂10mm就位"+"\r\n",true);
                                    flag = false;
                                }else{
                                    break;
                                }
                            }else{
                                sendC4Str = Constant.DEPTHCMD + "1 " + moveDis; //dofone 1 10
                                pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
//                                logWriter.append("机械臂移动到下一个位置:"+moveDis+"mm");
//                                UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:"+moveDis+"mm"+"\r\n",true);

                                UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:"+moveDis+"mm", recordTa,ColorUtils.BLUE);
                                if (pc2C4Rev.contains("ok")){
                                    UIUtils.logColorUpdate(logWriter, "机械臂"+(moveDis)+"mm就位", recordTa,ColorUtils.BLUE);
//                                    logWriter.append("机械臂"+(moveDis)+"mm就位");//moveDis+10 最开始有一个10，所以加
//                                    UIUtils.updateTxA(recordTa,"机械臂"+(moveDis)+"mm就位"+"\r\n",true);
                                }else{
                                    break;
                                }
                            }
                            long st = System.currentTimeMillis();
                            UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                            serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                            revPos = serialConnection.serialRead(Constant.TIMEOUT);
                            long ed = System.currentTimeMillis();
                            UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                            UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                            int i = UIUtils.recordLog(logWriter,revPos, content, recordTa);
                            if (i == 0) {
                                minDis = moveDis;
                                break;
                            } else {
                                if (moveDis >= 200) {
                                    testOver = true;
                                    break;
                                }
                                moveDis += 10;
                            }
                        }//end while(1) //目的记录最小扫描距离或一直扫描不到

                        if (!testOver) {
                            moveDis += 10;
                            while (true) {
                                sendC4Str = Constant.DEPTHCMD + "1 " + moveDis; //dofone 1 0
                                pc2C4Rev = pc2C4.writeReadOneLine(sendC4Str);
                                UIUtils.logColorUpdate(logWriter, "机械臂移动到下一个位置:"+moveDis+"mm", recordTa,ColorUtils.BLUE);

                                /*logWriter.append("机械臂移动到下一个位置:"+moveDis+"mm");
                                UIUtils.updateTxA(recordTa,"机械臂移动到下一个位置:"+moveDis+"mm"+"\r\n",true);*/

                                if (pc2C4Rev.contains("ok")) {

//                                    logWriter.append("机械臂"+(moveDis)+"mm就位");//moveDis+10 最开始有一个10，所以加
//                                    UIUtils.updateTxA(recordTa,"机械臂"+(moveDis)+"mm就位"+"\r\n",true);

                                    UIUtils.logColorUpdate(logWriter, "机械臂"+(moveDis)+"mm就位", recordTa,ColorUtils.BLUE);
                                    long st = System.currentTimeMillis();
                                    UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                                    serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                                    revPos = serialConnection.serialRead(Constant.TIMEOUT);
                                    long ed = System.currentTimeMillis();
                                    UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                                    UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                                    int i = UIUtils.recordLog(logWriter,revPos, content,recordTa);
                                    if (i == 0) {
                                        if (moveDis >= 200) {
                                            maxDis = moveDis;
                                            break;
                                        }
                                        moveDis += 10;
                                        continue;//继续测试
                                    } else {//失败
                                        maxDis = moveDis;
                                        maxDis -= 10;
                                        break;
                                    }
                                } else {
                                    break doTest;
                                }
                            }//end while(2) //目的记录最大扫描距离或一直扫描不到

                        } //end if(!testOver)
                       // pc2Phone.writeReadOneLine(Constant.CLEAR); //清理屏幕
                        return resultLog(minDis, maxDis,recordTa,caseTxt, readCfgUtils);
                    } while (false);
                    RecordLog.Logger().error("PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev);
                    UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev+",C4机械臂移动就位距离失败", recordTa,ColorUtils.RED);
//                    logWriter.append("PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev);
//                    UIUtils.updateTxA(recordTa,"PC发送给C4的请求:" + sendC4Str + "," + "PC收到C4的【错误】回应数据包:" + pc2C4Rev+"\r\n",true);
                    return 2;
                } else {// backp0
//                    logWriter.append("C4机械臂原点就位失败");
//                    UIUtils.updateTxA(recordTa,"PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
//                            ",C4机械臂原点就位失败"+"\r\n",true);

                    UIUtils.logColorUpdate(logWriter, "PC发送给C4的请求:"+Constant.ORIGIN+",PC收到C4的【错误】回应数据包:"+pc2C4Rev+
                            ",C4机械臂原点就位失败", recordTa,ColorUtils.RED);
                    return 2;
                }

            } else { //end pc2Phone

                UIUtils.logColorUpdate(logWriter, "PC发送给Phone的请求:"+ (FileName==null?"generate code":Constant.SHOW+FileName) +",PC收到Phone的【错误】数据包:" + pc2PhoneRev +
                        ",显示条码失败", recordTa,ColorUtils.RED);

//                logWriter.append("显示条码失败");
//                UIUtils.updateTxA(recordTa,"PC发送给Phone的请求:"+ FileName +",PC收到Phone的【错误】数据包:"+pc2PhoneRev+
//                        ",显示条码失败"+"\r\n",true);
                return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            RecordLog.Logger().error("Try抛出的异常："+ e.getMessage());
            return 4;//特殊处理
        }
        else { //只为特殊情况的时候
            RecordLog.Logger().error("通讯异常，请内部处理！");
            return 4;
        }
    }



    private int resultLog(int minDis ,int maxDis,JEditorPane recordTa,String caseTxt,ReadCfgUtils readCfgUtils)throws IOException{
        int flag;
        if (Constant.CODE_39.equalsIgnoreCase(readCfgUtils.getCodeType())||Constant.CODE_128.equalsIgnoreCase(readCfgUtils.getCodeType())){
            flag = 1;
        }else{
            flag = 2;
        }

        //ParseXml xml = new ParseXml(MainFrame.getPath().split(Constant.CONFIG)[0]+File.separator+Constant.CASEXML);//放在这里不行。。
        if (maxDis - minDis < 80){

//            logWriter.append("测试所得景深有:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm");
//            UIUtils.updateTxA(recordTa,"测试所得景深有:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm"+"\r\n",true);
            UIUtils.logColorUpdate(logWriter, "测试所得景深:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm", recordTa,ColorUtils.BLUE);

//            logWriter.append("扫码设备的景深不满足80mm的要求");
//            UIUtils.updateTxA(recordTa,"扫码设备的景深不满足80mm的要求"+"\r\n",true);
//
//            logWriter.append("扫码设备的最大景深是:"+(maxDis-minDis)+"mm");
//            UIUtils.updateTxA(recordTa,"扫码设备的最大景深是:"+(maxDis-minDis)+"mm"+"\r\n",true);

            UIUtils.logColorUpdate(logWriter, "扫码设备的景深不满足80mm的要求", recordTa,ColorUtils.RED);
            UIUtils.logColorUpdate(logWriter, "扫码设备的最大景深是:"+(maxDis-minDis)+"mm", recordTa,ColorUtils.RED);

//            logWriter.append("Fail");
//            UIUtils.updateTxA(recordTa,"Fail"+"\r\n",true);

            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
            if (flag == 1){ //一维不通过的时候，记录
                if(minDis<maxDis)
                    cn.com.pax.utils.FileUtils.writeOptimalDepth(minDis,maxDis,flag);
            }
            //UIUtils.updateTxA(recordTa,"[==============    Test END    ==============]"+"\r\n",true);
            xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),minDis+"~" + maxDis);
            xml.writeXml();
            return 3;
        }else{

            cn.com.pax.utils.FileUtils.writeOptimalDepth(minDis,maxDis,flag);
//            logWriter.append("测试所得景深有:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm");
//            UIUtils.updateTxA(recordTa,"测试所得景深范围为:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm"+"\r\n",true);

            UIUtils.logColorUpdate(logWriter, "测试所得景深:"+minDis+"mm到"+maxDis+"mm,景深是:"+(maxDis-minDis)+"mm", recordTa, ColorUtils.BLUE);

            //logWriter.append("设备扫码的最大景深是:"+(maxDis-minDis) +"mm");
            //UIUtils.updateTxA(recordTa,"设备扫码的最大景深是:"+(maxDis-minDis) +"mm"+"\r\n",true);
//            logWriter.append("Pass");
//            UIUtils.updateTxA(recordTa,"Pass"+"\r\n",true);
            UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
            xml.addResult(caseTxt, "PASS", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),minDis + "~" + maxDis);
            xml.writeXml();
            return 0;
        }
    }

    //调试
    public void writeOptimalDepth(int minDis,int maxdis,String str) throws  IOException{
        String path =str.split(Constant.CONFIG)[0];
       //String path = MainFrame.getPath().split(Constant.CONFIG)[0];
        StringBuffer stringBuffer = new StringBuffer();
        File file =new File(path +File.separator+Constant.OPTIMADEPTH);
        if (!file.exists()) {
            stringBuffer.append("最小景深" + "   " + "最大景深"+"\r\n");
        }
        stringBuffer.append(minDis + "            " + maxdis + "\r\n");

        FileUtils.writeStringToFile(file, stringBuffer.toString(),
                "UTF-8", true);
    }

    //调试计算
    public int calcOptimalDepth(String str )throws IOException{
        String path =str.split(Constant.CONFIG)[0];
        File file =new File(path +File.separator+Constant.OPTIMADEPTH);
        if (file.exists()){
           List<String> list =FileUtils.readLines(file,"utf-8");
           List<Integer>minList = new ArrayList<>();
           List<Integer>maxList= new ArrayList<>();
           for (int i = 1; i < list.size(); i++) {
               System.out.println("的值是：---"+ list.get(i).trim().replaceAll(" +",",") + "，当前方法=calcOptimalDepth.DepthMoveWork()");
               minList.add(Integer.parseInt(list.get(i).trim().replaceAll(" +",",").split(",")[0].trim()));
               maxList.add(Integer.parseInt(list.get(i).trim().replaceAll(" +",",").split(",")[1].trim()));
              //System.out.println("list.get(i)的值是：---"+ list.get(i) + "，当前方法=calcOptimalDepth.DepthMoveWork()");
            }
            System.out.println("minList：---"+ Collections.max(minList) + "，当前方法=calcOptimalDepth.DepthMoveWork()");
            System.out.println("maxList：---"+ +Collections.min(maxList) + "，当前方法=calcOptimalDepth.DepthMoveWork()");
            System.out.println("(Collections.max(minList)+Collections.min(maxList))/2的值是：---"+ (Collections.max(minList)+Collections.min(maxList))/2 + "，当前方法=calcOptimalDepth.DepthMoveWork()");
            return (Collections.max(minList)+Collections.min(maxList))/2;
        }else{
            return 0;
        }
    }


//    public static void pubFun(ReadCfgUtils readCfgUtils ,String FileName,String content,String version,String errLevel,
//                              String precision,String charSet,String bg,String fg){
//        ReadExcelCfgUtils readExcelCfg = new ReadExcelCfgUtils();
//        boolean excelFlag = readExcelCfg.readExcel();
//        Map <String,String> excelCfgMap = readExcelCfg.getExcelCfg();
//        // if ()// 不想判断了
//        if("1".equals(readCfgUtils.getFlag())){
//            ImageInfo imageInfo = (ImageInfo)readCfgUtils.getClassType();
//            FileName =  imageInfo.getImgName();
//            content = imageInfo.getContent();
//        }else if("0".equals(readCfgUtils.getFlag())){
//            if (Constant.GENERTYPE.equals(readCfgUtils.getCodeType())){
//                QrCfgInfo qrCfgInfo = (QrCfgInfo)readCfgUtils.getClassType();
//                content = qrCfgInfo.getGenerateDate();
//                if(content.matches("\\[{2}[a-zA-Z0-9]{2}\\]{2}")){
//                    content = excelCfgMap.get(content.substring(content.indexOf("[[")+2,content.lastIndexOf("]]")));
//                }
//                version = qrCfgInfo.getVersion();
//                errLevel = qrCfgInfo.getErrLevel();
//                precision = qrCfgInfo.getPrecision();
//                charSet = qrCfgInfo.getCharSet();
//                bg = qrCfgInfo.getBackground();
//                fg = qrCfgInfo.getForeground();
//            }//预留其他
//        }
//    }

    public static void main(String[] args)throws Exception {
         String str = "E:\\IDEA WorkSpace\\PAX_BCTCTestCode\\银联认证--受理终端条码识读能力测试-百富计算机技术（深圳）有限公司-S900-2017-12-13-17-00-06\\config.txt";
         DepthMoveWork depthMoveWork = new DepthMoveWork();
//         depthMoveWork.writeOptimalDepth(60,80, str);
//         depthMoveWork.writeOptimalDepth(50,90, str);
//         depthMoveWork.writeOptimalDepth(70,100, str);
        depthMoveWork. calcOptimalDepth(str);
    }

}
    