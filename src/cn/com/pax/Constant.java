package cn.com.pax;

/**
 * Created by luohl on 2017-12-4
 */
public interface Constant {
   // int WIDTH  = 1600;
    int WIDTH  = 1200;
    //int HEIGHT = 900;
    int HEIGHT = 850;
    int WIDTH1 = 500;
    int HEIGHT1 = 500;
    int SOFTWIDTH = 310;
    int SOFTHEIGHT = 120;
    String START = "开始";
    String OTHER = "其他";
    String HISTORY = "历史";
    String RECORD = "记录";
    //ICON
    String PAX = "icon/pax.jpg";
    String LOGO = "icon/logo.png";
    String STARTPATH  = "icon/start.jpg";
    String STOPPATH  = "icon/stop.png" ;
    String RUNPATH  = "icon/run.png";
    String ALLCHECK =  "icon/all_select.png";
    String PARTCHECK = "icon/part_select.png";
    String INVERSECHECK = "icon/all_remove.png";
    String ALLDELTE = "icon/all_delete.png";
    String PARTDELTE ="icon/part_delete.png";
    String CLEARSCREEN = "icon/Trash.png";
    String SOFTWARE = "icon/about_software.png";
    String []CONLUNMNSNAME = {"选择","案例编号","测试结果","ID"};
    String  DIRICON = "icon/folder.gif";
    //文件
    String CONFIG ="config.txt";
    String TESTCRIPT = "BCTC";

    String OPTIMADEPTH ="optimal_depth.txt";
    String OPTIMADEPTH1 ="optimal_depth1.txt";

    String CASEXML = "CaseList.xml";
    String CASEXMLBAK = "CaseListbak.xml";

   String FIRSTDIR = "BCTC";
   String DEBUG = "Debug";
   String SECONDDIR1= "条码测试";
   String SECONDDIR2 = "条码调试";
   String TESTTYPE = "银联认证--受理终端条码识读能力测试";

    //用例状态
    String PASS = "icon/pass.gif";
    String FAIL = "icon/fail.gif";
    String UNTEST = "icon/page.gif";
    //字体大小
    int FONTSIZE = 14 ;
    int FONTTITLE = 24;
    //点击事件
    String STARTEXE = "startExe";
    String STOPTEST = "stopExe";
    String CLEARSCREE = "clearscreen";
    String ALLSELECT = "all_select";
    String NOSELECT = "no_select";
    String ALLDELETE = "all_delete";
    String PARTSELECT = "part_select";
    String NOPARTSELECT ="nopart_select";
    String SOFTWAREINFO = "softwareinfo";
    //配置文件
    String CFG = "config/PC2C42Phone.properties";
    String C4IP = "IP_C4";
    String C4PORT= "PORT_C4";
    String PHONEIP = "IP_PHONE";
    String PHONEPORT = "PORT_PHONE";

    String ENGINECFG = "config/engine_config.properties";
    //测试脚步路径
    String SCRIPTPATH = "BCTC/Debug/条码测试/";
    String SCRIPDEBUGTPATH = "BCTC/Debug/条码调试/";
    //串口写命令
    String SENDPOS = "10109910";
    int TIMEOUT = 15000 ;
    int READEXTRATIME = 300;
    int MAXTIME = 2000;
    //测试发送的命令
    String SHOW = "showimage ";
    String CLEAR = "clearscreen";
    String SETBG = "setbright ";
    String GENERATE = "generatecode";

    String ORIGIN = "backp0";
    String DEPTHCMD = "dofone ";
    String BARCODECMD = "movexyz 0 -";


    String INITRATE = "inityrate";
    String RATECMD = "ymrate";

    String RIGHTLEFT0 = "rightleft45 0 ";
    String RIGHTLEFT1 = "rightleft45 1 ";
    String UPDOWN0 = "updown45 0 ";
    String UPDOWN1 = "updown45 1 ";
    int ANGLE = 45;
    String LEFT = "左翻";
    String RITHT ="右翻";
    String UP = "上翻";
    String DOWN = "下翻";
    String INIT360 = "initr360 ";
    String ROTATE360 = "rotate360 0 ";
    String ROTATE361 ="rotate360 1 ";
    String RLROLL = "rlroll ";
    String UDROLL = "udroll ";

    //软件信息
     String versionStr = "12.00.5";
     String BuildStr = versionStr + "-" + "2017";
     String titleStr = "百富BCTC条码终端自动测试软件";

     // case信息相关的
     String IMG = "文件名";
     String IMGCONTEND = "内容";
     String CODETYPE = "码制类型";
     String SCANTEST = "ScanTest";

     String VERSION = "版本";
     String ERRLEVEL = "容错";
     String CODEDATA = "数据";
     String PRECISION = "精度";
     String RATIO = "窄宽比";
     String FIXATIONWIDTH = "长度";
     String BRIGHT = "亮度";

     String CHARSET = "字符集";
     String BG = "背景色";
     String FG = "前景色";
     String GENERTYPE ="QR Code";

     String QR_CODE ="QRCode";
     String CODE_39 ="Code39";
     String CODE_128 ="Code128";

     //excel
    String EXCEL = "config/data.xlsx";

    //debug配置
   String NUM = "次数";
   String SPEED ="移动速度";
   String ROTATETYPE = "偏转类型";
   String ROTATEANGEL = "角度";
   String ROTATE_360 = "旋转角度";

   //编码类型
  String NUMTYPE = "数字";
  String ALPHTYPE = "字母";
  String BYTETYPE = "Byte";
  //报告配置
  String REPORTCFG = "config/report_col.properties";
  String REPORTBTN = "生成报告";

}
