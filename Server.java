package ChatServer;


/**
 * @author Andy
 *
 * 服务器
 */
public class Server {
	
	private ChatServer.ServerFrame serverFrame;		//定义一个ServerFrame类的对象
	private ChatServer.ServerThread serverThread;	//定义一个ServerThread类的对象

	//getter和setter方法
	public ChatServer.ServerFrame getServerFrame() {
		return serverFrame;
	}
	public void setServerFrame(ChatServer.ServerFrame serverFrame) {
		this.serverFrame = serverFrame;
	}

	public Server(){}

	//启动服务器
	public void startServer() {
		try{
			//创建新的服务器线程
			serverThread = new ChatServer.ServerThread(serverFrame);
		}catch(Exception e){
			System.exit(0);
		}
		serverThread.setFlag_exit(true);	//将退出标识置为true
		serverThread.start();				//启动线程
	}

	//停止服务器
	public void stopServer(){

		synchronized (serverThread.messages) {			//同步监视器
			String str = "serverexit";
			serverThread.messages.add(str);
		}
		serverThread.serverFrame.setDisMess("exit");	//设置信息标签
		serverThread.serverFrame.setDisUsers("exit");

		serverThread.stopServer();						//停止服务器
	}

	

	public static void main(String[] args) {
		Server server = new Server();
		ChatServer.ServerFrame serverFrame = new ChatServer.ServerFrame(server);
		server.setServerFrame(serverFrame);
		serverFrame.setVisible(true);
	}

	//关闭服务器
	public void close() {
		if(serverThread != null){			//判断是否无线程进行中
			if(serverThread.isAlive()){		//判断服务器线程是否存在
				serverThread.stopServer();	//关闭服务器之前先停止服务器
			}
		}
		System.exit(0);
	}
	
}
