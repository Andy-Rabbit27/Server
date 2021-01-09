package ChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Andy
 *
 * 服务器窗体
 */
//监听
public class ServerFrame extends JFrame implements ActionListener {
	//定义按钮、文本框、集合
	private JButton jbt_start;			//定义启动服务器按钮
	private JButton jbt_stop;			//定义停止服务器按钮
	private JButton jbt_exit;			//定义退出服务器按钮

	private JTextArea jta_disMess;		//定义一个多行文本域
	private JList jlt_disUsers;			//列表框
	private Server server;

	//定义用户名和用户id的列表集合
	public List<String> online_usernames;
	public List<Integer> online_usernameids;

	//构造方法
	public ServerFrame(Server server) {
		this.server = server;
		online_usernames = new ArrayList<String>();		//实例化用户名集合
		online_usernameids = new ArrayList<Integer>();	//实例化用户id集合
		try {
			//将当前的外观设置为实现本机系统外观的 LookAndFeel类
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("服务器");						//设置窗体标题
		setSize(449, 301);		//设置窗体大小
		setResizable(false);					//设置为不可更改大小
		ChatServer.WinCenter.center(this);	//将窗体设置到中间

		addWindowListener(new WindowAdapter() {		//加入窗口监听
			@Override
			public void windowClosing(WindowEvent arg0) {	//关闭窗口
				jbt_exit.doClick();
			}
		});

		getContentPane().setLayout(null);							//设置无布局管理器
		
		jbt_start = new JButton("启动服务器");
		jbt_start.setBounds(32, 23, 103, 34);	//设置启动服务器按钮位置
		jbt_start.addActionListener(this);						//设置活动监听
		getContentPane().add(jbt_start);
		
		jbt_stop = new JButton("停止服务器");
		jbt_stop.setBounds(145, 23, 103, 34);	//设置停止服务器按钮位置
		jbt_stop.setEnabled(false);									//初始化为不可编辑
		jbt_stop.addActionListener(this);							//设置活动监听
		getContentPane().add(jbt_stop);
		
		jbt_exit = new JButton("退出服务器");
		jbt_exit.setBounds(258, 23, 103, 34);	//设置退出服务器按钮位置
		jbt_exit.addActionListener(this);							//设置活动监听
		getContentPane().add(jbt_exit);
		
		JScrollPane scrollPane = new JScrollPane();					//定义滚动条
		scrollPane.setBounds(10, 64, 221, 192);	//设置滚动条信息
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setBorder(BorderFactory.createTitledBorder("聊天信息"));
		getContentPane().add(scrollPane);							//加入组件
		
		jta_disMess = new JTextArea();								//实现多行文本域
		Font f = new Font("Serief",Font.BOLD,12);
		jta_disMess.setFont(f);
		scrollPane.setViewportView(jta_disMess);
		
		JScrollPane scrollPane_1 = new JScrollPane();				//定义滚动条
		scrollPane_1.setBounds(258, 65, 157, 191);//设置滚动条信息
		scrollPane_1.setBorder(BorderFactory.createTitledBorder("在线用户"));//设置标题
		getContentPane().add(scrollPane_1);							//加入组件
		
		jlt_disUsers = new JList();									//创建列表框
		jlt_disUsers.setVisibleRowCount(4);							//设置列表框可见列数
		scrollPane_1.setViewportView(jlt_disUsers);					//加入滚动条
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {	//覆写ActionListener接口方法
		if(arg0.getSource() == jbt_start){			//判断触发源是否为开启服务器按钮
			jbt_start.setEnabled(false);			//将开启服务器按钮设置为不可编辑
			jbt_stop.setEnabled(true);				//将停止服务器按钮设置为可编辑
			server.startServer();					//开启服务器
		}
		if(arg0.getSource() == jbt_stop){										//判断触发源是否为停止服务器按钮
			int flag = JOptionPane.showConfirmDialog(this,//JOptionPane弹出一个标准选项框
					"是否停止服务器？", "",
            		JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);//选择是否停止服务器

            if(flag == JOptionPane.OK_OPTION){		//判断选择是否为是
            	server.stopServer();				//停止服务器
            	jbt_start.setEnabled(true);			//将开启服务器设置为可编辑
            	jbt_stop.setEnabled(false);			//将停止服务器设置为不可编辑
            }
		}

		if(arg0.getSource() == jbt_exit){			//判断触发源是否为退出服务器
			if(jbt_stop.isEnabled()){				//判断停止服务器按钮是否为可编辑
				jbt_stop.doClick();
			}
			server.close();							//关闭服务器
		}
	}

	public void setDisUsers(String userNames) {
		if(userNames.equals("userlist")){
			jlt_disUsers.removeAll();
			String[] user_null = new String[]{};
			jlt_disUsers.setListData(user_null);
		}else{
			if(userNames.contains("userlist")){
				String[] dis = userNames.split("userlist");
				String [] disUsernames = new String[dis.length / 2];
				int j = 0;
				for(int i = 0; i < dis.length; i++){
					disUsernames[j++] = dis[i++];
				}
				jlt_disUsers.removeAll();
				jlt_disUsers.setListData(disUsernames);
			}
			if(userNames.contains("exit")){
				String[] dis = {};
				jlt_disUsers.setListData(dis);
			}
		}
	}

	public void setDisMess(String message) {
		if(message.contains("chat")){
			int local = message.indexOf("chat");
			jta_disMess.append(message.substring(0, local) + "\n");
			jta_disMess.setCaretPosition(jta_disMess.getText().length());
		}
		if(message.contains("exit")){
			jta_disMess.setText("");
		}
	}

	//同时开启两个服务器的情况
	public void setStartAndStopUnable() {
		//弹出消息框报错
		JOptionPane.showMessageDialog(this, "不能同时开启两个服务器");
		jbt_start.setEnabled(false);
		jbt_stop.setEnabled(false);
	}
}
