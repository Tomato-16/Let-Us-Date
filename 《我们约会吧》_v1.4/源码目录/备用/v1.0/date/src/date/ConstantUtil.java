package date;

public class ConstantUtil {
	public static final String FEIEND = "friend_max";	//���ڻ�ȡfriend��ǰ�������
	public static final String DIARY = "diary_max";		//���ڻ�ȡdiary��ǰ�������
	public static final String ALBUM = "album_max";		//���ڻ�ȡalbum��ǰ�������
	public static final String PHOTO = "photo_max";		//���ڻ�ȡphoto��������
	public static final String COMMENT = "comment_max";	//���ڻ�ȡcomment��ǰ�������
	public static final String VISIT = "comment_max";	//���ڻ�ȡvisit��ǰ�������
	public static final String USER = "user_max";		//���ڻ�ȡuser��ǰ�������
	public static final String HEAD = "head_max";		//���ڻ�ȡuser��ǰ�������
	public static final String P_COMMENT = "p_comment_max";		//���ڻ�ȡuser��ǰ�������
	public static final String DATE = "date_max";		//���ڻ�ȡdate��ǰ�������
	public static final String CHAT = "chat_max";		//���ڻ�ȡchat��ǰ�������
	
	
	public static final String CHAR_ENCODING = "GBK";	//�ַ��������ʽ
	public static final String CONNECTION_OUT = "�������ݿ�������Ժ����ԡ�";
	public static final String LOGIN_FAIL = "�û�����������Ч�����������롣";
	public static final String REGISTER_FAIL = "ע��ʧ�ܣ������ԡ�"; 
	public static final String DIARY_FAIL = "Լ�����ʧ�ܣ������ԡ�";
	public static final String DIARY_SUCCESS = "Լ����³ɹ���";
	public static final String DELETE_FAIL = "ɾ��ʧ�ܣ������ԡ�";
	public static final String DELETE_SUCCESS = "ɾ���ɹ���";
	public static final String UPDATE_STATE_SUCCESS = "���³ɹ���";
	public static final String UPDATE_STATE_FAIL = "����ʧ�ܣ�";
	public static final String CREATE_ALBUM_SUCESS = "��ᴴ���ɹ���";
	public static final String CREATE_ALBUM_FAIL = "��ᴴ��ʧ�ܣ�";
	public static final String UPLOAD_SIZE_EXCEED = "�ļ���С�������ƣ�������ѡ�񡣣�3M���ڣ�";
	public static final String UPLOAD_SUCCESS = "�ļ��ϴ��ɹ���";
	public static final String UPLOAD_FAIL = "�ļ��ϴ�ʧ�ܡ�";
	public static final String PWD_MODIFY_SUCCESS = "�޸�����ɹ���";
	public static final String PWD_MODIFY_FAIL = "�޸�����ʧ�ܣ�";
	public static final String INFO_MODIFY_SUCCESS = "�޸�����ɹ���";
	public static final String INFO_MODIFY_FAIL = "�޸�����ʧ�ܣ�";
	
	public static final String PubDate_FAIL = "Լ�����ʧ�ܣ������ԡ�";
	public static final String PubDate_SUCCESS = "Լ����³ɹ���";
	public static final String CancelDate_FAIL = "Լ��ȡ��ʧ�ܣ������ԡ�";
	public static final String CancelDate_SUCCESS = "Լ��ȡ���ɹ���";
	
	public static final String UPDATE_GPS_SUCCESS = "��γ�ȸ��³ɹ���";
	public static final String UPDATE_GPS_FAIL = "��γ�ȸ���ʧ�ܣ�";
	public static final String WRITECHAT_SUCCESS = "�Ի����³ɹ���";
	public static final String WRITECHAT_FAIL = "�Ի�����ʧ�ܣ�";
	
	
	public static final int DIARY_ABBR = 8;//��ʾ��Լ��������������ʡ��
	
	public static void pp(byte [] buf){
		System.out.println("================================");
		int start = buf.length-100;
		for(int i=start;i<buf.length;i++){
			System.out.print(buf[i]);
		}
		System.out.println("\n================================");
	}
}
