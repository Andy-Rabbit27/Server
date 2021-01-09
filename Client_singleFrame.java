package ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Andy
 *
 * 单人聊天
 */
//单人聊天
public class Client_singleFrame extends JFrame implements ActionListener, KeyListener{


	private static final long serialVersionUID = 1L;
	private static JTextArea jta_disMess;	//聊天记录文本域
	private JTextField jtf_inputMess;		//发送栏文本域
	private JButton jbt_trans;				//发送按钮
	
	public int userThreadID = 0;			//用户线程ID
	
	private Client client;

	//构造方法
	public Client_singleFrame(Client client, String title) {
		this.client = client;
		init(title);
	}

	private void init(String title) {
		try {
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

		WinCenter.center(this);			//设置窗体在中间位置
		setTitle(title);						//设置标题
		setSize(400, 400);		//设置窗体大小
		setResizable(false);					//不可更改大小
		setContentPane(createContentPanel());
		addWindowListener(new WindowAdapter() {			//加入窗口监听
			@Override
			public void windowClosing(WindowEvent e) {	//关闭单人聊天
				closeSingleFrame();
			}
		});
	}

	private Container createContentPanel() {	//创建新的容器
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("聊天记录"));
		jp.setLayout(new BorderLayout());

		Font f = new Font("Serief",Font.BOLD,12);

		jta_disMess = new JTextArea();		//聊天记录文本域
		jta_disMess.setFont(f);
		jta_disMess.setEditable(false);		//不可编辑

		jp.add(BorderLayout.CENTER, new JScrollPane(jta_disMess));
		jp.add(BorderLayout.SOUTH, createInput());
		return jp;
	}

	//创建发送栏组件
	private Component createInput() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("发送栏"));
		jp.setLayout(new BorderLayout());	//启用BorderLayout布局管理器

		jtf_inputMess = new JTextField();		//发送栏文本域
		jtf_inputMess.addKeyListener(this);	//加入键盘监听

		jbt_trans = new JButton("发  送");	//发送按钮
		jbt_trans.addActionListener(this);	//加入活动监听

		jp.add(jtf_inputMess, BorderLayout.CENTER);	//加入文本域并设置位置
		jp.add(jbt_trans, BorderLayout.EAST);		//加入按钮并设置位置
		return jp;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){	//判断是否按下回车键
			if(arg0.getSource() == jtf_inputMess){	//判断触发源是否为发送栏文本
				jbt_trans.doClick();				//点击发送
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {	//活动监听

		if(e.getSource() == jbt_trans){				//判断触发源是否为发送按钮
			String str = jtf_inputMess.getText();	//获取发送栏文本
			str.trim();								//将信息前后的空格消除
			jtf_inputMess.setText("");				//将发送栏清空
			if(str.equals("")){						//如果发送栏为空则弹出提示消息
				JOptionPane.showMessageDialog(this, "发送消息为空！");
			}else{
				//日期加发送者用户名(指定发送格式)
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
				String date = form.format(new Date());
				String mess = client.username + "  " + date + "\n" + str;

				jta_disMess.append(mess + "\n");
				jta_disMess.setCaretPosition(jta_disMess.getText().length());

				int index = client.username_online.indexOf(this.getTitle());
				String info = client.username + "single" + client.getThreadID() + "single" +
								(int)client.clientuserid.get(index) + "single" +
								mess + "single";
				try {
					client.dos.writeUTF(info);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void setDisMess(String chat_re) {
		jta_disMess.append(chat_re + "\n");
		jta_disMess.setCaretPosition(jta_disMess.getText().length());
	}

	public void closeSingleFrame(){		//关闭单人聊天
		client.c_singleFrames.remove(this.getTitle());
		setVisible(false);				//单人聊天窗体关闭
	}

	public void setExitNotify() {
		jta_disMess.append(this.getTitle() + "");
		jbt_trans.setEnabled(false);
	}
}
