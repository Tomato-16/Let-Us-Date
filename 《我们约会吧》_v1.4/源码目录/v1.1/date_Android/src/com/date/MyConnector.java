package com.date;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MyConnector {
	Socket socket = null; // ����Socket����
	DataInputStream din = null; // ������������������
	DataOutputStream dout = null; // �����������������

	public MyConnector(String address, int port) {
		try {
			socket = new Socket();
			SocketAddress isa = new InetSocketAddress(address, port);
			socket.connect(isa, 20000);
			// socket = new Socket(address,port);
			// socket.setSoTimeout(2000);
			din = new DataInputStream(socket.getInputStream()); // ���������
			dout = new DataOutputStream(socket.getOutputStream()); // ��������
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �������Ͽ����ӣ��ͷ���Դ
	public void sayBye() {
		try {
			dout.writeUTF("<#USER_LOGOUT#>"); // �����Ͽ���Ϣ
			din.close();
			dout.close();
			socket.close();
			socket = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}