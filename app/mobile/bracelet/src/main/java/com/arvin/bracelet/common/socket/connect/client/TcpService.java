package com.arvin.bracelet.common.socket.connect.client;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class TcpService {
	public static final String TAG = TcpService.class.getSimpleName();
	public static final int READ_TIMEOUT = 1000 * 10;
	public static final int CONNECT_TIMEOUT = 1000 * 10;
	private static TcpService tcpService;

	OutputStream outputStream = null;
	Socket socket = null;
	InputStream inputStream = null;

	public static TcpService getInstance() {
		if (tcpService == null) {
			tcpService = new TcpService();
		}
		return tcpService;
	}

	private void initSocket(String ip, int port) throws Exception {
		Log.d(TAG, ip + ":" + port);
		try {
			socket = new Socket();
			socket.setSoTimeout(READ_TIMEOUT);
			socket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), CONNECT_TIMEOUT);
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			throw new Exception("time out!");
		}
	}

	private String sendData(String data) throws Exception {
		StringBuilder result = new StringBuilder("");
		try {
			outputStream.write(data.getBytes("UTF-8"));
			byte[] b = new byte[1024];
			int reads = inputStream.read(b);
			while (reads > 0) {
				byte[] bytes = Arrays.copyOfRange(b, 8, reads);
				String temp = new String(bytes);
				result.append(temp);
				reads = 0;
				b = new byte[1024];
				reads = inputStream.read(b);
			}
			Log.d(TAG, result.toString());
			return result.toString();
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
			e.printStackTrace();
			throw new Exception("send error");
		}
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
