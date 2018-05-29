package cn.com.pax.log;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    private File logFile;
    private  String logFileName ;//为拿到一致的文件名

    public LogWriter(String caseName) {
        logFileName = caseName + "-" + getFileFormat() + ".html";
        logFile = new File(logFileName);
    }

    public String getLogFileName() {

        return logFileName;
    }

    public void append(String str) throws IOException {
        append(str, false);
    }

    public void append(String conntent, boolean pre) throws IOException {
        if(!logFile.exists()) {
            File parentDir = new File(logFile.getParent());
            if(!parentDir.exists()) {
                parentDir.mkdirs();
            }
        }
        String string = conntent + "\r\n";
        if(pre) {
            string = getCommonFormat() + " " + string;
        }
        FileUtils.write(logFile, string, Charset.forName("UTF-8"), true);
    }

    private static String getDate(String format) {
        SimpleDateFormat sm = new SimpleDateFormat(format);
        return sm.format(new Date());
    }

    public static String getFileFormat() {
        return getDate("yyyy-MM-dd-HH-mm-ss");
    }

    public static String getCommonFormat() {
        return getDate("yyyy-MM-dd HH:mm:ss");
    }

    public static void main(String[] args) throws  IOException{
        //LogWriter logWriter = new LogWriter(".\\trace\\JSCS001");
        String str ="E:\\IDEA WorkSpace\\PAX_BCTCTestCode\\银联认证--受理终端条码识读能力测试-百富计算机技术（深圳有限公司）-S900-2017-12-13-09-47-38\\config.txt";
        String path = str.split(Constant.CONFIG)[0];
        System.out.println("path的值是：---"+ path + "，当前方法=main.LogWriter()");
        LogWriter  logWriter = new LogWriter(path+"\\trace\\"+"JSCS001");
        logWriter.append("123");
        logWriter.append("456");
        logWriter.append("789", true);
    }

}
