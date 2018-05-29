package cn.com.pax.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class C4Socket {
	private String host;
	private int port;
	
	public C4Socket(String host, int port) {
		this.host = host;
		this.port = port;
		sock = null;
	}

	public boolean isConnect() {
		return sock != null;
	}
	
	public boolean openConn() {
		try {
			sock = new Socket(host, port);
			return true;
		}
		catch(UnknownHostException e) {
			e.printStackTrace();
			sock = null;
			return false;
		}
		catch(IOException e) {
			e.printStackTrace();
			sock = null;
			return false;
		}
	}
	
	public void closeConn() {
		if(sock != null) {
			try {
				sock.close();
				sock = null;
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public String readOneLine() {
		if(sock == null) return "error null";

		try {
			InputStream inputStream = sock.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			return reader.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
			return "error read";
		}
	}

	public String writeReadOneLine(String xString) {
		if(sock == null) return "error null";
		try {
			OutputStream outputStream = sock.getOutputStream();
			PrintWriter writer = new PrintWriter(outputStream, true);
			writer.println(xString);
			InputStream inputStream = sock.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = reader.readLine();
			if(result == null) {
				result = "error result";
			}
			return result;
		}
		catch(SocketException e) {
			e.printStackTrace();
			return "error network";
		}
		catch(IOException e) {
			e.printStackTrace();
			return "error IO";
		}
		catch(Throwable e) {
			e.printStackTrace();
			return "error readwrite";
		}
	}

	public String sendCodeCmd(String intentString, String startString) {
		if(sock == null) return "error null";
		int count = 0;
		while(count < 3) {
			try {
				OutputStream outputStream = sock.getOutputStream();
				outputStream.write(intentString.getBytes());
				InputStream inputStream = sock.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String result = reader.readLine();
				if(startString != null) {
					outputStream.write(startString.getBytes());
					result += reader.readLine();
				}
				return "ok: " + result;
			}
			catch(SocketException e) {
				e.printStackTrace();
				count++;
				if(count < 3) {
					sleepTime(count);
					openConn();
				} else {
					return "error network";
				}
			}
			catch(IOException e) {
				e.printStackTrace();
				count++;
				if(count < 3) {
					sleepTime(count);
					openConn();
				} else {
					return "error IO";
				}
			}
			catch(Throwable e) {
				e.printStackTrace();
				return "error readwrite";
			}
		}
		return "error count";
	}

	public static void sleepTime(int count) {
		try {
			Thread.sleep(1000 * count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean writeOneLine(String data) {
		if(sock == null) return false;

		try {
			OutputStream outputStream = sock.getOutputStream();
			PrintWriter writer = new PrintWriter(outputStream, true);
			writer.println(data);
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//为了PC2Phone
	public boolean writeOneLineRemoveBr(String data){
		if(sock == null) return false;
		try {
			OutputStream outputStream = sock.getOutputStream();
			PrintWriter writer = new PrintWriter(outputStream, true);
			writer.print(data);
			return true;
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//为了PC2Phone
	public String writeReadOneLineRemoveBr(String xString) {
		if(sock == null) return "error null";
		try {
			OutputStream outputStream = sock.getOutputStream();
			PrintWriter writer = new PrintWriter(outputStream, true);
			writer.print(xString);
			InputStream inputStream = sock.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = reader.readLine();
			if(result == null) {
				result = "error result";
			}
			return result;
		}
		catch(SocketException e) {
			e.printStackTrace();
			return "error network";
		}
		catch(IOException e) {
			e.printStackTrace();
			return "error IO";
		}
		catch(Throwable e) {
			e.printStackTrace();
			return "error readwrite";
		}
	}


	private Socket sock;
}
