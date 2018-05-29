package cn.com.pax.barcode;

/**
 * @author luohl
 * @create 2018-05-15-10:42
 */

public class Test {
    public static void main(String[] args)throws Exception {
        //Runtime runtime = Runtime.getRuntime();
        //runtime.exec("notepad");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("notepad");
        processBuilder.start();
        //打开记事本
//        ProcessBuilder pBuilder = new ProcessBuilder("notepad");
//        pBuilder.start();
    }
}
    