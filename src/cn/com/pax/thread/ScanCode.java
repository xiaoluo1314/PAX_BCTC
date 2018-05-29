package cn.com.pax.thread;

import cn.com.pax.Constant;
import cn.com.pax.comm.SerialConnection;

import java.io.IOException;

public class ScanCode implements Runnable{
    private SerialConnection serialConnection;
    private String readContent;

    public ScanCode(SerialConnection connection) {
        this.serialConnection = connection;
        readContent = "";
    }

    @Override
    public void run() {
        try {
            setReadContent("");

            serialConnection.serailWrite(Constant.SENDPOS.getBytes());
            setReadContent(serialConnection.serialRead(Constant.TIMEOUT));

//            System.out.println();
//            if (getReadContent().equalsIgnoreCase("")){
//                setReadContent("FFFF");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            setReadContent(null);
        }
    }

    public synchronized String getReadContent() {
        return readContent;
    }

    public synchronized void setReadContent(String readContent) {
        this.readContent = readContent;
    }
}
