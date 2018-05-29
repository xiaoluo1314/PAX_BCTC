package cn.com.pax.comm;

import cn.com.pax.Constant;
import cn.com.pax.utils.FileUtils;

/**
 * Socket 相关的操作
 *
 * @author luohl
 * @create 2017-12-11-18:39
 */
public class SocketOperation {

    public static  boolean isPc2C4Conn;
    public static  boolean isPc2PhoneConn;
    private static C4Socket pc2C4;
    private static C4Socket pc2Phone;

    /*private static final SocketOperation socketOperation = new SocketOperation();
    private  SocketOperation(){}
    public static SocketOperation  getInstance(){
        return socketOperation;
    }*/
    public static  C4Socket  openC4SocKet(){
        FileUtils.readCfg();
        if (FileUtils.getIpC4().trim()!=null && FileUtils.getPortC4().trim()!=null){
            pc2C4 = new C4Socket(FileUtils.getIpC4().trim(),Integer.parseInt(FileUtils.getPortC4().trim()));
            //isPc2C4Conn = pc2C4.openConn();
            return pc2C4;
        }else{
            pc2C4 = null;
            return pc2C4;
        }
    }

    public static boolean openPhoneSocKet(){
        FileUtils.readCfg();
        if (FileUtils.getIpPhone().trim()!=null && FileUtils.getPortPhone().trim()!=null){
            pc2Phone = new C4Socket(FileUtils.getIpPhone().trim(),Integer.parseInt(FileUtils.getPortPhone().trim()));
            isPc2PhoneConn = pc2Phone.openConn();
            return isPc2PhoneConn;
        }else{
            isPc2PhoneConn = false;
            return isPc2PhoneConn;
        }
    }


    public static C4Socket getPc2C4() {
        return pc2C4;
    }

    public static void setPc2C4(C4Socket pc2C4) {
        SocketOperation.pc2C4 = pc2C4;
    }

    public static C4Socket getPc2Phone() {
        return pc2Phone;
    }

    public static boolean isPc2C4Conn() {
        return isPc2C4Conn;
    }

    public static void setIsPc2C4Conn(boolean isPc2C4Conn) {
        SocketOperation.isPc2C4Conn = isPc2C4Conn;
    }

    public static boolean isPc2PhoneConn() {
        return isPc2PhoneConn;
    }

    public static void pc2C4Close(){
        if(isPc2C4Conn()){
            System.out.println("pc2C4Close");
            pc2C4.closeConn();
            pc2C4 = null;
            isPc2C4Conn = false;
        }
    }

    public static void pc2PhoneClose(){
        if(isPc2PhoneConn()){
            pc2Phone.closeConn();
            pc2Phone = null;
            isPc2PhoneConn = false;
        }
    }

    public static void main(String[] args) {
//        boolean suc = SocketOperation.openC4SocKet();
//        System.out.println("suc的值是：---"+ suc + "，当前方法=main.SocketOperation()");
//        SocketOperation.pc2C4Close();

        boolean suc1 = SocketOperation.openPhoneSocKet();
        System.out.println("suc1的值是：---"+ suc1 + "，当前方法=main.SocketOperation()");
        C4Socket pc2Phone = SocketOperation.getPc2Phone();
        String fileName = Constant.SHOW+"Code_39_1920x1080.png";
         String revPhone =  pc2Phone.writeReadOneLine(fileName);
         System.out.println("revPhone = " +revPhone);
        SocketOperation.pc2PhoneClose();
    }
}
