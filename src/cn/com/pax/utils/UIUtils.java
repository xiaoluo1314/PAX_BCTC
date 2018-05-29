package cn.com.pax.utils;

import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * UI公共类
 *
 * @author luohl
 * @create 2017-12-05-20:19
 */
public class UIUtils {

    public static void ChangeWindowDisplay(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Image getLogo(Window window,String path){
        if (path != null) {
            return window.getToolkit().getImage("icon/logo.png");
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }
    public static ImageIcon createImageIcon(String path) {
        // java.net.URL imgURL = FileUtils.class.getResource(path);
        if (path != null) {
            return new ImageIcon(path);
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }
    public static Font setFont(int size){
        return new Font("宋体",Font.PLAIN, size);
    }
    public static void updateTable(JTable table,String str,int i){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    table.getModel().setValueAt(str,i,2);
                }
            });
    }

    public static  void updateTxf(final JTextField textField, final String string) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                textField.setText(string);
            }
        });
    }
    public static void updateTxA(final JTextArea area, final String string) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                area.setText(string);
            }
        });
    }

    public static void updateTxA(final JTextArea area, final String string, final boolean isAppend) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(isAppend)
                    area.append(string);
                else
                    area.setText(string);
            }
        });
    }
    public static void updateJtP(final JEditorPane editPane, final String string) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                HTMLEditorKit htmlEditorKit = (HTMLEditorKit) editPane.getEditorKit();
                Document doc = editPane.getDocument();
                try {
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength(), string, 0, 0, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void clearJtP(final JEditorPane editPane) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                HTMLEditorKit htmlEditorKit = (HTMLEditorKit) editPane.getEditorKit();
                Document doc = editPane.getDocument();
                try {
                    doc.remove(0,doc.getLength());
                   // editPane.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     *
     * @param contend
     * @return 0 成功，1 数据不匹配 2
     * @throws IOException
     */
    public static int recordLog(LogWriter logWriter, String revPos , String contend, JEditorPane recordTa)throws IOException {
//        logWriter.append("请扫码");
//        UIUtils.updateTxA(recordTa,"请扫码"+"\r\n",true);


        UIUtils.logColorUpdate(logWriter, "请扫码", recordTa,ColorUtils.BLACK);

//        logWriter.append("扫码设备返回原始数据:"+revPos);
//        UIUtils.updateTxA(recordTa,"扫码设备返回原始数据:"+revPos+"\r\n",true);

        if ("ffff".equalsIgnoreCase(revPos)){
            UIUtils.logColorUpdate(logWriter, "本次扫码读取超时:"+revPos, recordTa, ColorUtils.RED);
            UIUtils.logColorUpdate(logWriter, "扫码设备实际应返回:"+contend, recordTa,ColorUtils.RED);
//            logWriter.append("本次扫码失败");
//            logWriter.append("设备返回:FFFF");
//            logWriter.append("扫码设备应返回:");
//            UIUtils.updateTxA(recordTa,"本次扫码失败"+"\r\n",true);
//            UIUtils.updateTxA(recordTa,"设备返回:FFFF"+"\r\n",true);
//            UIUtils.updateTxA(recordTa,"扫码设备应返回:"+contend+"\r\n",true);
            return 2;
        }
        if (!contend.equals(revPos)){
            System.out.println("xxxx"+revPos);
            UIUtils.logColorUpdate(logWriter, "扫码成功，但返回数据错误,收到数据为:"+ revPos, recordTa,ColorUtils.RED);
            UIUtils.logColorUpdate(logWriter, "期望返回数据:"+contend, recordTa,ColorUtils.RED);

//            logWriter.append("扫码成功，但返回数据错误"+ revPos);
//            logWriter.append("期望返回数据:"+contend);
//            UIUtils.updateTxA(recordTa,"扫码成功，但返回数据错误:"+ revPos+"\r\n",true);
//            UIUtils.updateTxA(recordTa,"期望返回数据:"+contend+"\r\n",true);
            return 1;
        }else{
//            logWriter.append("本次扫码成功");
//            UIUtils.updateTxA(recordTa,"本次扫码成功"+"\r\n",true);
            UIUtils.logColorUpdate(logWriter, "本次扫码成功", recordTa,ColorUtils.BLUE);
            return 0;
        }
    }
    public static int testResult(LogWriter logWriter, JEditorPane recordTa ,int ret)throws IOException{
        if (ret == 0){
//            logWriter.append("Pass");
//            UIUtils.updateTxA(recordTa,"Pass\n\r",true);
            UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
            return 0;
        }else{
//            logWriter.append("Fail");
//            UIUtils.updateTxA(recordTa,"Fail\n\r",true);
            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
            return 3;
        }
    }

    public static void logUpdate(LogWriter logWriter, String str, JTextArea recordTa) throws IOException {
        logWriter.append(str);
        UIUtils.updateTxA(recordTa, str + "\r\n", true);
    }


    public static void recordDetail(ParseXml xml,String caseTxt, LogWriter logWriter, List failRecord)throws IOException{
        if (failRecord.size() > 0){
            xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1), StringUtils.join(failRecord.toArray(), ","));
            xml.writeXml();
        }else{
            xml.addResult(caseTxt, "PASS", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1),"");
            xml.writeXml();
        }
    }
    public static void logColorUpdate(LogWriter logWriter, String str, JEditorPane recordTa,ColorUtils colorUtils)throws IOException{
        String str1 = null;
        switch (colorUtils){
            case RED:
                str1 = "<p style=\"color:FF0000\">" + str + "</p>";
            break;
            case WHITE:
                str1 =  "<p style=\"color:808080\">" + str + "</p>";
                break;
            case BLACK:
                str1 =  "<p style=\"color:000000\">" + str + "</p>";
                break;
            case BLUE:
                str1 =  "<p style=\"color:008000\">" + str + "</p>";
                break;
            case OTHE:
                str1 = "<p style=\"color:FF00FF\">" + str + "</p>";
                break;
        }
        logWriter.append(str1);
        UIUtils.updateJtP(recordTa, str1);
    }
    public static void startEnd(String str, JEditorPane recordTa){
        String str1 = "<p style=\"color:000000\">" + str + "</p>";
        UIUtils.updateJtP(recordTa, str1);
    }

    public static int getResult(List<Integer> failList, LogWriter logWriter, JEditorPane recordTa)throws IOException{
        if (failList.size() > 0){
            UIUtils.logColorUpdate(logWriter, "Fail", recordTa,ColorUtils.RED);
            return 3;
        }else {
            UIUtils.logColorUpdate(logWriter, "Pass", recordTa,ColorUtils.BLUE);
            return 0;
        }
    }


}
    