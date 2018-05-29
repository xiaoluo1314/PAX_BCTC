package cn.com.pax.comm;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
/**
 * 串口操作公共类
 *
 * @author luohl
 * @create 2017-12-11-17:18
 */
public class SerialOperation {
    private String serialNum;
    private  SerialParameters serialParameters;
    private SerialConnection connection;
    private boolean isOpen;
    private static final SerialOperation serialOperation = new SerialOperation();
    private SerialOperation(){}
    public static SerialOperation getInstance(){
        return serialOperation;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public boolean openSerial(){
        try {
            serialParameters = new SerialParameters(serialNum);
            connection = new SerialConnection(serialParameters);
            connection.openConnection();
            isOpen = true;
            return isOpen;
        } catch (NoSuchPortException e) {
            e.printStackTrace();
            isOpen = false;
            return isOpen;
        } catch (PortInUseException e) {
            e.printStackTrace();
            isOpen = false;
            return isOpen;
        } catch (IOException e) {
            e.printStackTrace();
            isOpen = false;
            return isOpen;
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            isOpen = false;
            return isOpen;
        }
    }

    public SerialConnection getConnection() {
        return connection;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void closeSerial(){
        if(isOpen()){
            connection.closeConnection();
        }
    }

    public static void main(String[] args) {
        SerialOperation serialOperation = SerialOperation.getInstance();
        serialOperation.setSerialNum("COM1");
        boolean suc = serialOperation.openSerial();
        System.out.println("suc的值是：---"+ suc + "，当前方法=main.SerialOperation()");
        serialOperation.closeSerial();
    }
}
