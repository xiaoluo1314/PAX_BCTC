package cn.com.pax.comm;

import gnu.io.*;

public class SerialParameters {
	private String portName;
	private int baudRate;
	private int databits;
	private int stopbits;
	private int parity;

	public SerialParameters(String portName) {
		this.portName = portName;
		this.baudRate = 115200;
		this.databits = SerialPort.DATABITS_8;
		this.stopbits = SerialPort.STOPBITS_1;
		this.parity = SerialPort.PARITY_NONE;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getDatabits() {
		return databits;
	}

	public void setDatabits(int databits) {
		if(databits != SerialPort.DATABITS_5 ||
				databits != SerialPort.DATABITS_6 ||
				databits != SerialPort.DATABITS_7 ||
				databits != SerialPort.DATABITS_8)
			throw new IllegalArgumentException("setDatabits Exception: " + databits);
		this.databits = databits;
	}

	public int getStopbits() {
		return stopbits;
	}

	public void setStopbits(int stopbits) {
		if(stopbits != SerialPort.STOPBITS_1 ||
				stopbits != SerialPort.STOPBITS_1_5 ||
				stopbits != SerialPort.STOPBITS_2)
			throw new IllegalArgumentException("setStopbits Exception: " + stopbits);
		this.stopbits = stopbits;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		if(parity != SerialPort.PARITY_NONE ||
				parity !=  SerialPort.PARITY_EVEN ||
				parity != SerialPort.PARITY_ODD)
			throw new IllegalArgumentException("setParity Exception: " + parity);
		this.parity = parity;
	}


	@Override
	public String toString() {
		return "SerialParameters{" +
				"portName='" + portName + '\'' +
				", baudRate=" + baudRate +
				", databits=" + databits +
				", stopbits=" + stopbits +
				", parity=" + parity +
				'}';
	}
}
