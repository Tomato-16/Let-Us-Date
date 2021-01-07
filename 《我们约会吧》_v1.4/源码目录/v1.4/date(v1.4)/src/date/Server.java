package date;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.*;
import java.awt.*;

public class Server extends JFrame implements ActionListener
{

	  JPanel upPanel,downPanel,center;
	  JLabel portLabel,stateLabel,imageLabel;
	  JTextField port;               
	  JButton startButton,endButton;
	  ImageIcon icon;
	  
	  ServerSocket ss ;
	  ServerThread st ;
	  
	  public Server()
	  {
	    super("我们约会吧--服务器");    
	    
	    icon=new ImageIcon("a.jpg");
	    imageLabel=new JLabel(icon,JLabel.CENTER);
	    
	    portLabel = new JLabel("端口号：",JLabel.CENTER);
	    stateLabel = new JLabel("",JLabel.CENTER);
	    
	    port = new JTextField(10);
	    
	    startButton=new JButton("连接服务器");
	   
	    endButton=new JButton("断开连接");
	   
	    startButton.addActionListener(this);                 //addActionListener for button
	    endButton.addActionListener(this);
	  
	    center = new JPanel(new FlowLayout());
	    upPanel = new JPanel(new BorderLayout());
	    downPanel = new JPanel(new FlowLayout());
	  
	    center.add(portLabel);
	    center.add(port);
	    downPanel.add(startButton);
	    downPanel.add(endButton);
	    
	    upPanel.add(center,BorderLayout.NORTH);
	    upPanel.add(stateLabel,BorderLayout.SOUTH);
	    
	    getContentPane().add(imageLabel,BorderLayout.NORTH);
	    getContentPane().add(upPanel,BorderLayout.CENTER);                    //add components
	    getContentPane().add(downPanel,BorderLayout.SOUTH);
	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocation(450,300);
	    this.setSize(450,200);
	    this.setVisible(true);
	   
	    this.addWindowListener(new WindowAdapter()                 //add and handler WindowEvent
	          {
	          	public void windowClosing(WindowEvent e)
	          	{
	          		System.exit(0);
	          	}
	          } );     
	  }
	  
		@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
			 if(evt.getSource()==startButton)
		       {
		       	 if(port.getText().trim().equals(""))
		       	    {
		       	    	JOptionPane.showMessageDialog(null,"端口号不能为空");              //Makesurer User Name not Empty
		       	    	return;
		       	    }
		         else 
		         {
		     		try{
		    			ss = new ServerSocket(Integer.parseInt(port.getText().trim()));
		    			st = new ServerThread(ss);
		    			st.start();
		    			stateLabel.setText("服务器开启");
		    			System.out.println("Listening...");
		    		}catch(Exception e){
		    			e.printStackTrace();
		    		}
		    	}
		    }
		    if(evt.getSource() == endButton)
		    {
		    	try {
					if(st.isAlive())
					{
						st.stop();
						ss.close();	
						stateLabel.setText("服务器断开");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		  }
  
	  
	public static void main(String args[]){
		new Server();
	}


	
}