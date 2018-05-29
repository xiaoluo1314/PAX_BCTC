package cn.com.pax.display;

import cn.com.pax.Constant;
import cn.com.pax.log.ParseXml;
import cn.com.pax.utils.FileUtils;
import cn.com.pax.utils.GBC;
import cn.com.pax.utils.TimeUtils;
import cn.com.pax.utils.UIUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 新建工程
 *
 * @author luohl
 * @create 2017-12-05-20:15
 */
public class NewProject extends JDialog implements ActionListener {
    private JButton enterButton;
    private JButton cancelButton;
    private JTextField testType;
    private JTextField posField;
    private JTextField companyName;

    public NewProject(){
        setTitle("新建项目");
        setIconImage(UIUtils.getLogo(this,Constant.LOGO));
        setLayout(new GridBagLayout());
        setSize(Constant.WIDTH1,Constant.HEIGHT1);
        UIUtils.ChangeWindowDisplay();
        setLocationRelativeTo(null);
        JLabel testTypeLabel = new JLabel("测试类型：");
        testType = new JTextField(50);
        testType.setText(Constant.TESTTYPE);
        testType.setEnabled(false);

        JLabel companyNameLabel = new JLabel("公司名字：");
        companyName = new JTextField(50);

        JLabel posLabel = new JLabel("POS 机型： ");
        posField = new JTextField(50);

        testTypeLabel.setFont( UIUtils.setFont(16));
        posLabel.setFont( UIUtils.setFont(16));
        companyNameLabel.setFont( UIUtils.setFont(16));//百富计算机技术（深圳）有限公司

        enterButton = new JButton("确认");
        cancelButton = new JButton("取消");
        enterButton.addActionListener(this);
        cancelButton.addActionListener(this);
        enterButton.setFont(UIUtils.setFont(16));
        cancelButton.setFont(UIUtils.setFont(16));
        
        add(testTypeLabel, new GBC(0, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        add(testType, new GBC(1, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        add(companyNameLabel, new GBC(0, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        add(companyName, new GBC(1, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        add(posLabel, new GBC(0, 2).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        add(posField, new GBC(1, 2).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));

        add(enterButton, new GBC(0, 3).setInsets(5).setWeight(100, 100).setAnchor(GBC.CENTER));
        add(cancelButton, new GBC(1, 3).setInsets(5).setWeight(100, 100).setAnchor(GBC.CENTER));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(enterButton)){

            java.util.List<String> typeList = new ArrayList<>();
            Map<String, List<String>> caseMap = new HashMap<>();

            java.util.List<String> typeList2 = new ArrayList<>();
            Map<String, List<String>> caseMap2 = new HashMap<>();

            StringBuilder configBuilder = new StringBuilder();
            String testTypeStr = testType.getText().toString().trim();
            String companyStr = companyName.getText().toString().trim();
            String posStr = posField.getText().toString().trim();
            if (StringUtils.isBlank(testTypeStr)||StringUtils.isBlank(companyStr)||StringUtils.isBlank(posStr))
            {
                MyOptionPane.showMessage(NewProject.this,"请输入完整信息!","信息输入");
                return;
            }
            String timeStr = TimeUtils.getDateTimeFormat(new Date());
            String dirStr = testTypeStr+"-"+companyStr+"-"+posStr+"-"
                    +timeStr;
            dirStr =dirStr.replace(":","-").replace(" ","-");
            //System.out.println("dirStr的值是：---"+ dirStr + "，当前方法=actionPerformed.NewProject()");
            configBuilder.append(testTypeStr+"\r\n");
            configBuilder.append(companyStr+"\r\n");
            configBuilder.append(posStr+"\r\n");
            if (FileUtils.createDir(dirStr)){
                //同时把信息写入配置
                FileUtils.writeConfig(FileUtils.absolutePath(".")+dirStr+File.separator+Constant.CONFIG,configBuilder.toString());
                //显示主页
                MainFrame.setPath(FileUtils.absolutePath(".")+dirStr+File.separator+Constant.CONFIG);
                /**
                 * 写xml
                 */
                //Todo 这里有问题了
                FileUtils.xmlConfig("." + File.separator + Constant.FIRSTDIR + File.separator + Constant.DEBUG + File.separator+Constant.SECONDDIR1,typeList, caseMap);
                FileUtils.xmlConfig("." + File.separator + Constant.FIRSTDIR + File.separator + Constant.DEBUG + File.separator+Constant.SECONDDIR2,typeList2, caseMap2);

                //Document document = ParseXml.createDocument(dirStr, Constant.FIRSTDIR, Constant.SECONDDIR1, FileUtils.getTypeList(), FileUtils.getCaseMap());

                try {
                    Document document = ParseXml.createDocument();
                    Element firstEl = ParseXml.createFirstEL(document,dirStr, Constant.FIRSTDIR);
                    Element secondE1 = ParseXml.createSecondEL(firstEl, Constant.SECONDDIR1);
                    Element secondE2 = ParseXml.createSecondEL(firstEl, Constant.SECONDDIR2);
                    ParseXml.createChildren(secondE1, typeList, caseMap);
                    ParseXml.createChildren(secondE2, typeList2, caseMap2);
                    ParseXml.writeXml(document, new File(FileUtils.absolutePath(".")+dirStr+File.separator+Constant.CASEXML));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dispose();
            }else{
                MyOptionPane.showMessage(NewProject.this,"输入非法字符,导致不能创建文件!请重新输入","信息输入");
                return;
            }
        }else if (e.getSource().equals(cancelButton)){
            //如果取消，就关闭工程
            this.setDefaultCloseOperation(NewProject.DISPOSE_ON_CLOSE);
            dispose();
        }
    }

    public static void main(String[] args) {
        NewProject newProject = new  NewProject();
        newProject.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        newProject.setVisible(true);
    }
}
    