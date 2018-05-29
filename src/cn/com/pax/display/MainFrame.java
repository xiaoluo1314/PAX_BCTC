package cn.com.pax.display;

import cn.com.pax.Constant;
import cn.com.pax.comm.C4Socket;
import cn.com.pax.comm.SerialConnection;
import cn.com.pax.comm.SerialOperation;
import cn.com.pax.comm.SocketOperation;
import cn.com.pax.table.HidTableColumn;
import cn.com.pax.table.MyButtonClick;
import cn.com.pax.table.MyTableModel;
import cn.com.pax.table.TableOperation;
import cn.com.pax.tree.*;
import cn.com.pax.utils.FileUtils;
import cn.com.pax.utils.GBC;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * UI显示类
 *
 * @author luohl
 * @create 2017-12-04-16:30
 */
public class MainFrame extends JFrame {

    private JPanel startPanel;//开始
    private JTextArea config;//其他
    private MyTableModel tableModel = null;
    private static String path;
    private JPanel otherPanel;
    private JTable table;
    private JTree jTree;
    private CheckBoxTreeNode rootNode;
   // private DefaultTreeModel model;
    private JButton exeImageButton;
    private JButton stopImageButton;
    private JButton clearScreen;
    private JButton allCheck;
    private JButton inverseCheck;
    private JButton partCheck;
    private JButton allDelete;
    private JButton inPartCheck;
    private JButton software;
    private MyButtonClick mybuttonClick;
    private JComboBox<String> serialCbx;
    public static JTextField jingShenJtf;//改成了静态

    public static int state;

    private JTextField rotateField;
    private JTextField moveField;
    private C4Socket c4Socket;
    //private JTextArea recordTa;
    private static Map<String, String> caseMap ;
    private JCheckBox passJcb;
    private JCheckBox failJcb;
    private JCheckBox unTestJcb;
    private CustTreeModel model;
    public static JCheckBox isInputChx;
    private JEditorPane recordTa; // 为了不改变以往的代码，名字就先不改
    private JButton reportBt;

    public static void setCaseMap(Map<String, String >caseMap1) {
        caseMap = caseMap1;
    }

    //这个路径新建和打开已有的工程都要走
    public static void setPath(String path1) {
        path = path1;
    }
    public static String getPath(){
        return path;
    }

    public MainFrame() {
        super("BCTC条码测试软件");
        setSize(Constant.WIDTH, Constant.HEIGHT);
        setIconImage(UIUtils.getLogo(this,Constant.LOGO));
        mybuttonClick = MyButtonClick.getInstance();
        UIUtils.ChangeWindowDisplay();
        /*this.setLocation((this.getToolkit().getScreenSize().width - WIDTH)/2, (this
                .getToolkit().getScreenSize().height - HEIGHT )/2 );*/
        setLocationRelativeTo(null);
        JTabbedPane tabPane = new JTabbedPane();
        startPanel = new JPanel(new BorderLayout( 5,5));
        otherPanel = new JPanel();
        addInterface(otherPanel);
        ///可以增加面板
        tabPane.addTab(Constant.START, UIUtils.createImageIcon(Constant.STARTPATH),startPanel);
        tabPane.addTab(Constant.OTHER,UIUtils.createImageIcon(Constant.STARTPATH), otherPanel);
        tabPane.setSelectedIndex(0);
        // tabPane.setTabPlacement(JTabbedPane.TOP);
        //  tabPane.setBorder(null);
        //  tabPane.setUI(new MyTabBedPanelUI());

        JPanel topPanel = new JPanel();
        topPanelUI(topPanel);
        JPanel treePanel = new JPanel(new BorderLayout(5,5));
        treeOperation(treePanel);
        JPanel tablePanel = new JPanel();
        tableJpanel(tablePanel);

        JPanel logPanel = new JPanel();
        logJpanel(logPanel);

        startPanel.add(topPanel, BorderLayout.NORTH);
        startPanel.add(treePanel, BorderLayout.WEST);
        startPanel.add(tablePanel, BorderLayout.CENTER);
        startPanel.add(logPanel, BorderLayout.EAST);
        add(tabPane);
    }
    private void topPanelUI (JPanel topPanel){

        topPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       // topPanel.setBackground(Color.gray);
        topPanel.setLayout(new BorderLayout(5,5));
        //Todo 可以修改UI大小
        topPanel.setPreferredSize(new Dimension(Constant.WIDTH, Constant.HEIGHT/8));

        JPanel runPanel = new JPanel(new GridBagLayout());
        runPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        runPanel.setPreferredSize(new Dimension(Constant.WIDTH/5, Constant.HEIGHT/8));

        JPanel centerPanel = new JPanel(new BorderLayout( 5,5));
        selectScript(centerPanel);

        exeImageButton = new JButton(UIUtils.createImageIcon(Constant.RUNPATH));
        exeImageButton.setActionCommand(Constant.STARTEXE);
        JLabel exeLabel = new JLabel("开始执行",JLabel.CENTER);
        //exeLabel.setFont(font);

        stopImageButton = new JButton(UIUtils.createImageIcon(Constant.STOPPATH));
        stopImageButton.setActionCommand(Constant.STOPTEST);
        JLabel stopJLabel = new JLabel("停止执行",JLabel.CENTER);
       // stopJLabel.setFont(font);

        clearScreen = new JButton(UIUtils.createImageIcon(Constant.CLEARSCREEN));
        clearScreen.setActionCommand(Constant.CLEARSCREE);
        JLabel clearScreenJLabel = new JLabel("景深测试",JLabel.CENTER);

        JLabel runscriptJLabel = new JLabel("执行脚本");
        runscriptJLabel.setFont(UIUtils.setFont(Constant.FONTSIZE));

        runPanel.add(exeImageButton, new GBC(0, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        runPanel.add(exeLabel, new GBC(0, 1).setInsets(5,13,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        runPanel.add(stopImageButton, new GBC(1, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        runPanel.add(stopJLabel, new GBC(1, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        runPanel.add(clearScreen, new GBC(2, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        runPanel.add(clearScreenJLabel, new GBC(2, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        runPanel.add(runscriptJLabel, new GBC(1, 3).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        topPanel.add(runPanel, BorderLayout.WEST);
        topPanel.add(centerPanel,BorderLayout.CENTER);

    }

    private void selectScript (JPanel centerPanel){
        JPanel selectPanel = new JPanel(new GridBagLayout());
        selectPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        //Todo 可以修改UI大小
        selectPanel.setPreferredSize(new Dimension(Constant.WIDTH/3, Constant.HEIGHT/8));

        JPanel projectPanel = new JPanel();
        projectJPanel(projectPanel);

        JPanel selectReadPanel = new JPanel();
        selectRead(selectReadPanel);

        allCheck = new JButton( UIUtils.createImageIcon(Constant.ALLCHECK));
        allCheck.setActionCommand(Constant.ALLSELECT);
        JLabel allCheckLabel = new JLabel("全部选择",JLabel.CENTER);

        inverseCheck = new JButton(UIUtils.createImageIcon(Constant.INVERSECHECK));
        inverseCheck.setActionCommand(Constant.NOSELECT);
        JLabel inverseCheckLabel = new JLabel("全部不选",JLabel.CENTER);

        partCheck = new JButton(UIUtils.createImageIcon(Constant.PARTCHECK));
        partCheck.setActionCommand(Constant.PARTSELECT);
        JLabel partCheckLabel = new JLabel("部分选择",JLabel.CENTER);

        inPartCheck = new JButton(UIUtils.createImageIcon(Constant.PARTDELTE));
        inPartCheck.setActionCommand(Constant.NOPARTSELECT);
        JLabel inpartCheckLabel = new JLabel("部分不选",JLabel.CENTER);

        allDelete = new JButton(UIUtils.createImageIcon(Constant.ALLDELTE));
        allDelete.setActionCommand(Constant.ALLDELETE);
        JLabel allDeleteLabel = new JLabel("全部删除",JLabel.CENTER);

        JLabel selectScriptLabel = new JLabel("选择脚本",JLabel.CENTER);
        selectScriptLabel.setFont(UIUtils.setFont(Constant.FONTSIZE));

        selectPanel.add(allCheck, new GBC(0, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        selectPanel.add(allCheckLabel, new GBC(0, 1).setInsets(5,13,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        selectPanel.add(inverseCheck, new GBC(1, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        selectPanel.add(inverseCheckLabel, new GBC(1, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        selectPanel.add(partCheck, new GBC(2, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        selectPanel.add(partCheckLabel, new GBC(2, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        selectPanel.add(inPartCheck, new GBC(3, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        selectPanel.add(inpartCheckLabel, new GBC(3, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        selectPanel.add(allDelete, new GBC(4, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        selectPanel.add(allDeleteLabel, new GBC(4, 1).setInsets(5,15,5,5).
                setWeight(100, 100).setAnchor(GBC.WEST));

        selectPanel.add(selectScriptLabel, new GBC(2, 3).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.WEST));
        centerPanel.add(selectPanel,BorderLayout.WEST);
        centerPanel.add(projectPanel,BorderLayout.CENTER);
        centerPanel.add(selectReadPanel,BorderLayout.EAST);
    }
    private void projectJPanel(JPanel jPanel){
        jPanel.setLayout(new BorderLayout(5,5));
        jPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        //Todo 可以修改UI大小
        jPanel.setPreferredSize(new Dimension(
                Constant.WIDTH/4, Constant.HEIGHT/8));
        JLabel project = new JLabel("工程：",JLabel.LEFT);
        project.setFont(UIUtils.setFont(Constant.FONTSIZE));

        config = new JTextArea();
        config.setLineWrap(true);        //激活自动换行功能
        config.setWrapStyleWord(true);   // 激活断行不断字功能
        //System.out.println("path的值是：---"+ path + "，当前方法=projectJPanel.MainFrame()");
        if (path == null){
            config.setText("获取配置信息有误,请检查！");
        }else{
            config.setText(FileUtils.readConfig(path));
        }

        JLabel projectInfo = new JLabel("工程信息",JLabel.CENTER);
        projectInfo.setFont(UIUtils.setFont(Constant.FONTSIZE));
        jPanel.add(project,BorderLayout.NORTH);
        //中间信息
        jPanel.add(new JScrollPane(config),BorderLayout.CENTER);
        jPanel.add(projectInfo,BorderLayout.SOUTH);
    }
   private void selectRead(JPanel jPanel){
        //Todo 可以修改UI大小
       jPanel.setPreferredSize(new Dimension(Constant.WIDTH/4, Constant.HEIGHT/8));
       jPanel.setLayout(new BorderLayout(5,5));

       JPanel softwarePanel = new JPanel(new GridBagLayout());
       softwarePanel.setPreferredSize(new Dimension(Constant.WIDTH/11, Constant.HEIGHT/8));
       aboutSoftware(softwarePanel);

       JPanel readPanel = new JPanel(new BorderLayout(5,5));
       readPanel.setLayout( new BorderLayout(5,5));
       readPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       readPanel.setPreferredSize(new Dimension(Constant.WIDTH/4+40, Constant.HEIGHT/8));

       JPanel centerPan = new JPanel(new GridBagLayout());
       serialCbx = new JComboBox<>();
       if (SerialConnection.getCommInfo()!=null || SerialConnection.getCommInfo().size()>0 ){
           for (String s : SerialConnection.getCommInfo())
               serialCbx.addItem(s);
       }
       JLabel serialLabel  = new JLabel("串口:");
       JLabel jingShenLabel = new JLabel("最优景深(mm):");
       jingShenJtf = new JTextField(10);

       jingShenJtf.setText(FileUtils.calcOptimalDepth(path + File.separator + Constant.OPTIMADEPTH,2)+""); //默认加载二维最优景深

      // JButton updateSerial = new JButton("更新串口");
       serialCbx.setPreferredSize(new Dimension(jingShenJtf.getPreferredSize().width, jingShenJtf.getPreferredSize().height));// 66,21

       JLabel isInputLabel = new JLabel("手动输入标志:");
       isInputChx = new JCheckBox();

       JLabel readInfo = new JLabel("串口&景深",JLabel.CENTER);
       readInfo.setFont(UIUtils.setFont(Constant.FONTSIZE));

       centerPan.add(serialLabel, new GBC(0, 0).
               setWeight(100, 100).setAnchor(GBC.WEST));
       centerPan.add(serialCbx, new GBC(1, 0).
               setWeight(100, 100).setAnchor(GBC.WEST));
       centerPan.add(jingShenLabel, new GBC(0, 1).
               setWeight(100, 100).setAnchor(GBC.WEST));
       centerPan.add(jingShenJtf, new GBC(1, 1).
               setWeight(100, 100).setAnchor(GBC.WEST));

       centerPan.add(isInputLabel, new GBC(0, 2).
               setWeight(100, 100).setAnchor(GBC.WEST));
       centerPan.add(isInputChx, new GBC(1, 2).
               setWeight(100, 100).setAnchor(GBC.WEST));

//      centerPan.add(updateSerial, new GBC(0, 2).setInsets(5).
//               setWeight(100, 100).setAnchor(GBC.WEST));
       //System.out.println("的值是：---"+jingShenJtf.getPreferredSize().height  + "，当前方法=selectRead.MainFrame()");
      // System.out.println("的值是：---"+jingShenJtf.getPreferredSize().width  + "，当前方法=selectRead.MainFrame()");
       readPanel.add(centerPan,BorderLayout.CENTER);
       readPanel.add(readInfo,BorderLayout.SOUTH);

       jPanel.add(readPanel,BorderLayout.CENTER);
       jPanel.add(softwarePanel,BorderLayout.EAST);
   }
   private void aboutSoftware(JPanel jPanel){
       software = new JButton(UIUtils.createImageIcon(Constant.SOFTWARE));
       software.setActionCommand(Constant.SOFTWAREINFO);
       JLabel aboutSoft = new JLabel("关于软件",JLabel.CENTER);
       JLabel softInfo = new JLabel("软件信息",JLabel.CENTER);
       softInfo.setFont(UIUtils.setFont(Constant.FONTSIZE));
       aboutSoft.setFont(UIUtils.setFont(Constant.FONTSIZE));
       jPanel.add(software, new GBC(0, 0).setInsets(5).
               setWeight(100, 100).setAnchor(GBC.WEST));
       jPanel.add(aboutSoft, new GBC(0, 1).setInsets(5,13,5,5).
               setWeight(100, 100).setAnchor(GBC.WEST));
       jPanel.add(softInfo, new GBC(0, 2).setInsets(5,13,5,5).
               setWeight(100, 100).setAnchor(GBC.WEST));
   }

    //下面树生成
   public  void treeOperation(JPanel treePanel){
       treePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       treePanel.setPreferredSize(new Dimension(Constant.WIDTH/5 +30, Constant.HEIGHT-(Constant.HEIGHT/8)));//可更改大小

       JPanel headPanel = new JPanel(new GridBagLayout());
       headPanel.setPreferredSize(new Dimension(Constant.WIDTH/5 +30, 68));
       headPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       //填充树的内容
       JPanel contendPanel = new JPanel(new BorderLayout(5,5));

       rootNode = TreeOperation.traverseFolder(Constant.TESTCRIPT);
       if (rootNode == null){
           JOptionPane.showMessageDialog(MainFrame.this,"用例集配置文件不存在,请确认,再测试！","获取测试用例集",JOptionPane.WARNING_MESSAGE);
           return;
       }
      // System.out.println("rootNode的值是：---"+ rootNode + "，当前方法=treeOperation.MainFrame()");
      // model = new DefaultTreeModel(rootNode);
       model = new CustTreeModel(rootNode);
       jTree = new JTree(model);
       jTree.setCellRenderer(new CheckBoxTreeCellRenderer());
       JScrollPane scroll = new JScrollPane(jTree);
       scroll.setBounds(0, 0, 300, 320);
       jTree.setShowsRootHandles(true);


       // rootNode = TreeOperation.traverseFolder(Constant.TESTCRIPT);
      // newModel = new DefaultTreeModel(rootNode);
       //jTree = new JTree();
      // jTree.setModel(newModel);//模型中给出数据
       //jTree.putClientProperty("JTree.lineStyle","Horizontal");
       //jTree.putClientProperty("JTree.lineStyle","Angled");
       //
      // jTree.setCellRenderer(new CheckBoxTreeCellRenderer());
      // jTree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
      // myCellRenderer = new MyCellRenderer();
       //myCellRenderer.setLeafIcon(UIUtils.createImageIcon(Constant.UNTEST));
      // myCellRenderer.setFont(UIUtils.setFont(Constant.FONTSIZE));
      // jTree.setCellRenderer(myCellRenderer);
      // jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

       //难道是树的状态改变来更改图标？事件？
      // myTreeSelectionListener.getTabelSet();

       //DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) jTree.getCellRenderer();
       //cellRenderer.setLeafIcon(UIUtils.createImageIcon(Constant.UNTEST));
       //cellRenderer.setOpenIcon(new ImageIcon("..\\icons\\open.gif"));
      // cellRenderer.setClosedIcon(new ImageIcon("..\\icons\\close.gif"));

       JLabel project = new JLabel("工程");
       project.setFont(UIUtils.setFont(Constant.FONTSIZE));
       passJcb = new JCheckBox();
       failJcb = new JCheckBox();
       unTestJcb = new JCheckBox();
       JLabel pass = new JLabel("Pass");
       JLabel fail = new JLabel("Fail");
       JLabel unTest = new JLabel("Un Test");
       passJcb.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               selectJCB();
               model.setShowStatus(state);
               TreeOperation.expandAll(jTree, new TreePath( rootNode.getRoot()), true);
           }
       });
       failJcb.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               selectJCB();
               model.setShowStatus(state);
               TreeOperation.expandAll(jTree, new TreePath( rootNode.getRoot()), true);
           }
       });
       unTestJcb.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               selectJCB();
               model.setShowStatus(state);
               TreeOperation.expandAll(jTree, new TreePath( rootNode.getRoot()), true);
           }
       });

       passJcb.setSelected(true);
       failJcb.setSelected(true);
       unTestJcb.setSelected(true);
       state = 7;
       model.setShowStatus(state);
       TreeOperation.expandAll(jTree, new TreePath( rootNode.getRoot()), true);

       headPanel.add(project,new GBC(0,0).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(passJcb,new GBC(0,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(pass,new GBC(1,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(failJcb,new GBC(2,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(fail,new GBC(3,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(unTestJcb,new GBC(4,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       headPanel.add(unTest,new GBC(5,1).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
       contendPanel.add(scroll);
       treePanel.add(headPanel,BorderLayout.NORTH);
       treePanel.add(contendPanel,BorderLayout.CENTER);

   }
   //表格
    public void tableJpanel (JPanel tablePanel){
        tablePanel.setLayout(new BorderLayout(5,5));
        //tablePanel.setPreferredSize(new Dimension(Constant.WIDTH/5 +30, Constant.HEIGHT-(Constant.HEIGHT/8)));//可更改大小
        tablePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        JPanel head = new JPanel(new GridBagLayout());
        head.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        JPanel tableJPanel = new JPanel(new BorderLayout(5,5));

        JLabel title = new JLabel("卡片特征");
        JLabel headTitle = new JLabel("ALL TEST");
        // 显示用例表格
        JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        jp1.setBackground(Color.GRAY);
        //Todo 遇到的问题 以解决
        JPanel jp2 = new JPanel(new BorderLayout(5,5));

        //jp2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        tableModel = new MyTableModel(Constant.CONLUNMNSNAME, 0);
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tableModel.setCellEditable(0, 0);
        HidTableColumn.HiddenCell(table,3);
        TableOperation.setTable(table);
        //Todo 更新表格数据以解决
        jTree.addMouseListener(new CheckBoxTreeNodeSelectionListener(table));

       // myTreeSelectionListener = new MyTreeSelectionListener(table);
        //jTree.addTreeSelectionListener(myTreeSelectionListener);
       // table.addMouseListener(new MyMouseListener(table));
        headTitle.setFont(UIUtils.setFont(Constant.FONTTITLE));
        head.add(title,new GBC(0,0).setWeight(100,100).setAnchor(GBC.WEST).setInsets(5));
        head.add(headTitle,new GBC(0,1).setWeight(100,100).setAnchor(GBC.CENTER).setInsets(5));

        //System.out.println(head.getPreferredSize().width +" "+ head.getPreferredSize().height);
        //System.out.println(headTitle.getPreferredSize().width +" "+ headTitle.getPreferredSize().height);
        //System.out.println(title.getPreferredSize().width +" "+ title.getPreferredSize().height);

        JLabel tableHead = new JLabel("测试用例");
        tableHead.setFont(UIUtils.setFont(Constant.FONTSIZE));

        jp1.add(tableHead);
        jp2.add(new JScrollPane(table));

        tableJPanel.add(jp1,BorderLayout.NORTH);
        tableJPanel.add(jp2,BorderLayout.CENTER);

        //System.out.println(head.getPreferredSize().width +" "+ head.getPreferredSize().height);
        tablePanel.add(head,BorderLayout.NORTH);
        tablePanel.add(tableJPanel,BorderLayout.CENTER);
       // System.out.println(tablePanel.getPreferredSize().width +" "+ tablePanel.getPreferredSize().height);

    }
    //日志
    public void logJpanel (JPanel logPanel){
        logPanel.setLayout(new BorderLayout(5,5));
        logPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        logPanel.setPreferredSize(new Dimension(Constant.WIDTH/2, Constant.HEIGHT-(Constant.HEIGHT/8)));
        JTabbedPane jTabbedPane = new JTabbedPane();
        JPanel historyPanel = new JPanel();//历史
        JPanel recordPanel  = new JPanel(new BorderLayout());//记录

        //计算的出行数与列数
//        recordTa = new JTextArea(35,72);
//        recordTa.setLineWrap(true);
//        recordTa.setWrapStyleWord(true);

        recordTa = new JEditorPane();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        recordTa.setEditorKit(htmlEditorKit);
//        Document doc = editPane.getDocument();
        recordPanel.add(new JScrollPane(recordTa),BorderLayout.CENTER);

//        JTextPane txt = new JTextPane();
//        JScrollPane scl =new JScrollPane(txt);
//        recordPanel.add(scl);
       // recordPanel.add(new JScrollPane(recordTa));
//        txt.setFont(UIUtils.setFont(14));
//        txt.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        // UIUtils.updateTxA(recordTa,"[================start Test====================\r\n");
      //  UIUtils.updateTxA(recordTa,"[================start Test====================\r\n",true);
//        recordTa.setText("[================start Test====================\r\n");
//        recordTa.append("[================start Test====================\r\n");
        jTabbedPane.addTab(Constant.RECORD, recordPanel);
        jTabbedPane.addTab(Constant.HISTORY, historyPanel);
        jTabbedPane.setSelectedIndex(0);
        logPanel.add(jTabbedPane);
        buttonActionListener();
    }

    private void buttonActionListener(){
        serialCbx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SerialOperation.getInstance().setSerialNum(serialCbx.getSelectedItem().toString().trim());
            }
        });

        if (caseMap !=null) {
            if (caseMap.size() > 0) {
                Set<String> set = caseMap.keySet();
                for (String s : set) {
                    if ("PASS".equals(caseMap.get(s))) {
                        UpdateTestResult.updateTestResult(jTree, s, 1);
                    } else {
                        UpdateTestResult.updateTestResult(jTree, s, 2);
                    }
                }
            }
        }
        exeImageButton.addActionListener(mybuttonClick);
        stopImageButton.addActionListener(mybuttonClick);
        clearScreen.addActionListener(mybuttonClick);
        allCheck.addActionListener(mybuttonClick);
        inverseCheck.addActionListener(mybuttonClick);
        partCheck.addActionListener(mybuttonClick);
        allDelete.addActionListener(mybuttonClick);
        inPartCheck.addActionListener(mybuttonClick);
        software.addActionListener(mybuttonClick);
        setVar();
    }
    private void setVar(){
        mybuttonClick.setjTree(jTree);
        mybuttonClick.setjTable(table);
        mybuttonClick.setjFrame(this);
        mybuttonClick.setExeImageButton(exeImageButton);
        mybuttonClick.setStopImageButton(stopImageButton);
        mybuttonClick.setClearScreen(clearScreen);
        mybuttonClick.setAllCheck(allCheck);
        mybuttonClick.setAllDelete(allDelete);
        mybuttonClick.setInPartCheck(inPartCheck);
        mybuttonClick.setInverseCheck(inverseCheck);
        mybuttonClick.setPartCheck(partCheck);
        mybuttonClick.setSoftware(software);
        //mybuttonClick.setJingShenJtf(jingShenJtf);
        mybuttonClick.setRecordTa(recordTa);
    }

    public void addInterface(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"机械臂控制模块"));
        panel.add(centerPanel, new GBC(0, 0, 3, 1).setWeight(100, 100).setInsets(5).setFill(GBC.BOTH));

        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "辅助功能选项"));
        panel.add(eastPanel,new GBC(3, 0, 1, 1).setWeight(50, 100).setInsets(5).setFill(GBC.BOTH));

        addCenterPanel(centerPanel);
        addEastPanel(eastPanel);
    }

    public void addCenterPanel(JPanel center) {
        c4Socket = SocketOperation.openC4SocKet();

        center.setLayout(new GridBagLayout());

        JLabel moveLabel = new JLabel("移动距离:");
        moveField = new JTextField(5);
        moveField.setText("5");
        JLabel rotateLabel = new JLabel("转动角度:");
        rotateField = new JTextField(5);
        rotateField.setText("1");
        JLabel moveRateLabel = new JLabel("移动速度");
        JTextField moveRateField = new JTextField(5);
        moveRateField.setText("10");
        moveRateField.setEnabled(false);
        JLabel rotateRateLabel = new JLabel("转动速度:");
        JTextField rotateRateField = new JTextField(5);
        rotateRateField.setText("1");
        rotateRateField.setEnabled(false);

        JButton upButton = new JButton("上");
        addActionListen(upButton, "movexyz 0 0 %s");
        JButton downButton = new JButton("下");
        addActionListen(downButton, "movexyz 0 0 -%s");
        JButton leftButton = new JButton("左");
        addActionListen(leftButton, "movexyz %s 0 0");
        JButton rightButton = new JButton("右");
        addActionListen(rightButton, "movexyz -%s 0 0");
        JButton forwardButton = new JButton("前进");
        addActionListen(forwardButton, "movexyz 0 %s 0");
        JButton fallbackButton = new JButton("后退");
        addActionListen(fallbackButton, "movexyz 0 -%s 0");

        JButton leftRotateButton = new JButton("左转");
        addActionListen(leftRotateButton, "rotateuvw 0 %s 0");
        JButton rightRotateButton = new JButton("右转");
        addActionListen(rightRotateButton, "rotateuvw 0 -%s 0");
        JButton upRollButton = new JButton("上翻");
        addActionListen(upRollButton, "rotateuvw 0 0 %s");
        JButton downRollButton = new JButton("下翻");
        addActionListen(downRollButton, "rotateuvw 0 0 -%s");
        JButton leftRollButton = new JButton("左翻");
        addActionListen(leftRollButton, "rotateuvw -%s 0 0");
        JButton rightRollButton = new JButton("右翻");
        addActionListen(rightRollButton, "rotateuvw %s 0 0");

        JButton returnButton = new JButton("初始化");
        addActionListen(returnButton, "init");
        JButton confirmButton = new JButton("标定");
        addActionListen(confirmButton, "locateorigin");
        JButton backtop0Button = new JButton("回到标定");
        addActionListen(backtop0Button, "backp0");
        JButton onoffButton = new JButton("查询标定");
        addActionListen(onoffButton, "queryorigin");

        //first line
        center.add(upButton, new GBC(1, 0).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(forwardButton, new GBC(3, 0).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(fallbackButton, new GBC(4, 0).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(returnButton, new GBC(5, 0).setInsets(10).setWeight(100,100).setAnchor(GBC.CENTER));

        //second line
        center.add(leftButton, new GBC(0, 1).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(rightButton, new GBC(2, 1).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(leftRotateButton, new GBC(3, 1).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(rightRotateButton, new GBC(4, 1).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(confirmButton, new GBC(5, 1).setInsets(10).setWeight(100,100).setAnchor(GBC.CENTER));

        //three line
        center.add(downButton, new GBC(1, 2).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(upRollButton, new GBC(3, 2).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(downRollButton, new GBC(4, 2).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(backtop0Button, new GBC(5, 2).setInsets(10).setWeight(100,100).setAnchor(GBC.CENTER));

        //four line
        center.add(leftRollButton, new GBC(3, 3).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(rightRollButton, new GBC(4, 3).setInsets(5).setWeight(100,100).setAnchor(GBC.CENTER));
        center.add(onoffButton, new GBC(5, 3).setInsets(10).setWeight(100,100).setAnchor(GBC.CENTER));

        //five line
        center.add(moveLabel, new GBC(0, 4, 1,1).setInsets(5).setWeight(100,100).setAnchor(GBC.EAST));
        center.add(moveField, new GBC(1, 4).setInsets(5).setWeight(100,100).setAnchor(GBC.WEST));
        center.add(moveRateLabel, new GBC(2, 4, 1,1).setInsets(5).setWeight(100,100).setAnchor(GBC.EAST));
        center.add(moveRateField, new GBC(3, 4).setInsets(5).setWeight(100,100).setAnchor(GBC.WEST));

        //six line
        center.add(rotateLabel, new GBC(0, 5,1,1).setInsets(5).setWeight(100,100).setAnchor(GBC.EAST));
        center.add(rotateField, new GBC(1, 5).setInsets(5).setWeight(100,100).setAnchor(GBC.WEST));
        center.add(rotateRateLabel, new GBC(2,5,1,1).setInsets(5).setWeight(100,100).setAnchor(GBC.EAST));
        center.add(rotateRateField, new GBC(3, 5).setInsets(5).setWeight(100,100).setAnchor(GBC.WEST));
        SocketOperation.setPc2C4(c4Socket);
    }

    public void addEastPanel(JPanel east) {
        //JButton jsButton = new JButton("最优景深");
       // east.add(jsButton);
        FileUtils.readCfg();
        east.setLayout(new BorderLayout(5,5));
        JPanel north = new JPanel(new GridBagLayout());
        JPanel center = new JPanel(new BorderLayout(5,5));
        JPanel reportPanel = new JPanel(new GridBagLayout());
        north.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"通讯设置"));
        reportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"报告设置"));
        east.setPreferredSize(new Dimension(300,300));
        JLabel C4Adrr= new JLabel("IP_C4:");
        JLabel C4Port = new JLabel("PORT_C4:");
        JLabel phoneAdrr = new JLabel("IP_PHONE:");
        JLabel phonePort = new JLabel("PORT_PHONE:");
        JButton button = new JButton("确认");

        JLabel testerLabel = new JLabel("测试员:", JLabel.LEFT);
        JLabel testLocLabel = new JLabel("测试地点:", JLabel.LEFT);
        JLabel testEnvLabel = new JLabel("测试环境:", JLabel.LEFT);
        JLabel remarkLabel = new JLabel("备注:", JLabel.LEFT);

        JTextField testerField = new JTextField(20);
        JTextField testLocField = new JTextField(20);
        JTextField testEnvField = new JTextField(20);
        JTextField remarkField = new JTextField(20);

        reportBt = new JButton("生成报告");
        reportBt.setActionCommand(Constant.REPORTBTN);
        reportBt.addActionListener(mybuttonClick);
        mybuttonClick.setReport(reportBt);
        mybuttonClick.setTesterField(testerField);
        mybuttonClick.setTestLocField(testLocField);
        mybuttonClick.setTestEnvField(testEnvField);
        mybuttonClick.setRemarkField(remarkField);

        final JTextField C4AddrField= new JTextField(20);
        C4AddrField.setText(FileUtils.getIpC4().trim());
        final JTextField C4PortField = new JTextField(20);
        C4PortField.setText(FileUtils.getPortC4().trim());
        final JTextField phoneAddrField= new JTextField(20);
        phoneAddrField.setText(FileUtils.getIpPhone().trim());
        final JTextField phonePortField = new JTextField(20);
        phonePortField.setText(FileUtils.getPortPhone().trim());

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileUtils.writeComm2File(Constant.C4IP, C4AddrField.getText().trim());
                FileUtils.writeComm2File(Constant.C4PORT, C4PortField.getText().trim());
                FileUtils.writeComm2File(Constant.PHONEIP, phoneAddrField.getText().trim());
                FileUtils.writeComm2File(Constant.PHONEPORT, phonePortField.getText().trim());
                MyOptionPane.showMessage(MainFrame.this,"通讯设置完成","设置");
            }
        });
        north.add(C4Adrr, new GBC(0, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        north.add(C4AddrField, new GBC(1, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        north.add(C4Port, new GBC(2, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        north.add(C4PortField, new GBC(3, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));

        north.add(phoneAdrr, new GBC(0, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        north.add(phoneAddrField, new GBC(1, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        north.add(phonePort, new GBC(2, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        north.add(phonePortField, new GBC(3, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));

        north.add(button, new GBC(2, 2).setInsets(10,0,0,0).setWeight(100, 100).setAnchor(GBC.CENTER));
        east.add(north, BorderLayout.NORTH );

        reportPanel.add(testerLabel, new GBC(0, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        reportPanel.add(testerField, new GBC(1, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        reportPanel.add(testLocLabel, new GBC(2, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        reportPanel.add(testLocField, new GBC(3, 0).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));

        reportPanel.add(testEnvLabel, new GBC(0, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        reportPanel.add(testEnvField, new GBC(1, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));
        reportPanel.add(remarkLabel, new GBC(2, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.EAST));
        reportPanel.add(remarkField, new GBC(3, 1).setInsets(5).setWeight(100, 100).setAnchor(GBC.WEST));

        reportPanel.add(reportBt, new GBC(2, 2).setInsets(10,0,0,0).setWeight(100, 10).setAnchor(GBC.WEST));

        center.add(reportPanel, BorderLayout.NORTH);
        east.add(center, BorderLayout.CENTER );
    }

    public void addActionListen(final JButton button, final String command) {
        String distanceStr = "1";
        if(command.contains("movexyz"))
            distanceStr = moveField.getText().trim();
        else if(command.contains("rotateuvw"))
            distanceStr = rotateField.getText().trim();
        else
            distanceStr = "000";

        final String cmdStr = distanceStr.equalsIgnoreCase("000") ? command : String.format(command, distanceStr) ;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean succFlag = false;
                        if(connServer()) {
                            if(c4Socket.writeReadOneLine(cmdStr).contains("ok")) {
                                succFlag = true;
                            } else {
                                System.out.println(cmdStr + ": failed!");
                            }
                        } else {
                            System.out.println("network: failed!");
                        }
                        final boolean isSucc = succFlag;
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(command.equalsIgnoreCase("queryorigin")) {
                                    JOptionPane.showMessageDialog(null, isSucc ? "已标定":"未标定");
                                }
                                button.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public boolean connServer() {
        if(!c4Socket.isConnect()) {
            return c4Socket.openConn();
        }
        else {
            return true;
        }
    }

    private void selectJCB(){
        if ( passJcb.isSelected() && failJcb.isSelected() && unTestJcb.isSelected()){
            state = 7;
        }else if(!passJcb.isSelected() && failJcb.isSelected() && unTestJcb.isSelected()){
            state = 6;
        }else if(passJcb.isSelected() && !failJcb.isSelected() && unTestJcb.isSelected()){
            state = 5;
        }else if(passJcb.isSelected() && failJcb.isSelected() && !unTestJcb.isSelected()){
            state = 3;
        }else if(passJcb.isSelected() && !failJcb.isSelected() && !unTestJcb.isSelected()){
            state = 1;
        }else if(!passJcb.isSelected() && failJcb.isSelected() && !unTestJcb.isSelected()){
            state = 2;
        }else if(!passJcb.isSelected() && !failJcb.isSelected() && unTestJcb.isSelected()){
            state = 4;
        }else if(!passJcb.isSelected() && !failJcb.isSelected() && !unTestJcb.isSelected()){
            state = 0;
        }
    }

    public static void main(String[] args) {
        JFrame jFrame = new MainFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
