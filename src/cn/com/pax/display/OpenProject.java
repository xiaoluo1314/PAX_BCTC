package cn.com.pax.display;

import cn.com.pax.Constant;
import cn.com.pax.log.ParseXml;
import cn.com.pax.utils.FileUtils;
import cn.com.pax.utils.GBC;
import cn.com.pax.utils.UIUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 打开一个已存在的项目
 *
 * @author luohl
 * @create 2017-12-06-10:40
 */
public class OpenProject extends JDialog implements ActionListener{

    //private JButton openButton;
    private JButton importButton;
    //private JFileChooser fileChooser;
    private JComboBox<String> dirPath;

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource().equals(openButton)){
//           // System.out.println("e.getSource()的值是：---"+ e.getSource() + "，当前方法=actionPerformed.OpenProject()");
//            fileChooser = new JFileChooser(new File("."));
//            fileChooser.setDialogTitle("选择工程");
//            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            fileChooser.setFileFilter(new MyFileFilter());
//            fileChooser.setCurrentDirectory(new File("./"));
//            fileChooser.setAcceptAllFileFilterUsed(true);
//            int ret = fileChooser.showOpenDialog(this);
//            if(ret != JFileChooser.APPROVE_OPTION){
//                return;
//            }
//            File projectDir = fileChooser.getSelectedFile();
//            int i;
//            for(i = 0; i < dirPath.getItemCount(); i++ ) {
//                if(projectDir.getAbsolutePath().equals(String.valueOf(dirPath.getItemAt(i)))){
//                    break;
//                }
//            }
//            if(i == dirPath.getItemCount()) {
//                dirPath.addItem(projectDir.getAbsolutePath());
//            }
//            dirPath.setSelectedIndex(i);
//
//        }else
//
            if (e.getSource().equals(importButton)){
            //Todo 获取当前选择的工程目录，然后是解析xml
            if (dirPath.getSelectedItem() == null|| "none".equals(dirPath.getSelectedItem().toString())){
                MyOptionPane.showMessage(this,"没有工程项目，请知悉!","打开工程");
                return;
            }
            java.util.List<String> typeList = new ArrayList<>();
            Map<String, List<String>> caseMap = new HashMap<>();

            java.util.List<String> typeList2 = new ArrayList<>();
            Map<String, List<String>> caseMap2 = new HashMap<>();

            String projectPath  = dirPath.getSelectedItem().toString();
            //String dirStr = projectPath.substring(projectPath.lastIndexOf("\\")+1);
            System.out.println("projectPath的值是：---"+ projectPath + "，当前方法=actionPerformed.OpenProject()");
            //System.out.println("dirStr的值是：---"+ dirStr + "，当前方法=actionPerformed.OpenProject()");
            //创建对象
            String xmlPath = projectPath+File.separator+Constant.CASEXML;
            ParseXml xml = new ParseXml(xmlPath);
            Map<String,String> case2Result = xml.queryCaseResult();
            Map<String, java.util.List<String>>case2LogResult = xml.queryCaseLogResult();
           // System.out.println("case2Result的值是：---"+ case2Result + "，当前方法=actionPerformed.OpenProject()");
            // 先备份，后面合适在删除
            File file  = new File(xmlPath);
            // file.delete();
            File fileBak  = new File(projectPath+File.separator+Constant.CASEXMLBAK);
            file.renameTo(fileBak);
            FileUtils.xmlConfig("." + File.separator + Constant.FIRSTDIR + File.separator + Constant.DEBUG + File.separator + Constant.SECONDDIR1,typeList, caseMap);
            FileUtils.xmlConfig("." + File.separator + Constant.FIRSTDIR + File.separator + Constant.DEBUG + File.separator + Constant.SECONDDIR2,typeList2, caseMap2);

           // Document document = ParseXml.createDocument(dirStr, Constant.FIRSTDIR, Constant.SECONDDIR2, FileUtils.getTypeList(), FileUtils.getCaseMap(),typeList2, caseMap2);
            try {
                Document document = ParseXml.createDocument();
                Element firstEl = ParseXml.createFirstEL(document, projectPath, Constant.FIRSTDIR);
                Element secondE1 = ParseXml.createSecondEL(firstEl, Constant.SECONDDIR1);
                Element secondE2 = ParseXml.createSecondEL(firstEl, Constant.SECONDDIR2);
                ParseXml.createChildren(secondE1, typeList, caseMap);
                ParseXml.createChildren(secondE2, typeList2, caseMap2);
                ParseXml.writeXml(document, new File(FileUtils.absolutePath(".")+projectPath+File.separator+Constant.CASEXML));

                if (case2LogResult.size()>0){
                    //更新状态配置文件
                    ParseXml xml1 = new ParseXml(xmlPath);
                    Set<String> caseSet = case2LogResult.keySet();
                    for (String s : caseSet) {
                        List<String>strList = case2LogResult.get(s);
                        for (String s1 : strList) {
                            String detail = null;
                            if ("null".equals(s1.split(",")[2])){
                                detail = "";
                            }else{
                                detail = s1.split(",")[2];
                            }
                            xml1.addResult(s,s1.split(",")[1],s1.split(",")[0],detail);
                           // xml1.addResult(s,s1.split(",")[1],s1.split(",")[0]);
                        }
                    }
                    xml1.writeXml();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //显示主页
            MainFrame.setPath(FileUtils.absolutePath(".")+projectPath+File.separator+Constant.CONFIG);
            MainFrame.setCaseMap(case2Result);
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dispose();
        }
    }

    public OpenProject(){
        setTitle("打开一个工程");
        setIconImage(UIUtils.getLogo(this, Constant.LOGO));
        setLayout(new GridBagLayout());
        setSize(Constant.WIDTH1,Constant.HEIGHT1);
        UIUtils.ChangeWindowDisplay();
        setLocationRelativeTo(null);
       // openButton = new JButton("选择工程");
        //openButton.addActionListener(this);
        //openButton.setFont(UIUtils.setFont(24));
        importButton = new JButton("导入工程");
        importButton.addActionListener(this);
        importButton.setFont(UIUtils.setFont(24));

        dirPath = new JComboBox(){
            public void updateUI() {
                super.updateUI();
                setUI(new MyComboBoxUI());
            }
        };
        if (FileUtils.getProjectName().size() > 0) {
            for (String s : FileUtils.getProjectName()) {
                dirPath.addItem(s);
            }
        }else{
            dirPath.addItem("none");
        }
        dirPath.setFont(UIUtils.setFont(16));
        dirPath.setEnabled(true);
        dirPath.setPreferredSize(new Dimension(Constant.WIDTH1-100, 25));
        //add(openButton, new GBC(2, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.CENTER));
        add(dirPath, new GBC(2, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.CENTER));
        add(importButton, new GBC(2, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.CENTER));
    }

    public static void main(String[] args) {
        OpenProject openProject = new OpenProject();
        openProject.setVisible(true);
        openProject.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
    