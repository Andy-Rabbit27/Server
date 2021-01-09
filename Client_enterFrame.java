package ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;


/**
 *
 * @author Andy
 *
 * 用户登录
 */
//登录界面
public class Client_enterFrame extends JFrame implements ActionListener, KeyListener{

	//构造方法
	public Client_enterFrame(Client client) {
		this.client = client;
		try {
			//设置外观
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		setTitle("聊天室");					//设置窗体标题
		getContentPane().setLayout(null);	//设置无布局管理器
		setSize(296, 249);	//设置窗体大小
		WinCenter.center(this);		//设置窗体位置
		setResizable(false);				//设置为不可改变大小
		addWindowListener(new WindowAdapter() {			//使用窗口适配器
			@Override
			public void windowClosing(WindowEvent e) {	//只覆写窗口关闭
				jbt_exit.doClick();
			}
		});
		
		JLabel lblNewLabel = new JLabel("用户名");						//用户名标签
		lblNewLabel.setFont(new Font("Serief", Font.PLAIN, 14));//设置标签字体
		lblNewLabel.setBounds(23, 30, 81, 34);			//设置标签位置
		getContentPane().add(lblNewLabel);									//将组件加入
		
		jtf_username = new JTextField();									//多行文本域
		jtf_username.addKeyListener(this);								//加入键盘监听
		jtf_username.setBounds(114, 30, 143, 34);		//设置文本域位置
		getContentPane().add(jtf_username);									//将组件加入
		jtf_username.setColumns(10);										//设置文本空间大小
		
		JLabel lblNewLabel_1 = new JLabel("服务器地址");					//服务器地址标签
		lblNewLabel_1.setFont(new Font("Serief", Font.PLAIN, 14));	//设置标签字体
		lblNewLabel_1.setBounds(23, 74, 81, 34);			//设置标签位置
		getContentPane().add(lblNewLabel_1);									//将组件加入
		
		jtf_hostIp = new JTextField();								//单行文本框（服务器地址）
		jtf_hostIp.setBounds(114, 74, 143, 34);	//设置文本域位置
		jtf_hostIp.addKeyListener(this);							//加入键盘监听
		getContentPane().add(jtf_hostIp);							//将组件加入

		try {
			String ip = (String)Inet4Address.getLocalHost().getHostAddress();//获取本机IP地址
			jtf_hostIp.setText(ip);				//将服务器地址设置为本机IP地址
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		jtf_hostIp.setColumns(10);				//设置文本域内容大小
		
		JLabel lblNewLabel_2 = new JLabel("端口号");						//端口号标签
		lblNewLabel_2.setFont(new Font("Serief", Font.PLAIN, 14));	//设置标签字体
		lblNewLabel_2.setBounds(23, 118, 81, 34);			//设置标签位置
		getContentPane().add(lblNewLabel_2);									//将组件加入
		
		jtf_hostPort = new JTextField();		//单行文本域（端口号）
		jtf_hostPort.addKeyListener(this);	//加入键盘监听
		jtf_hostPort.setBounds(114, 118, 143, 34);
		getContentPane().add(jtf_hostPort);		//将组件加入
		jtf_hostPort.setText("5000");			//默认端口号为5000
		jtf_hostPort.setColumns(10);			//设置文本内容大小
		
		jbt_enter = new JButton("进入聊天室");							//进入聊天室按钮
		jbt_enter.addActionListener(this);								//加入活动监听
		jbt_enter.addKeyListener(this);									//加入键盘监听
		jbt_enter.setFont(new Font("Serief", Font.PLAIN, 14));	//设置按钮字体
		jbt_enter.setBounds(23, 162, 108, 39);			//设置按钮位置
		getContentPane().add(jbt_enter);									//将组件加入
		
		jbt_exit = new JButton("退出聊天室");							//退出聊天室按钮
		jbt_exit.setFont(new Font("Serief", Font.PLAIN, 14));	//设置按钮字体
		jbt_exit.setBounds(144, 162, 113, 39);			//设置按钮位置
		jbt_exit.addActionListener(this);									//加入活动监听
		getContentPane().add(jbt_exit);										//将组件加入
	}




	private static final long serialVersionUID = 1L;
	private JTextField jtf_username;	//用户名单行文本域
	private JTextField jtf_hostIp;		//服务器地址文本域
	private JTextField jtf_hostPort;	//端口号文本域
	private JButton jbt_enter;			//进入聊天室按钮
	private JButton jbt_exit;			//退出聊天室按钮
	private Client client;				//


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbt_exit){					//判断触发源是否为退出按钮
			setVisible(false);							//登录窗体消失
			client.exitLogin();							//退出登录
		}
		if(e.getSource() == jbt_enter){					//判断触发源是否为进入聊天室按钮
			String username = jtf_username.getText();	//获取用户名
			username.trim();							//去掉用户名前后的空格
			String hostIp = jtf_hostIp.getText();		//获取服务器地址
			hostIp.trim();
			String hostPort = jtf_hostPort.getText();	//获取端口号
			hostPort.trim();
			if(!username.equals("")){			//用户名不为空
				if(!hostIp.equals("")){			//服务器地址不为空
					if(!hostPort.equals("")){	//端口号不为空
						String login_mess = client.login(username, hostIp, hostPort);	//进行登录
						if(login_mess.equals("true")){
							this.setVisible(false);			//登录成功则登录窗口消失
							client.showChatFrame(username);	//进入聊天室
						}else{
							JOptionPane.showMessageDialog(this, login_mess);
						}
						//若有一项为空则会弹出提示消息
					}else{
						JOptionPane.showMessageDialog(this, "端口号不可为空！");
					}
				}else{
					JOptionPane.showMessageDialog(this, "服务器地址不可为空！");
				}
			}else{
				JOptionPane.showMessageDialog(this, "用户名不可为空！");
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){	//判断是否按下回车键
			jbt_enter.doClick();					//点击进入按钮
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
