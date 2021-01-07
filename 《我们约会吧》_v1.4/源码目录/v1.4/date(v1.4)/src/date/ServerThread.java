package date;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread{
	public ServerSocket ss;		//����ServerSocket����
	public boolean flag = false;
	
	public ServerThread(ServerSocket ss){	//������
		this.ss = ss;	
		flag = true;
	}
	public void run(){
		while(flag){
			try{
				Socket socket = ss.accept();
				System.out.println("serverAgent to be Started...");
				ServerAgent sa = new ServerAgent(socket);
				sa.start();
				System.out.println("serverAgent Start...");
			}
			catch(SocketException se){
				try{
					ss.close();
					ss = null;
					System.out.println("ServerSocket closed");
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
