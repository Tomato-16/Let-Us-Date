package date;
import static date.ConstantUtil.CancelDate_SUCCESS;
import static date.ConstantUtil.DIARY_SUCCESS;
import static date.ConstantUtil.INFO_MODIFY_SUCCESS;
import static date.ConstantUtil.PWD_MODIFY_SUCCESS;
import static date.ConstantUtil.PubDate_SUCCESS;
import static date.ConstantUtil.REGISTER_FAIL;
import static date.ConstantUtil.WRITECHAT_SUCCESS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Blob;
import java.util.ArrayList;

public class ServerAgent extends Thread{
	public Socket socket;
	public DataInputStream din;
	public DataOutputStream dout;
	boolean flag = false;
	
	public ServerAgent(Socket socket){
		this.socket = socket;
		try {
			this.din = new DataInputStream(socket.getInputStream());
			this.dout = new DataOutputStream(socket.getOutputStream());
			flag =true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//�������߳�ִ�з���
	public void run(){
		while(flag){
			try {
				String msg = din.readUTF();			//���տͻ��˷�������Ϣ
				System.out.println("�յ�����Ϣ�ǣ�"+msg);	
				if(msg.startsWith("<#LOGIN#>")){				//��ϢΪ��¼
					String content = msg.substring(9);			//�����Ϣ����
					String [] sa = content.split("\\|");
					ArrayList<String> result = DBUtil.checkLogin(sa[0], sa[1]);
					if(result.size()>1){			//��¼�ɹ�
						StringBuilder sb = new StringBuilder();
						sb.append("<#LOGIN_SUCCESS#>");
						for(String s:result){
							sb.append(s);
							sb.append("|");
						}
//						String updateresult = DBUtil.updateState(sa[0]);	//
//						if(updateresult.equals(UPDATE_STATE_SUCCESS)){		//��������û�����״̬
//							dout.writeUTF("<#STATUS_SUCCESS#>");
//						}
//						else{
//							dout.writeUTF("<#STATUS_FAIL#>");			//�����û�����״̬����ʧ����Ϣ
//						}
						String loginInfo = sb.substring(0,sb.length()-1);
						dout.writeUTF(loginInfo);			//�����û��Ļ�����Ϣ			
					}
					else{				//��¼ʧ��
						String loginInfo = "<#LOGIN_FAIL#>"+result.get(0);
						dout.writeUTF(loginInfo);
					}
				}
				else if(msg.startsWith("<#USER_LOGOUT#>")){			//��ϢΪ�û��ǳ�
					this.din.close();
					this.dout.close();
					this.flag = false;
					this.socket.close();
					this.socket = null;
				}
				else if(msg.startsWith("<#REGISTER#>")){			//��ϢΪ�û�ע��
					msg = msg.substring(12);	//����ַ���ֵ
					String [] sa = msg.split("\\|");	//�и��ַ���
					String regResult = DBUtil.registerUser(sa[0], sa[1], sa[2], sa[3],sa[4],false,"2");
					System.out.println();
					if(regResult.equals(REGISTER_FAIL)){		//ע��ʧ��
						dout.writeUTF("<#REG_FAIL#>");
					}
					else{
						dout.writeUTF("<#REG_SUCCESS#>"+regResult);
					}
				}	
				else if(msg.startsWith("<#PubDate#>")){					//��ϢΪ������Լ��
					msg = msg.substring(11);				//�����Ϣ����
					String [] sa = msg.split("\\|");		//����ַ�������
					String reply = "";
					Date date = DBUtil.getDate(sa[4]);
					System.out.println(date);
					if(date==null){
						///�޸�һ������
						String result = DBUtil.writeNewDate(sa[0], sa[1], Integer.parseInt(sa[2]),sa[3],Integer.parseInt(sa[4]));
						
						if(result.equals(PubDate_SUCCESS)){			//Լ�ᷢ���ɹ�
							reply = "<#PubDate_SUCCESS#>";
						}
						else {
							reply = "<#PubDate_FAIL#>";
						}
						
					}
					else
						reply ="<#PubDate_ONCE#>";
					
					dout.writeUTF(reply);					//�����ظ���Ϣ
				}
				else if(msg.startsWith("<#CancelDate#>")){					//��ϢΪ������Լ��
					msg = msg.substring(14);				//�����Ϣ����
//					String [] sa = msg.split("\\|");		//����ַ�������
					String reply = "";
					Date date = DBUtil.getDate(msg);
					System.out.println(date);
					if(date==null){
						reply = "<#PubDate_NEVER#>";	
					}
					else{
						String result = DBUtil.deleteDate(msg);
						
						if(result.equals(CancelDate_SUCCESS)){			//Լ�ᷢ���ɹ�
							reply = "<#CancelDate_SUCCESS#>";
						}
						else {
							reply = "<#CancelDate_FAIL#>";
						}
					}
						
					dout.writeUTF(reply);					//�����ظ���Ϣ
				}
				else if(msg.startsWith("<#UserInfo#>")){			//��ϢΪ����û���Ϣ
					msg = msg.substring(12);
					User u = DBUtil.getUser(msg);	//����û���Ϣ
					//dout.writeInt(list.size());		//��֪�ͻ��˺����б�ĳ���
					//for(int i=0;i<list.size();i++){
						//User u = list.get(i);			//��øô���User����
						StringBuilder sb = new StringBuilder();	//����StringBuilder
						sb.append(u.u_no);
						sb.append("|");
						sb.append(u.u_name);
						sb.append("|");
						sb.append(u.u_email);
						sb.append("|");						
						sb.append(u.h_id);
						sb.append("|");						
						sb.append(u.u_sex);
						sb.append("|");						
						sb.append(u.u_birthday);
						sb.append("|");						
						sb.append(u.u_latitude);
						sb.append("|");						
						sb.append(u.u_longitude);
						sb.append("|");						
						sb.append(u.u_pwd);
						
						System.out.println(sb.toString());
						dout.writeUTF(sb.toString());			//����������Ϣ
						Blob blob = DBUtil.getHeadBlob(u.h_id);	//���ָ���û���ͷ��Blob
						byte [] buf = blob.getBytes(1l, (int)blob.length());	//����ֽ�����
						dout.writeInt(buf.length);
						dout.write(buf);
						System.out.println(buf);
						dout.flush();				//��֪���������������
					//}
				}
				else if(msg.startsWith("<#DATE_LIST#>")){			//��ϢΪ���Լ���б�
					
					msg = msg.substring(13);					//��ȡ��Ϣ����
					String [] sa = msg.split("\\|");			//�ָ��ַ���
					ArrayList<Date> dateList = DBUtil.getDateList(sa[0], Integer.valueOf(sa[1]), 5);
					int size = dateList.size();
					dout.writeInt(size);						//��ͻ��˷���Լ���б���
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<size;i++){
						
						Date d= dateList.get(i);
						sb.append(d.d_id);			
						sb.append("|");
						sb.append(d.d_style);		
						sb.append("|");
						sb.append(d.d_sex);		
						sb.append("|");
						sb.append(d.d_age);
						sb.append("|");
						sb.append(d.d_distance);
						sb.append("|");
						sb.append(d.u_name);	
						sb.append("|");
						sb.append(d.h_id);
						sb.append("|");
						sb.append(d.u_no);
						sb.append("|");
						sb.append(d.u_latitude);
						sb.append("|");
						sb.append(d.u_longitude);
						sb.append(",");
						System.out.println(sb.toString());
						
						
//						Blob blob = DBUtil.getHeadBlob(d.h_id);	//���ָ���û���ͷ��Blob
//						byte [] buf = blob.getBytes(1l, (int)blob.length());	//����ֽ�����
//						System.out.println(buf.length);
//						dout.writeInt(buf.length);
//						dout.write(buf);						
						dout.flush();	
					}	
					dout.writeUTF(sb.toString());
				}
				else if(msg.startsWith("<#CHAT#>")){					//��ϢΪ������Լ��
					msg = msg.substring(8);				//�����Ϣ����
					String [] sa = msg.split("\\|");		//����ַ�������
					String reply = "";
					
						String result = DBUtil.writeNewChat(sa[0], sa[1], sa[2]);
						
						if(result.equals(WRITECHAT_SUCCESS)){			//Լ�ᷢ���ɹ�
							reply = "<#WRITECHAT_SUCCESS#>";
						}
						else {
							reply = "<#WRITECHAT_FAIL#>";
						}					
					
					dout.writeUTF(reply);					//�����ظ���Ϣ
				}
				else if(msg.startsWith("<#CHAT_LIST#>")){			//��ϢΪ���Լ���б�
					
					msg = msg.substring(13);					//��ȡ��Ϣ����
					String [] sa = msg.split("\\|");			//�ָ��ַ���
					ArrayList<Chat> chatList = DBUtil.getChatList(sa[0], sa[1],Integer.valueOf(sa[2]), 5);
					int size = chatList.size();
					dout.writeInt(size);						//��ͻ��˷����б���
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<size;i++){
						
						Chat c= chatList.get(i);
						sb.append(c.c_id);
						sb.append("|");
						sb.append(c.u_ano);			
						sb.append("|");
						sb.append(c.u_bno);		
						sb.append("|");
						sb.append(c.c_text);
						sb.append(",");
						DBUtil.updateChatState(c.c_id);
						
						System.out.println(sb.toString());
										
					}	
					dout.writeUTF(sb.toString());		
				}
				else if(msg.startsWith("<#CHAT_IF#>")){			//��ϢΪ���Լ���б�
					
					msg = msg.substring(11);					//��ȡ��Ϣ����
					//String [] sa = msg.split("\\|");			//�ָ��ַ���
					ArrayList<Chat> chatList = DBUtil.getNewChatList(msg);
					int size = chatList.size();
					dout.writeInt(size);						//��ͻ��˷����б���
//					for(int i=0;i<size;i++){
//						StringBuilder sb = new StringBuilder();
//						Chat c= chatList.get(i);
//						sb.append(c.c_id);
//						sb.append("|");
//						sb.append(c.u_ano);			
//						sb.append("|");
//						sb.append(c.u_bno);		
//						sb.append("|");
//						sb.append(c.c_text);
//											
//						System.out.println(sb.toString());
//						dout.writeUTF(sb.toString());						
//					}				
				}
				else if(msg.startsWith("<#UPDATE_GPS#>")){		//��ϢΪ�޸����Ȩ��
					msg = msg.substring(14);
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					int result = DBUtil.updateGPS(sa[0], sa[1],sa[2]);	//�޸�Ȩ��
					if(result == 1){		//�޸ĳɹ�
						dout.writeUTF("<#UPDATE_GPS_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#UPDATE_GPS_FAIL#>");
					}
				}
				else if(msg.startsWith("<#FRIEND_LIST#>")){			//��ϢΪ��ú����б�
					msg = msg.substring(15);
					ArrayList<User> list = DBUtil.getFiendList(msg);	//��ú��ѵ��б�
					dout.writeInt(list.size());		//��֪�ͻ��˺����б�ĳ���
					StringBuilder sb = new StringBuilder();	//����StringBuilder
					
					for(int i=0;i<list.size();i++){
						User u = list.get(i);			//��øô���User����
						sb.append(u.u_no);
						sb.append("|");
						sb.append(u.u_name);
						sb.append("|");
						sb.append(u.u_sex);
						sb.append("|");
						sb.append(u.u_birthday);
						sb.append("|");
						sb.append(u.h_id);
						sb.append(",");
						
						System.out.println(sb.toString());
//						Blob blob = DBUtil.getHeadBlob(u.h_id);	//���ָ���û���ͷ��Blob
//						byte [] buf = blob.getBytes(1l, (int)blob.length());	//����ֽ�����
//						dout.writeInt(buf.length);
//						dout.write(buf);
//						dout.flush();				//��֪���������������
					}
					dout.writeUTF(sb.toString());			//����������Ϣ
				}
				else if(msg.startsWith("<#PWD_MODIFY#>")){		//�û��޸�����
					msg = msg.substring(14);
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					String result = DBUtil.modify_pwd(sa[0], sa[1]);	//�޸�Ȩ��
					System.out.println(result);
					if(result.equals(PWD_MODIFY_SUCCESS)){		//�޸ĳɹ�
						dout.writeUTF("<#PWD_MODIFY_SUCCESS#>");
						System.out.println(result);
					}
					else{
						dout.writeUTF("<#PWD_MODIFY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#INFO_MODIFY#>")){		//�û��޸ĸ�����Ϣ
					msg = msg.substring(15);
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					String result = DBUtil.modify_info(sa[0], sa[1],sa[2],sa[3],sa[4]);	//�޸�Ȩ��
					System.out.println(result);
					if(result.equals(INFO_MODIFY_SUCCESS)){		//�޸ĳɹ�
						dout.writeUTF("<#INFO_MODIFY_SUCCESS#>");
						System.out.println(result);
					}
					else{
						dout.writeUTF("<#INFO_MODIFY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#NEW_DIARY#>")){					//��ϢΪ�������ռ�
					msg = msg.substring(13);				//�����Ϣ����
					String [] sa = msg.split("\\|");		//����ַ�������
					String result = DBUtil.writeNewDiary(sa[0], sa[1], sa[2]);
					String reply = "";
					if(result.equals(DIARY_SUCCESS)){			//Լ�ᷢ���ɹ�
						reply = "<#DIARY_SUCCESS#>";
					}
					else {
						reply = "<#DIARY_FAIL#>";
					}
					dout.writeUTF(reply);					//�����ظ���Ϣ
				}
//				else if(msg.startsWith("<#NEW_STATUS#>")){				//��ϢΪ��������
//					msg = msg.substring(14);			//��ȡ����
//					String [] sa = msg.split("\\|");	//�и��ַ���
//					
//				}
//				else if(msg.startsWith("<#FRIEND_LIST#>")){			//��ϢΪ��ú����б�
//					msg = msg.substring(15);
//					ArrayList<User> list = DBUtil.getFiendList(msg);	//��ú��ѵ��б�
//					dout.writeInt(list.size());		//��֪�ͻ��˺����б�ĳ���
//					for(int i=0;i<list.size();i++){
//						User u = list.get(i);			//��øô���User����
//						StringBuilder sb = new StringBuilder();	//����StringBuilder
//						sb.append(u.u_no);
//						sb.append("|");
//						sb.append(u.u_name);
//						sb.append("|");
//						sb.append(u.u_email);
//						sb.append("|");
//						sb.append(u.u_state);
//						sb.append("|");
//						sb.append(u.h_id);
//						dout.writeUTF(sb.toString());			//����������Ϣ
//						Blob blob = DBUtil.getHeadBlob(u.h_id);	//���ָ���û���ͷ��Blob
//						byte [] buf = blob.getBytes(1l, (int)blob.length());	//����ֽ�����
//						dout.writeInt(buf.length);
//						dout.write(buf);
//						dout.flush();				//��֪���������������
//					}
//				}
//				else 
//					if(msg.startsWith("<#VISITOR_LIST#>")){					//��ϢΪ��ʾ����ÿ�����
//					msg = msg.substring(16);			//��ȡ��Ϣ����
//					ArrayList<Visitor> visitorList = DBUtil.getVisitors(msg);		//��ȡ�ÿ��б�
//					int size = visitorList.size();
//					dout.writeInt(size);			//��֪�ͻ������ݵĳ���
//					for(int i=0;i<size;i++){		//�����ÿ��б�
//						StringBuilder sb = new StringBuilder();
//						Visitor v = visitorList.get(i);
//						sb.append(v.v_no);
//						sb.append("|");
//						sb.append(v.v_name);
//						sb.append("|");
//						sb.append(v.v_date);
//						sb.append("|");
//						sb.append(v.h_id);
//						dout.writeUTF(sb.toString());
//						Blob blob = DBUtil.getHeadBlob(v.h_id);	//���ͷ��Blob����
//						byte [] buf = blob.getBytes(1l, (int)blob.length());	//��ȡ�ֽ�����
//						dout.writeInt(buf.length);
//						dout.write(buf);		//���ֽ����鷢��
//						dout.flush();
//					}
//				}
//				else if(msg.startsWith("<#GET_DIARY#>")){			//��ϢΪ����ռ��б�
//					msg = msg.substring(13);					//��ȡ��Ϣ����
//					String [] sa = msg.split("\\|");			//�ָ��ַ���
//					ArrayList<Diary> diaryList = DBUtil.getUserDiary(sa[0], Integer.valueOf(sa[1]), 5);
//					int size = diaryList.size();
//					dout.writeInt(size);						//��ͻ��˷����ռǳ���
//					for(int i=0;i<size;i++){
//						StringBuilder sb = new StringBuilder();
//						Diary d= diaryList.get(i);
//						sb.append(d.rid);			//Լ��id
//						sb.append("|");
//						sb.append(d.title);		//Լ�����
//						sb.append("|");
//						sb.append(d.content);		//Լ������
//						sb.append("|");
//						sb.append(d.time);			//Լ�ᷢ��ʱ��
//						System.out.println(sb.toString());
//						dout.writeUTF(sb.toString());		//�����ռ��б�
//					}
//				}
				else if(msg.startsWith("<#GET_ALBUM_LIST#>")){			//��ϢΪ��ȡ����б�
					msg = msg.substring(18);			//��ȡ����
					ArrayList<String []> albumList = DBUtil.getAlbumList(msg);
					if(albumList.size() == 0){//����û������
						dout.writeUTF("<#NO_ALBUM#>");
					}
					else{			//�û�����б�Ϊ��
						StringBuilder sb = new StringBuilder();
						for(String [] sa:albumList){
							sb.append(sa[0]);
							sb.append("|");
							sb.append(sa[1]);
							sb.append("|");
							sb.append(sa[2]);
							sb.append("$");
						}
						dout.writeUTF(sb.substring(0, sb.length()-1));						
					}
				}
//				else if(msg.startsWith("<#GET_ALBUM#>")){				//��ϢΪ���ָ�����
//					msg = msg.substring(13);		//��ȡ����
//					int albumSize = DBUtil.getAlbumSize(msg);		//��ȡ��᳤��
//					dout.writeInt(albumSize);					//����᳤�ȷ����ͻ���
//					List<PhotoInfo> photoList = DBUtil.getPhotoInfoByAlbum(msg, 1, albumSize);//��ȡͼƬ��Ϣ
//					for(int i=0;i<albumSize;i++){				//ѭ����ȡͼƬ����
//						PhotoInfo pi = photoList.get(i);		//���ͼƬ��Ϣ
//						StringBuilder sb = new StringBuilder();
//						sb.append(pi.p_id);
//						sb.append("|");
//						sb.append(pi.p_name);
//						sb.append("|");
//						sb.append(pi.p_des);
//						sb.append("|");
//						sb.append(pi.x_id);
//						dout.writeUTF(sb.toString());
//						Blob blob = DBUtil.getPhotoBlob(pi.p_id);		//���ͼƬBlob
//						byte [] buf = blob.getBytes(1l, (int)blob.length());		//����ֽ�����
//						dout.writeInt(buf.length);				//��֪�ͻ������鳤��
//						dout.write(buf);
//						dout.flush();
//					}						
//				}
				else if(msg.startsWith("<#NEW_ALBUM#>")){				//��ϢΪ���������
					msg = msg.substring(13);				//��ȡ����
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					int result = DBUtil.createAlbum(sa[0], sa[1]);		//���������
					if(result == 1){		//�����ɹ�
						dout.writeUTF("<#NEW_ALBUM_SUCCESS#>");		//���سɹ���Ϣ
					}
					else{
						dout.writeUTF("<#NEW_ALBUM_FAIL#>");			//���ش���ʧ����Ϣ
					}
				}
				else if(msg.startsWith("<#NEW_PHOTO#>")){			//��ϢΪ�ϴ���Ƭ
					msg = msg.substring(13);			//��ȡ����
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					int size = din.readInt();			//��ȡͼƬ��С
					byte [] buf = new byte[size];		//�����ֽ�����
					for(int i=0;i<size;i++){
						buf[i] = din.readByte();
					}
					int result = DBUtil.insertPhotoFromAndroid(buf, sa[0], sa[1], sa[2]);	//����ͼƬ
					if(result == 1){
						dout.writeUTF("<#NEW_PHOTO_SUCCESS#>");		//�����ϴ��ɹ���Ϣ
					}
					else{
						dout.writeUTF("<#NEW_PHOTO_FAIL#>");		//�����ϴ�ʧ�ܵ���Ϣ
					}
				}
				else if(msg.startsWith("<#GET_COMMENT#>")){			//��ϢΪ��ȡָ��Լ��������б�
					msg = msg.substring(15);						//��ȡ����
					ArrayList<Comments> cmtList = DBUtil.getComments(msg);	//��������б�
					int size = cmtList.size();			//���۵ĸ���
					dout.writeInt(size);				//�������۵ĸ���
					String reply = din.readUTF();		//�ȴ��ͻ��˷���
					if(reply.equals("<#READY_TO_READ_COMMENT#>")){	//����ͻ����Ѿ�׼����
						for(Comments c:cmtList){
							StringBuilder sb = new StringBuilder();
							sb.append(c.cmtNo);
							sb.append("|");
							sb.append(c.cmtName);
							sb.append("|");
							sb.append(c.content);
							sb.append("|");
							sb.append(c.date);
							dout.writeUTF(sb.toString());		//������Ϣ���ͻ���
						}
					}
				}
				else if(msg.startsWith("<#NEW_COMMENT#>")){		//��ϢΪ�������
					msg = msg.substring(15);
					String [] sa = msg.split("\\|");
					int result = DBUtil.addComment(sa[0], sa[1], sa[2]);
					if(result == 1){
						dout.writeUTF("<#NEW_COMMENT_SUCESS#>");
					}
					else{
						dout.writeUTF("<#NEW_COMMENT_FAIL#>");
					}
				}
				else if(msg.startsWith("<#SEARCH_CONTACT#>")){					//��ϢΪ������ϵ��
					msg = msg.substring(18);			//��ȡ����
					ArrayList<User> result = DBUtil.searchFriendByName(msg);
					int size = result.size();
					dout.writeInt(size);				//��֪�ͻ������������
					if(size >0){			//������������Ϊ��
							for(int i=0;i<size;i++){
								StringBuffer sb = new StringBuffer();
								User u = result.get(i);
								sb.append(u.u_no);
								sb.append("|");
								sb.append(u.u_name);
								sb.append("|");
								sb.append(u.u_email);
								sb.append("|");
								sb.append(u.u_state);
								sb.append("|");
								sb.append(u.h_id);
								dout.writeUTF(sb.toString());			//����΢���û���Ϣ
								Blob blob = DBUtil.getHeadBlob(u.h_id);		//���ͷ��Blob
								byte [] buf = blob.getBytes(1l, (int)blob.length());	//�ֽ�����
								dout.writeInt(buf.length);			//�����ֽ����鳤��
								dout.write(buf);
							}
//						}
					}
				}
				else if(msg.startsWith("<#CHANGE_ALBUM_ACCESS#>")){		//��ϢΪ�޸����Ȩ��
					msg = msg.substring(23);
					String [] sa = msg.split("\\|");		//�ָ��ַ���
					int result = DBUtil.changeAlbumAccess(sa[0], sa[1]);	//�޸�Ȩ��
					if(result == 1){		//�޸ĳɹ�
						dout.writeUTF("<#ALBUM_ACCESS_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#ALBUM_ACCESS_FAIL#>");
					}
				}
				else if(msg.startsWith("<#DELETE_DIARY#>")){		//��ϢΪɾ��Լ��
					msg = msg.substring(16);		//��ȡ����
					int result = DBUtil.deleteDiary(msg);
					if(result == 1){		//ɾ���ɹ�
						dout.writeUTF("<#DELETE_DIARY_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#DELETE_DIARY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#MODIFY_DIARY#>")){		//��ϢΪ�޸�Լ��
					msg = msg.substring(16);		//��ȡ����
					String [] sa = msg.split("\\|");	//�ָ��ַ���
					int result = DBUtil.modifyDiary(sa[0], sa[1], sa[2]);
					if(result == 1){		//�޸ĳɹ�
						dout.writeUTF("<#MODIFY_DIARY_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#MODIFY_DIARY_FAIL#>");
					}
				}
				else if(msg.startsWith("<#DELETE_PHOTO#>")){			//��ϢΪɾ����Ƭ
					msg = msg.substring(16);
					int result = DBUtil.deletePhoto(msg);
					if(result == 1){
						dout.writeUTF("<#DELETE_PHOTO_SUCCESS#>");
					}
					else{
						dout.writeUTF("<#DELETE_PHOTO_FAIL#>");
					}
				}
				else if(msg.startsWith("<#GET_ALBUM_LIST_BY_ACCESS#>")){	//��ϢΪ����Ȩ�޻������б�
					msg = msg.substring(28);	//��ȡ����
					String [] sa = msg.split("\\|");
					ArrayList<String []> albumList = DBUtil.getAlbumListByAccess(sa[0], sa[1]);
					if(albumList.size() == 0){
						dout.writeUTF("<#NO_ALBUM#>");
					}
					else{
						StringBuilder sb = new StringBuilder();
						for(String [] albumInfo:albumList){
							sb.append(albumInfo[0]);
							sb.append("|");
							sb.append(albumInfo[1]);
							sb.append("$");
						}
						dout.writeUTF(sb.toString());						
					}
				}
			}
			catch(SocketException se){
				try {
					dout.close();
					din.close();
					socket.close();
					socket = null;
					flag = false;
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
			catch(EOFException eof){
				try {
					dout.close();
					din.close();
					socket.close();
					socket = null;
					flag = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
