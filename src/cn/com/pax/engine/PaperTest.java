package cn.com.pax.engine;

import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.PaperInfo;
import cn.com.pax.log.RecordLog;
import cn.com.pax.utils.ColorUtils;
import cn.com.pax.utils.ReadCfgUtils;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import java.util.Map;

/**
 * 手动测试
 * @author luohl
 * @create 2018-01-11-19:25
 */
public class PaperTest extends BaseEngine implements BaseMoveWork{
    @Override
    public int MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa) {
        super.readCFG(readCfgUtils,caseTxt,recordTa);
        PaperInfo paperInfo = (PaperInfo) readCfgUtils.getPaper();
        System.out.println(paperInfo.getContend());
        if (paperInfo.getContend() == null){
            RecordLog.Logger().info("纸质测试脚本中的内容为空！，请知悉");
            return 6;
        }
        content = paperInfo.getContend();
        if (content.matches("\\[{2}[a-zA-Z0-9]+\\]{2}")) {
            content = excelCfgMap.get(content.substring(content.indexOf("[[") + 2, content.lastIndexOf("]]")));
        }
        RecordLog.Logger().info("【纸质测试】" );
        if(serialConnection != null && pc2C4 != null && pc2Phone != null ){
            String revPos;
            if (Integer.parseInt( MainFrame.jingShenJtf.getText().trim()) == -1

                    ){
                RecordLog.Logger().info("没有测试景深或者没有输入最优景深的大小！，请知悉");
                return 5;
            }else{
                try{
                    serialConnection.readExtraData(Constant.READEXTRATIME, Constant.MAXTIME);
                    UIUtils.logColorUpdate(logWriter, "请扫码", recordTa,ColorUtils.BLACK);
                    long st = System.currentTimeMillis();
                    UIUtils.logColorUpdate(logWriter, "从发送命令（POS）用时："+(st)+"ms", recordTa,ColorUtils.OTHE);
                    serialConnection.serailWrite(Constant.SENDPOS.getBytes());
                    revPos = serialConnection.serialRead(Constant.TIMEOUT);
                    long ed = System.currentTimeMillis();
                    UIUtils.logColorUpdate(logWriter, "从接受命令（POS）用时："+(ed)+"ms", recordTa,ColorUtils.OTHE);
                    UIUtils.logColorUpdate(logWriter, "从发送命令到接收命令（POS）用时："+(ed-st)+"ms", recordTa,ColorUtils.OTHE);
                    RecordLog.Logger().info("原始条码显示内容:" + content + "," + "扫码设备返回原始数据:" + revPos);
                    UIUtils.logColorUpdate(logWriter, "原始条码显示内容:" + content + "," + "扫码设备返回原始数据:" + revPos, recordTa,ColorUtils.BLACK);
                    if ("ffff".equalsIgnoreCase(revPos)){
                        UIUtils.logColorUpdate(logWriter,"超时读取条码失败,返回的数据:" + revPos, recordTa, ColorUtils.RED);
                        UIUtils.logColorUpdate(logWriter,"Fail", recordTa, ColorUtils.RED);
                        return 3;
                    }else{
                        if (content.equals(revPos)){
                            UIUtils.logColorUpdate(logWriter,"扫码验证通过!", recordTa, ColorUtils.BLUE);
                            UIUtils.logColorUpdate(logWriter,"Pass", recordTa, ColorUtils.BLUE);
                            return 0;
                        }else{
                            UIUtils.logColorUpdate(logWriter, "扫码验证失败,返回的数据:" + revPos, recordTa,ColorUtils.RED);
                            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
                            return 3;
                        }
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

    