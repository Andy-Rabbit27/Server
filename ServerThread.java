package ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author Andy
 *
 * 服务器线程实现类
 */
//服务器线程的实现类
public class ServerThread extends Thread {

	public ServerSocket serverSocket;		//服务器Socket对象
	public Vector<String> messages;			//信息 用Vector集合存储
	public Vector<ClientThread> clients;	//用户线程 用Vector集合存储
	public Map<Integer, String> users;		//Map集合存储用户信息
	public BroadCast broadcast;				//广播对象


	public int Port = 5000;					//端口号为5000
	public boolean login = true;
	public ServerFrame serverFrame;
	private boolean flag_exit = false;


	//服务器线程
	public ServerThread(ServerFrame serverFrame){
		this.serverFrame = serverFrame;

		//创建集合存储信息
		messages = new Vector<String>();
		clients = new Vector<ClientThread>();
		users = new HashMap<Integer, String>();

		try {
			//创建新的服务器对象
			serverSocket = new ServerSocket(Port);
		} catch (IOException e) {
			this.serverFrame.setStartAndStopUnable();
			System.exit(0);
		}

		//广播线程
		broadcast = new BroadCast(this);
		broadcast.setFlag_exit(true);	//将退出标识置为true
		broadcast.start();				//开启线程
	}
	
	@Override
	public void run() {
		Socket socket;


		while(flag_exit){
				try {
					if(serverSocket.isClosed()){	//判断服务器端是否处于开启状态
						flag_exit = false;			//如果关闭则将退出标识置为false
					}else{
						try{
							//监听客户端，接收客户端消息
							socket = serverSocket.accept();
						}catch(SocketException e){
							socket = null;
							flag_exit = false;
						}


						//当客户端socket不为null时，启动客户端线程
						if(socket != null){
							ClientThread clientThread = new ClientThread(socket, this);
							clientThread.setFlag_exit(true);
							clientThread.start();

							//加入线程
							synchronized (clients) {
								clients.addElement(clientThread);
							}
							synchronized (messages) {
								users.put((int) clientThread.getId(), "login");

								//将long类型转化为String类型
								messages.add(clientThread.getId() + "clientThread");
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


	//停止服务器
	public void stopServer() {
		try {
			if(this.isAlive()){
				serverSocket.close();
				setFlag_exit(false);
			}
		} catch (Throwable e) {}
	}


	//设置标记退出
	public void setFlag_exit(boolean b) {
		flag_exit = b;
	}
}
