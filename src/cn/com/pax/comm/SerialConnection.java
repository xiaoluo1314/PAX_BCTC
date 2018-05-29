package cn.com.pax.comm;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class SerialConnection  {
	private SerialParameters parameters;
	private OutputStream os;
	private InputStream is;
	private  SerialPort sPort;
	private boolean open;

	public static final int UNLIMITTIMEOUT = 2000000000;

	public SerialConnection(SerialParameters parameters) {
		this.parameters = parameters;
		open = false;
	}

	public void openConnection() throws NoSuchPortException, PortInUseException, IOException, UnsupportedCommOperationException {
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(parameters.getPortName());
		sPort = (SerialPort) portId.open("PAX", 3000);
		setConnectionParameters(parameters);
		os = sPort.getOutputStream();
		is = sPort.getInputStream();
		open = true;
	}

	public void setConnectionParameters(SerialParameters parameters)  throws  UnsupportedCommOperationException {
		sPort.setSerialPortParams(parameters.getBaudRate(), parameters.getDatabits(), parameters.getStopbits(),
				parameters.getParity());
	}

	public static List<String> getCommInfo() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		List<String> ports = new ArrayList<>();
		while(portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				ports.add(portIdentifier.getName());
			}
		}
		return ports;
	}

	private static String getPortTypeName(int portType) {
		switch (portType) {
			case CommPortIdentifier.PORT_I2C:
				return "I2C";
			case CommPortIdentifier.PORT_PARALLEL: // 并口
				return "Parallel";
			case CommPortIdentifier.PORT_RAW:
				return "Raw";
			case CommPortIdentifier.PORT_RS485: // RS485端口
				return "RS485";
			case CommPortIdentifier.PORT_SERIAL: // 串口
				return "Serial";
			default:
				return "unknown type";
		}
	}

	public static Map<String, String> getCommType() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		Map<String, String> portMap = new HashMap<>();
		while(portEnum.hasMoreElements()) {
			CommPortIdentifier portIdentifier = portEnum.nextElement();
			portMap.put(portIdentifier.getName(), getPortTypeName(portIdentifier.getPortType()));
		}
		return portMap;
	}

	public SerialParameters getParameters() {
		return parameters;
	}

	public void closeConnection() {
		if (!open) {
			return;
		}
		if (sPort != null) {
			try {
				if(is != null)
					is.close();
				if(os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sPort.close();
			sPort = null;
		}
		open = false;
	}

	public boolean isOpen() {
		return open;
	}

	public void enableReadTimeout(int timeout) throws UnsupportedCommOperationException {
		sPort.enableReceiveTimeout(timeout);
	}

	public boolean isExistComm(){
		try {
			CommPortIdentifier.getPortIdentifier(parameters.getPortName());
			return true;
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void readExtraData(int timeoutMs, int maxTime) throws UnsupportedCommOperationException, IOException
	{
		int num=0;
		byte[] buf = new byte[16];
		enableReadTimeout(timeoutMs);
		long startTime = System.currentTimeMillis(), endTime = 0;
		while(true) {
			num = is.read(buf);
			if (num <= 0) {
				break;
			}
			if(maxTime < UNLIMITTIMEOUT) {
				endTime = System.currentTimeMillis();
				if ((endTime - startTime) > maxTime)
					return;
			}
		}
	}

	//默认缓存1000字节，如果超过，则需要重新考虑实现
	public String serialRead(int timeoutMs) throws UnsupportedCommOperationException, IOException {
		byte[] buffer = new byte[4096];
		int num;
		num = serialRead(buffer, timeoutMs);
		return new String(buffer, 0 , num,"gbk");
	}

//    public int serialRead(byte[] recvBuf, int timeoutMs) throws UnsupportedCommOperationException, IOException {
//		int num=0, len = 0, txLen = recvBuf.length;
//		enableReadTimeout(timeoutMs);
//		while(true) {
//			len = is.read(recvBuf, num, txLen - num);
//			if (len <= 0) { break; }
//			num += len;
//			if(num >= txLen) { break; }
//		}
//		return num;
//	}

	public int serialRead(byte[] recvBuf, int timeoutMs) throws UnsupportedCommOperationException, IOException {
		int num=0, len = 0, txLen = recvBuf.length;
		int count = timeoutMs / 500 + 1 ,i=0;
		boolean isRecv = false;
		enableReadTimeout(500);
		while(true) {
			len = is.read(recvBuf, num, txLen - num);
			if (len <= 0) {
				i++;
				if(isRecv) break;
				if(i >= count) break;
				else continue;
			}
			num += len;
			isRecv = true;
			if(num >= txLen) { break; }
		}
		return num;
	}

	public void serailWrite(byte []buf) throws IOException {
		os.write(buf);
	}
	
	public static void main(String[] args) throws Exception {
		byte[] buffer = new byte[64];

        SerialParameters parameters = new SerialParameters("COM11");

        SerialConnection connection = new SerialConnection(parameters);
        connection.openConnection();
        boolean suc = connection.isOpen();

        connection.serailWrite("POLL".getBytes());

        long st = System.currentTimeMillis();
        //int sum = connection.serialRead(buffer, 3000);
		String str = connection.serialRead(2000);
		long ed = System.currentTimeMillis();
        System.out.println(ed - st);
        System.out.println(str);
        connection.closeConnection();
		for (String s : getCommInfo()) {
			System.out.println("s的值是：---"+s  + "，当前方法=main.SerialConnection()");
		}
	}

}
