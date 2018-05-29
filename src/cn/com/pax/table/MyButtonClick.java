package cn.com.pax.table;

import cn.com.pax.Constant;
import cn.com.pax.barcode.GenCodeNew;
import cn.com.pax.display.AboutSoft;
import cn.com.pax.display.MainFrame;
import cn.com.pax.display.MyOptionPane;
import cn.com.pax.engine.BaseEngine;
import cn.com.pax.engine.BaseMoveWork;
import cn.com.pax.engine.ReadEngineCfg;
import cn.com.pax.entity.ImageInfo;
import cn.com.pax.entity.StopMove;
import cn.com.pax.entity.TableData;
import cn.com.pax.log.LogWriter;
import cn.com.pax.log.ParseXml;
import cn.com.pax.log.RecordLog;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.report.EnvInfo;
import cn.com.pax.report.ReportCreate;
import cn.com.pax.tree.CustTreeModel;
import cn.com.pax.tree.UpdateTestResult;
import cn.com.pax.tree.CheckBoxTreeNodeSelectionListener;
import cn.com.pax.utils.ReadCfgUtils;
import cn.com.pax.utils.ReportMap;
import cn.com.pax.utils.TimeUtils;
import cn.com.pax.utils.UIUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 所有按钮的点击事件
 *
 * @author luohl
 * @create 2017-12-11-11:33
 */
public class MyButtonClick implements ActionListener {
   // private List<Integer> list = new ArrayList<>();
    private JTree jTree;
    private  JTable jTable;
    private SerialOperation  serialOperation;
    private JFrame jFrame ;
    private List<String>caseTxtList = new LinkedList<>();
    private Map<String,Integer>rowCaseTextMap = new HashMap<>();//反过来，因为都是唯一
    private LogWriter logWriter;

    private JButton exeImageButton;         //开始执行
    private JButton stopImageButton;        //停止执行
    private JButton clearScreen;            //清理屏幕
    private JButton allCheck;               //全部选择
    private JButton inverseCheck;           //全部不选
    private JButton partCheck;              //部分选择
    private JButton inPartCheck;            //部分不选
    private JButton allDelete;              //全部删除
    private JButton software;               //软件信息
    private JButton report;                 //生成报告
    private JTextField testerField;
    private JTextField testLocField;
    private JTextField testEnvField;
    private JTextField remarkField;

    public void setSerialOperation(SerialOperation serialOperation) {
        this.serialOperation = serialOperation;
    }

    //    private JTextArea recordTa;
    private JEditorPane recordTa;
    private ReadCfgUtils readCfgUtils;

//    public void setRecordTa(JTextArea recordTa) {
//        this.recordTa = recordTa;
//    }
    public void setRecordTa(JEditorPane recordTa) {
        this.recordTa = recordTa;
    }

//    public void setJingShenJtf(JTextField jingShenJtf) {
//        this.jingShenJtf = jingShenJtf;
//    }

    public void setReport(JButton report) {
        this.report = report;
    }

    public void setTesterField(JTextField testerField) {
        this.testerField = testerField;
    }

    public void setTestLocField(JTextField testLocField) {
        this.testLocField = testLocField;
    }

    public void setTestEnvField(JTextField testEnvField) {
        this.testEnvField = testEnvField;
    }

    public void setRemarkField(JTextField remarkField) {
        this.remarkField = remarkField;
    }

    public void setLogWriter(LogWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void setExeImageButton(JButton exeImageButton) {
        this.exeImageButton = exeImageButton;
    }

    public void setStopImageButton(JButton stopImageButton) {
        this.stopImageButton = stopImageButton;
    }

    public void setClearScreen(JButton clearScreen) {
        this.clearScreen = clearScreen;
    }

    public void setAllCheck(JButton allCheck) {
        this.allCheck = allCheck;
    }

    public void setInverseCheck(JButton inverseCheck) {
        this.inverseCheck = inverseCheck;
    }

    public void setPartCheck(JButton partCheck) {
        this.partCheck = partCheck;
    }

    public void setInPartCheck(JButton inPartCheck) {
        this.inPartCheck = inPartCheck;
    }

    public void setAllDelete(JButton allDelete) {
        this.allDelete = allDelete;
    }

    public void setSoftware(JButton software) {
        this.software = software;
    }

    private static final MyButtonClick myClick = new MyButtonClick();
    private MyButtonClick(){}
    public static MyButtonClick getInstance(){
        return  myClick;
    }

   // public Map<String,List<String>> depthMap = new LinkedHashMap<>();//景深测试
    //public Map<String,List<String>> codeMap = new LinkedHashMap<>(); //码制测试
    // public Map<String,List<String>> angelMap = new LinkedHashMap<>();//角度测试

    public void setjFrame(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    public void setjTree(JTree jTree) {
        this.jTree = jTree;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().trim()){
            case Constant.STARTEXE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        exeProcess();
                    }
                }).start();
                break;
            case Constant.STOPTEST:
                stopMove();
                break;
            case Constant.CLEARSCREE:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        testDepth();
                    }
                }).start();
                break;
            case Constant.ALLSELECT:
                all_Select();
                break;
            case Constant.NOSELECT:
                no_Select();
                break;
            case Constant.PARTSELECT:
                partSelect();
                break;
            case Constant.NOPARTSELECT:
                partNoSelect();
                break;
            case  Constant.ALLDELETE:
                all_Delete();
                break;
            case Constant.SOFTWAREINFO:
                new AboutSoft();
                break;
            case Constant.REPORTBTN:
                generateReport();
                break;
        }
    }
    private void generateReport(){
        String path = MainFrame.getPath();
        //System.out.println("path=" + path );
        try {
            List <String> config = FileUtils.readLines(new File(path),"utf-8");
            EnvInfo info = new EnvInfo(testerField.getText() == null? "" : testerField.getText(),
                    testLocField.getText() == null ? "" : testLocField.getText(),
                    testEnvField.getText() == null ? "" : testEnvField.getText(), config.get(1),
                    config.get(2), remarkField.getText() == null ? "" : remarkField.getText());
            ReportMap reportMap = new ReportMap();
            String name = path.split(Constant.CONFIG)[0] + File.separator +TimeUtils.getDateTimeFormat(new Date()).replace(":","-").replace(" ","-") + "-" + config.get(2) ;
           // System.out.println(path.split(Constant.CONFIG)[0] + File.separator + Constant.CASEXML);
            ReportCreate.build(reportMap.getReportMap(), info, path.split(Constant.CONFIG)[0] + File.separator + Constant.CASEXML,
                    name + ".docx");
            MyOptionPane.showMessage(jFrame,"生成报告成功，请知悉!", "生成报告");
        } catch (Exception e) {
            e.printStackTrace();
            MyOptionPane.showMessage(jFrame,"生成报告失败，请知悉!", "生成报告");
        }
    }
    private void testDepth(){
        clearScreen.setEnabled(false);
        all_Delete();
        int id = 0;
        Set<TableData> tabelSetSelect = new LinkedHashSet<TableData>();
        tabelSetSelect.clear();
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        String path = MainFrame.getPath().split(Constant.CONFIG)[0]+File.separator+Constant.CASEXML;
        ParseXml xml = new ParseXml(path);
        Map<String,String> case2Result = xml.queryCaseResult();
        String str = Constant.SCRIPTPATH + File.separator+"JSCS";
        List<String> fileNameList = cn.com.pax.utils.FileUtils.getFileName(str);
        for (String s : fileNameList) {
            TableData tableData = new TableData();
            tableData.setLeafName(s);
            tableData.setSelect(false);
            tableData.setState(CustTreeModel.convertInt(case2Result.get(s)));
            tabelSetSelect.add(tableData);
        }

        for (TableData tableData1 : tabelSetSelect) {
            if(CustTreeModel.getResult(MainFrame.state).contains(CustTreeModel.convertStr(tableData1.getState()))){
                model.addRow(new Object[]{tableData1.isSelect(), tableData1.getLeafName(), "UNTEST", ++id});
            }
        }
        all_Select();
        exeProcess();
        all_Delete();
        clearScreen.setEnabled(true);
    }
    private void all_Select(){
        allCheck.setEnabled(false);
        for(int i=0; i < jTable.getRowCount(); i++) {
            jTable.setValueAt(true, i, 0);
        }
        jTable.repaint();
        allCheck.setEnabled(true);
    }
    private void no_Select(){
        inverseCheck.setEnabled(false);
        for(int i=0; i < jTable.getRowCount(); i++) {
            jTable.setValueAt(false, i, 0);
        }
        jTable.repaint();
        inverseCheck.setEnabled(true);
    }
    private void all_Delete(){
        allDelete.setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        CheckBoxTreeNodeSelectionListener.tabelSetSelect.clear();
        jTable.repaint();
        allDelete.setEnabled(true);
    }
    public void partSelect(){
        //partCheck.setEnabled(false);
//        for (int i : jTable.getSelectedRows()) {
//            System.out.println("jTable.getSelectedRows()的值是：---"+  i+ "，当前方法=partSelect.MyButtonClick()");
//        }
        for(int rowIndex : jTable.getSelectedRows()){
            jTable.setValueAt(true, rowIndex, 0);
        }
        jTable.repaint();
       // partCheck.setEnabled(true);
    }
    private void partNoSelect(){
        //inPartCheck.setEnabled(false);
        for(int rowIndex : jTable.getSelectedRows()){
            jTable.setValueAt(false, rowIndex, 0);
        }
        jTable.repaint();
       // inPartCheck.setEnabled(true);
    }
    private boolean openAlL(){
        System.out.println("serialOperation的值是：---"+ serialOperation + "，当前方法=openAlL.MyButtonClick()");
        if (!SocketOperation.getPc2C4().isConnect()){
            if (serialOperation.openSerial()&&SocketOperation.getPc2C4().openConn()&&SocketOperation.openPhoneSocKet()){
                return true;
            }else{
                RecordLog.Logger().error("串口打开"+(serialOperation.isOpen()?"成功":"失败"));
                RecordLog.Logger().error("PC与C4通讯"+(SocketOperation.getPc2C4().isConnect()?"成功":"失败"));
                RecordLog.Logger().error("PC与Phone通讯"+(SocketOperation.isPc2PhoneConn()?"成功":"失败"));
                return  false;
            }
        }else{
            if(serialOperation.openSerial()&& SocketOperation.openPhoneSocKet()){
                return true;
            }else{
                RecordLog.Logger().error("串口打开"+(serialOperation.isOpen()?"成功":"失败"));
                RecordLog.Logger().error("PC与C4通讯"+(SocketOperation.getPc2C4().isConnect()?"成功":"失败"));
                RecordLog.Logger().error("PC与Phone通讯"+(SocketOperation.isPc2PhoneConn()?"成功":"失败"));
                return  false;
            }
        }
    }
    private void closeAll(){
        serialOperation.closeSerial();
        //SocketOperation.pc2C4Close();
        SocketOperation.getPc2C4().closeConn();
        SocketOperation.pc2PhoneClose();
    }
    private void getScript(){
        caseTxtList.clear();
        rowCaseTextMap.clear();
         DefaultTableModel tableModel = (DefaultTableModel)jTable.getModel();
         int rowCount = tableModel.getRowCount();
         if (rowCount >0 ){
             //获取用例集
             for (int i = 0; i < rowCount; i++) {
                 if( Boolean.parseBoolean(String.valueOf(tableModel.getValueAt(i, 0)))) {
                     caseTxtList.add(tableModel.getValueAt(i, 1).toString().trim());
                     rowCaseTextMap.put(tableModel.getValueAt(i, 1).toString().trim(),i);
                 }
             }

             RecordLog.Logger().info("你选择了"+caseTxtList.size()+"个用例进行测试，请知悉!");
         }else{
             //MyOptionPane.showMessage(jFrame,"没有选择树中的用例集,请选择后,再测试！","开始测试");
             RecordLog.Logger().error("没有选择树中的用例集,请选择后,再测试!");
             return;
         }
    }

//    private void readScript(){
//        for (String castTxt : caseTxtList) {
//            try {
//                List<String> strings = FileUtils.readLines(new File(Constant.SCRIPTPATH +castTxt.substring(0,4)+File.separator+castTxt),
//                        "utf-8");
//                switch (strings.get(0)){
//                    case Constant.DEPTH:
//                        save2Map(strings,depthMap);
//                        break;
//                    case Constant.CODE:
//                        save2Map(strings,codeMap);
//                        break;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("depthMap：---"+ depthMap + "，当前方法=readScript.MyButtonClick()");
//        System.out.println("codeMap：---"+ codeMap + "，当前方法=readScript.MyButtonClick()");
//    }
//    private void save2Map(List<String> list ,Map<String, List<String>> map){
//        if (map.containsKey(list.get(0))){
//            map.get(list.get(0)).add(list.get(1).split(":")[1]+":"+list.get(2).split(":")[1]);//这个根据配置相当于做特定处理
//        }else{
//            List<String> temp = new ArrayList<>();
//            temp.add(list.get(1).split(":")[1]+":"+list.get(2).split(":")[1]);
//            map.put(list.get(0),temp);
//        }
//    }
    private void stopMove(){
        stopImageButton.setEnabled(false);
        StopMove.setRunning(false);
        MyOptionPane.showMessage(jFrame,"停止成功，请等待当前运动结束即可,请知悉!", "停止测试");
        stopImageButton.setEnabled(true);
    }

    private void exeProcess(){
      //  new Thread(new Runnable() {
           // @Override
           // public void run() {
                StopMove.setRunning(true);
              //  UIUtils.updateTxA(recordTa,"");
                exeImageButton.setEnabled(false);
                serialOperation = SerialOperation.getInstance();
                if (!openAlL()){
                    closeAll();
                    exeImageButton.setEnabled(true);
                    MyOptionPane.showMessage(jFrame,"通讯异常，请检查！","开始测试");
                    return;
                }
                getScript();
                ReadEngineCfg.readEng();
                Properties properties = ReadEngineCfg.getProperties();
                int ret;
                if (caseTxtList.size() <= 0){
                    closeAll();
                    exeImageButton.setEnabled(true);
                    MyOptionPane.showMessage(jFrame,"没有选择用例进行测试，请知悉!", "选择用例");
                    return;
                }else {
                    for (String castTxt : caseTxtList) {
                        if (StopMove.isRunning()){
                            //bymodiid
                            UIUtils.clearJtP(recordTa);
                           // UIUtils.updateTxA(recordTa,"");//清除日志,为了保证用户执行停止，能够保证最后的日志保留在屏幕上，放在判断里
                            try {
                                readCfgUtils = new ReadCfgUtils(castTxt);
                               if(readCfgUtils.readCfg()) {
                                   if (!properties.containsKey(readCfgUtils.getTestEngine())) {
                                       closeAll();
                                       exeImageButton.setEnabled(true);
                                       //UIUtils.updateTable(jTable,"FAIL",rowCaseTextMap.get(castTxt));
                                       RecordLog.Logger().error(readCfgUtils.getTestEngine() + "没有对应的业务逻辑测试");
                                       MyOptionPane.showMessage(jFrame, "没有对应的业务逻辑测试，请知悉", "开始测试");
                                       return;

                                   }
                                   BaseMoveWork baseMoveWork = getRealObject(properties.getProperty(readCfgUtils.getTestEngine()));
                                   if (baseMoveWork == null) {
                                       closeAll();
                                       exeImageButton.setEnabled(true);
                                       //UIUtils.updateTable(jTable,"FAIL",rowCaseTextMap.get(castTxt));
                                       RecordLog.Logger().error("[" + readCfgUtils.getTestEngine() + "]" + "baseMoveWork == null");
                                       MyOptionPane.showMessage(jFrame, "创建类失败，请知悉", "开始测试");
                                       return;
                                   }
                                   {
                                       String path = MainFrame.getPath().split(Constant.CONFIG)[0];
                                       int flag;
                                       if (Constant.CODE_39.equalsIgnoreCase(readCfgUtils.getCodeType()) || Constant.CODE_128.equalsIgnoreCase(readCfgUtils.getCodeType())) {
                                           flag = 1;
                                       } else {
                                           flag = 2;
                                       }
                                       int x = cn.com.pax.utils.FileUtils.calcOptimalDepth(path, flag);
                                       if (!MainFrame.isInputChx.isSelected())
                                           UIUtils.updateTxf(MainFrame.jingShenJtf, x + "");//更新景深
                                    }
                                   UIUtils.updateTable(jTable,"RUNNING",rowCaseTextMap.get(castTxt));
                                   ret = baseMoveWork.MoveWork(castTxt, readCfgUtils, recordTa);
                                   RecordLog.Logger().info("["+ readCfgUtils.getTestEngine()+"]"+"ret == "+ret);
                                   int ret2 = updateTableResult(ret, rowCaseTextMap.get(castTxt),castTxt,recordTa, readCfgUtils);
                                   if (ret2 == 100){
                                       MyOptionPane.showMessage(jFrame,"PC与Phone通讯失败！请知悉","开始测试");
                                       break;
                                   }else if (ret2 == 101){
                                       MyOptionPane.showMessage(jFrame,"PC与C4通讯失败！请知悉","开始测试");
                                       break;
                                   }else if (ret2 == 102){
                                       MyOptionPane.showMessage(jFrame,"通讯失败！请知悉","开始测试");
                                       break;
                                   }else if (ret2 == 103){
                                       MyOptionPane.showMessage(jFrame,"没有设置景深或者景深没有测试，请知悉","开始测试");
                                       break;
                                   }else if ( ret2 == 104){
                                       MyOptionPane.showMessage(jFrame,"烙铁，扎心了，没有调试配置，你也能调试，那么你真牛，赶快去配置一下，再测试","开始测试");
                                       break;
                                   }else if(ret2 == 105){
                                       MyOptionPane.showMessage(jFrame,"亮度设置失败","开始测试");
                                       break;
                                   }else if(ret2 == 106){
                                       MyOptionPane.showMessage(jFrame,"纸质测试失败","开始测试");
                                       break;
                                   }
                               }else{
                                   closeAll();
                                   exeImageButton.setEnabled(true);
                                   RecordLog.Logger().error(castTxt+"配置有误，请仔细检查配置");
                                   MyOptionPane.showMessage(jFrame,castTxt +"配置有误，请仔细检查配置","开始测试");
                                   return;
                               }
                            } catch (Exception e) {
                                closeAll();
                                exeImageButton.setEnabled(true);
                                e.printStackTrace();
                            }finally {
                               // closeAll();
                               // exeImageButton.setEnabled(true);

//                                if (Constant.GENERTYPE.equals(readCfgUtils.getClassType())){
//                                    GenCodeNew.clearQrCode(SocketOperation.getPc2Phone());
//                                }else if (Constant.CODE_128.equals(readCfgUtils.getClassType())){
//                                    GenCodeNew.clearCode128(SocketOperation.getPc2Phone());
//                                }else if (Constant.CODE_39.equals(readCfgUtils.getClassType())){
//                                    GenCodeNew.clearCode39(SocketOperation.getPc2Phone());
//                                }
                            }

                        }
                    }//end for
                }
                closeAll();
                exeImageButton.setEnabled(true);
                if(!StopMove.isRunning()) {
                    closeAll();
                    exeImageButton.setEnabled(true);
                    MyOptionPane.showMessage(jFrame, "用户停止测试，请知悉！", "开始测试");
                    return;
                }
           // }
       // }).start();
    }
    private int updateTableResult(int ret ,int i ,String caseTxt,JEditorPane recordTa,ReadCfgUtils readCfgUtils)throws Exception{
        String path = MainFrame.getPath().split(Constant.CONFIG)[0];
        int flag;
        if (Constant.CODE_39.equalsIgnoreCase(readCfgUtils.getCodeType())||Constant.CODE_128.equalsIgnoreCase(readCfgUtils.getCodeType())){
            flag = 1;
        }else{
            flag = 2;
        }
        int x = cn.com.pax.utils.FileUtils.calcOptimalDepth(path,flag);
        if (!MainFrame.isInputChx.isSelected())
            UIUtils.updateTxf(MainFrame.jingShenJtf,x +"");//更新景深
        //ParseXml xml = new ParseXml(MainFrame.getPath().split(Constant.CONFIG)[0]+File.separator+Constant.CASEXML);
        if (ret == 0 ){
            //更新表格
            //jTable.getModel().setValueAt("PASS",i,2);
            UIUtils.updateTable(jTable,"PASS",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,1);//成功
            //xml.addResult(caseTxt, "PASS",  logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
           // xml.writeXml();
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            //UIUtils.updateJtP(recordTa,"<p style=\"color:000000\">[==============    Test END    ==============]</p>");
            //UIUtils.updateTxA(recordTa,"[==============    Test END    ==============]"+"\r\n",true);
           // delayTime();
        }else if(ret == 1){
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);
            //xml.addResult(caseTxt, "FAIL",  logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
            //xml.writeXml();
            //MyOptionPane.showMessage(jFrame,"PC与Phone通讯失败！请知悉","开始测试");
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 100;
        }else if (ret == 2){
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);
           // xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
           // xml.writeXml();
            //MyOptionPane.showMessage(jFrame,"PC与C4通讯失败！请知悉","开始测试");
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 101;
        }else if(ret == 4) {
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);
           // MyOptionPane.showMessage(jFrame,"通讯失败！请知悉","开始测试");
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 102;
        } else if(ret == 5) {
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree, caseTxt,2);
            //xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
            //xml.writeXml();
            //MyOptionPane.showMessage(jFrame,"没有设置景深或者景深没有测试，请知悉","开始测试");
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 103;
        }else if(ret == 6) {
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree, caseTxt,2);
           // xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
           // xml.writeXml();
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 104;
        }else if(ret == 7){
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 105;
        }else if(ret == 8){
            exeImageButton.setEnabled(true);
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            return 106;
        }
        else {
            //fail
            UIUtils.updateTable(jTable,"FAIL",i);
            UpdateTestResult.updateTestResult(jTree,caseTxt,2);//失败
            //xml.addResult(caseTxt, "FAIL", logWriter.getLogFileName().substring(logWriter.getLogFileName().lastIndexOf("\\")+1));
           // xml.writeXml();
            UIUtils.startEnd("[==============    Test END    ==============]",recordTa);
            //delayTime();
        }
        return 0;
    }
    @Nullable
    private BaseMoveWork getRealObject(String className) {
        try {
            if(ReadEngineCfg.getNameToRealObject().containsKey(className))
                return ReadEngineCfg.getNameToRealObject().get(className);
            else {
                Class cls = Class.forName(className);
                BaseMoveWork basicOperation = (BaseMoveWork)cls.newInstance();
                ReadEngineCfg.getNameToRealObject().put(className, basicOperation);
                return basicOperation;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //方便看每次执行后的结果，可要可不要，毕竟表格中已有结果
    private void delayTime()throws InterruptedException{
        Thread.sleep(1000);
    }
}