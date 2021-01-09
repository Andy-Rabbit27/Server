package ChatClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Andy
 *
 * 多人聊天室
 */
//用户聊天模块
//实现监听接口
public class Client_chatFrame extends JFrame implements ActionListener,
		KeyListener, ListSelectionListener {

	//构造方法
	public Client_chatFrame(Client client, String title) {
		this.client = client;
		try {
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

		setTitle("聊天室" + "  " + title);
		setSize(450, 325);
		WinCenter.center(this);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				jbt_exit.doClick();
			}
		});

		getContentPane().setLayout(null);			//设置无布局管理器

		JScrollPane scrollPane = new JScrollPane();		//声明一个滚动条对象
		scrollPane.setBorder(BorderFactory.createTitledBorder("聊天记录"));
		scrollPane.setBounds(10, 10, 283, 167);
		scrollPane.setWheelScrollingEnabled(true);
		getContentPane().add(scrollPane);

		jta_disMess = new JTextArea();				//实例化多行文本域
		Font f = new Font("Serief",Font.BOLD,12);
		jta_disMess.setFont(f);
		jta_disMess.setEditable(false);				//聊天信息框设置为不可编辑
		scrollPane.setViewportView(jta_disMess);

		jtf_inputMess = new JTextField();			//实例化单行文本域
		jtf_inputMess.addKeyListener(this);		//加入键盘监听
		jtf_inputMess.setBounds(10, 242, 192, 32);	//设置文本域位置
		getContentPane().add(jtf_inputMess);		//将组件加入
		jtf_inputMess.setColumns(10);				//设置文本大小

		jbt_trans = new JButton("发  送");		//实例化发送按钮
		jbt_trans.setFont(new Font("Serief", Font.PLAIN, 14));	//设置按钮字体
		jbt_trans.setBounds(212, 241, 93, 32);			//设置按钮位置
		jbt_trans.addActionListener(this);		//加入活动监听
		getContentPane().add(jbt_trans);			//将组件加入

		jbt_clear = new JButton("清除消息记录");	//实例化清除消息按钮
		jbt_clear.setFont(new Font("Serief", Font.PLAIN, 14));	//设置按钮字体
		jbt_clear.setBounds(158, 187, 135, 37);			//设置按钮位置
		jbt_clear.addActionListener(this);			//加入活动监听
		getContentPane().add(jbt_clear);				//将组件加入

		jbt_exit = new JButton("退出聊天室");	//实例化退出聊天室按钮
		jbt_exit.setFont(new Font("Serief", Font.PLAIN, 14));	//设置按钮字体
		jbt_exit.setBounds(20, 189, 128, 37);			//设置按钮位置
		jbt_exit.addActionListener(this);			//加入活动监听
		getContentPane().add(jbt_exit);				//将组件加入

		scrollPane_1 = new JScrollPane();			//实例化滚动条
		scrollPane_1.setBorder(BorderFactory.createTitledBorder("在线用户"));
		scrollPane_1.setBounds(303, 10, 128, 214);
		getContentPane().add(scrollPane_1);			//将组件加入

		jlt_disUsers = new JList();
		jlt_disUsers.setVisibleRowCount(4);
		jlt_disUsers.setSelectedIndex(0);
		jlt_disUsers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlt_disUsers.addListSelectionListener(this);
		scrollPane_1.setViewportView(jlt_disUsers);

		jbt_singlChat = new JButton("单人聊天");					//单人聊天按钮
		jbt_singlChat.setFont(new Font("Serief", Font.PLAIN, 14));
		jbt_singlChat.setBounds(315, 241, 116, 32);	//设置按钮位置
		jbt_singlChat.addActionListener(this);						//加入活动监听
		getContentPane().add(jbt_singlChat);							//将组件加入
	}




	private static final long serialVersionUID = 1L;
	private JTextField jtf_inputMess;	//单行文本域
	private JTextArea jta_disMess;		//多行文本域
	private JButton jbt_trans;			//发送按钮
	private JButton jbt_clear;			//清除按钮
	private JButton jbt_exit;			//退出按钮
	private JList jlt_disUsers;			//在线用户列表
	private JButton jbt_singlChat;		//单人聊天按钮
	private JScrollPane scrollPane_1;	//滚动条
	private Client client;

	@Override
	public void actionPerformed(ActionEvent e) {	//覆写
		if (e.getSource() == jbt_clear) {			//判断触发源是否为清除按钮
			jta_disMess.setText("");				//将聊天记录清空
		}
		if (e.getSource() == jbt_trans) {			//判断触发源是否为发送按钮
			String mess = jtf_inputMess.getText();	//获取发送信息框中的文本
			mess.trim();							//去掉信息前后的空格
			jtf_inputMess.setText("");				//将发送信息框清空

			if (mess.equals("")) {					//如果发送信息为空
				JOptionPane.showMessageDialog(this, "发送不可为空");	//弹出提示消息
				jtf_inputMess.setText("");
			} else {
				client.transMess(mess);				//发送消息框不为空则将消息发出
			}
		}
		if (e.getSource() == jbt_exit) {			//判断触发源是否是退出按钮

			//弹出提示消息选择是否退出聊天室
			if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
					"是否退出聊天室", "消息", JOptionPane.OK_CANCEL_OPTION)) {
				this.setVisible(false);
				client.exitChat();
				System.exit(0);
			}
		}
		if (e.getSource() == jbt_singlChat) {		//判断触发源是否为单人聊天按钮
			String user_names = (String) jlt_disUsers.getSelectedValue();
			if (user_names == null) {				//如果未选择聊天对象则弹出提示消息
				JOptionPane.showMessageDialog(this, "您未选择聊天对象\n请选择进行聊天的对象");
			} else {
				if (!client.c_singleFrames.containsKey(user_names)) {
					createSingleChatFrame(user_names);
				} else {
					client.c_singleFrames.get(user_names).setFocusableWindowState(true);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {			//覆写
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {	//判断按下的键是否为回车
			if (arg0.getSource() == jtf_inputMess) {
				jbt_trans.doClick();					//点击发送按钮
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void setDisMess(String substring) {
		int local = substring.indexOf("chat");
		jta_disMess.append(substring.substring(0, local) + "\n");
		jta_disMess.setCaretPosition(jta_disMess.getText().length());
	}

	public void setDisUsers(String chat_re) {
		String[] infos = chat_re.split("userlist");
		String[] info = new String[infos.length / 2];
		for (int i = 1; i < infos.length; i++) {
			int id_user = 0;
			try {
				id_user = Integer.parseInt(infos[i]);
				if (client.getThreadID() == id_user) {
					if (!client.username.equals(infos[i - 1])) {
						JOptionPane.showMessageDialog(this,
								"5");
						client.username = infos[i - 1];
						this.setTitle("6" + client.username);
						break;
					} else {
						break;
					}
				} else {
					i++;
				}
			} catch (Exception e) {
			}
		}
		if (infos.length == 2) {
			String[] s = new String[] {};
			if (!client.c_singleFrames.isEmpty()) {
				ListModel list = jlt_disUsers.getModel();
				for (int i = 0; i < list.getSize(); i++) {
					if (client.c_singleFrames.get(list.getElementAt(i)) != null) {
						client.c_singleFrames.get(list.getElementAt(i))
								.setExitNotify();
					}
				}
			}
			jlt_disUsers.removeAll();
			jlt_disUsers.setListData(s);
		} else {
			if ((infos.length / 2 - 1) < client.username_online.size()) {
				List<String> rec = new ArrayList<String>();
				int i = 0;
				for (; i < infos.length; i++) {
					rec.add(0, infos[i++]);
				}
				for (i = 0; i < client.username_online.size(); i++) {
					if (!rec.contains(client.username_online.get(i))) {
						break;
					}
				}
				String name = client.username_online.get(i);
				client.username_online.remove(i);
				try {
					client.clientuserid.remove(i);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (client.c_singleFrames.containsKey(name)) {
					client.c_singleFrames.get(name).closeSingleFrame();
					client.c_singleFrames.remove(name);
				}
			} else {
				List<Integer> online = new ArrayList<Integer>();
				for (int i = 0; i < client.username_online.size(); i++) {
					online.add(0, client.clientuserid.get(i));
				}
				if (online.isEmpty()) {
					for (int i = 1; i < infos.length; i++) {
						if ((int) Integer.parseInt(infos[i]) != client
								.getThreadID()) {
							client.username_online.add(0, infos[i - 1]);
							client.clientuserid.add(0,
									Integer.parseInt(infos[i]));
						}
						i++;
					}
				} else {
					for (int i = 1; i < infos.length; i++) {
						if (Integer.parseInt(infos[i]) != client.getThreadID()) {
							if (!online.contains(Integer.parseInt(infos[i]))) {
								client.username_online.add(0, infos[i - 1]);
								client.clientuserid.add(0,
										Integer.parseInt(infos[i]));
							} else {
								String name = client.username_online
										.get(client.clientuserid
												.indexOf(Integer
														.parseInt(infos[i])));
								if (!name.equals(infos[i - 1])) {
									if (client.c_singleFrames.containsKey(name)) {
										ChatClient.Client_singleFrame cf = client.c_singleFrames
												.get(name);
										cf.setTitle(name);
										client.c_singleFrames.remove(name);
										client.c_singleFrames.put(name, cf);
										cf.setVisible(false);

									}
									client.username_online.remove(name);
									client.clientuserid.remove(new Integer(
											Integer.parseInt(infos[i])));
									client.username_online.add(0, infos[i - 1]);
									client.clientuserid.add(0,
											Integer.parseInt(infos[i]));
								}
							}
						}
						i++;
					}
				}

			}
			try {
				for (int i = 0; i < client.username_online.size(); i++) {
					info[i] = client.username_online.get(i);
				}

			} catch (Exception e) {
			}
			jlt_disUsers.removeAll();
			jlt_disUsers.setListData(info);
		}
	}

	public void closeClient() {
		JOptionPane.showMessageDialog(this, "是否关闭聊天", "消息",
				JOptionPane.OK_OPTION);
		client.exitClient();
		setVisible(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == jlt_disUsers) {
		}
	}

	//创建单人聊天
	public void createSingleChatFrame(String name) {
		ChatClient.Client_singleFrame c_singlFrame = new ChatClient.Client_singleFrame(client, name);
		//向map集合中加入新的元素
		client.c_singleFrames.put(name, c_singlFrame);
		try {
			c_singlFrame.userThreadID = client.clientuserid.get(client.username_online.indexOf(name));

		} catch (Exception e) {
		}
		c_singlFrame.setVisible(true);
	}

	//设置单人聊天
	public void setSingleFrame(String chat_re) {
		String[] infos = chat_re.split("single");
		try {
			if (client.c_singleFrames.containsKey(infos[0])) {
				client.c_singleFrames.get(infos[0]).setDisMess(infos[3]);
			} else {
				createSingleChatFrame(infos[0]);
				client.c_singleFrames.get(infos[0]).setDisMess(infos[3]);
			}
		} catch (Exception e) {
		}
	}
}
