package ChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Andy
 *
 * 客户端线程实现类
 */
//客户端线程
//客户端线程的实现类
public class ClientThread extends Thread {

	public Socket clientSocket;					//服务器客户端Socket对象
	public ChatServer.ServerThread serverThread;//服务端线程对象
	public DataInputStream dis;					//数据输入流对象
	public DataOutputStream dos;				//数据输出流对象
	public String client_userID;				//客户端用户ID
	private boolean flag_exit = false;			//退出标记

	//客户端Socket
	public ClientThread(Socket socket, ChatServer.ServerThread serverThread){
		clientSocket = socket;
		this.serverThread = serverThread;
		try {
			//封装数据输入输出流
			dis = new DataInputStream(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//线程运行模块
	@Override
	public void run() {
		//当退出标识为true
		while(flag_exit){
			try {
				//读入信息
				String Message = dis.readUTF();
				//判断读入信息是否包含login字段标识
				if(Message.contains("login")){

					//将login标识删去并进行接下来的操作
					String [] userInfo = Message.split("login");
					int userID = Integer.parseInt(userInfo[1]);
					//删去userID
					serverThread.users.remove(userID);

					if(serverThread.users.containsValue(userInfo[0])){

						for(int i = 0; i < serverThread.clients.size(); i++){
							int id = (int)serverThread.clients.get(i).getId();

							if(serverThread.users.get(id).equals(userInfo[0])){
								serverThread.users.remove(id);
								serverThread.users.put(id, userInfo[0] + "_" + id);
								break;
							}
						}
						serverThread.users.put(Integer.parseInt(userInfo[1]), userInfo[0] + "_" + userInfo[1]);
					}else{
						serverThread.users.put(userID, userInfo[0]);
					}

					Message = null;

					//创建容器 存储线程id和userlist
					StringBuffer sb = new StringBuffer();
					synchronized (serverThread.clients) {
						for(int i = 0; i < serverThread.clients.size(); i++){
							int threadID = (int) serverThread.clients.elementAt(i).getId();
							sb.append((String)serverThread.users.get(new Integer(threadID)) + "userlist");
							sb.append(threadID + "userlist");
						}
					}
					String userNames = new String(sb);
					serverThread.serverFrame.setDisUsers(userNames);
					Message = userNames;
				}else{
					if(Message.contains("exit")){							//判断是否存在exit标签
						String [] userInfo = Message.split("exit");	//消除exit标签
						int userID = Integer.parseInt(userInfo[1]);
						serverThread.users.remove(userID);
						Message = null;
						StringBuffer sb = new StringBuffer();
						synchronized (serverThread.clients) {
							for(int i = 0; i < serverThread.clients.size(); i++){
								int threadID = (int) serverThread.clients.elementAt(i).getId();
								if(userID == threadID){
									serverThread.clients.removeElementAt(i);
									i--;
								}else{
									sb.append((String)serverThread.users.get(new Integer(threadID)) + "userlist");
									sb.append(threadID + "userlist");
								}
							}
						}
						String userNames = new String(sb);
						if(userNames.equals("")){
							serverThread.serverFrame.setDisUsers("userlist");
						}else{
							serverThread.serverFrame.setDisUsers(userNames);
						}
						Message = userNames;
					}else{
						//聊天显示姓名和发送时间的实现
						if(Message.contains("chat")){
							String[] chat = Message.split("chat");
							StringBuffer sb = new StringBuffer();
							SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
							String date = form.format(new Date());
							sb.append(chat[0] + "  " + date + "\n");
							sb.append(chat[2] + "chat");
							String str = new String(sb);
							Message = str;
							serverThread.serverFrame.setDisMess(Message);
						}else{
							if(Message.contains("single")){
								
							}
						}
					}
				}
				synchronized (serverThread.messages) {
					if(Message != null){
						serverThread.messages.addElement(Message);
					}
				}
				if(Message.contains("exit")){
					this.clientSocket.close();
					flag_exit = false;
				}
			} catch (IOException e) {}
		}
	}


	//关闭客户端线程，并提示服务器的客户端Socket为空
	public void closeClienthread(ClientThread clientThread) {
		if(clientThread.clientSocket != null){
			try {
				clientThread.clientSocket.close();
			} catch (IOException e) {
				System.out.println("server's clientSocket is null");
			}
		}
		
		try {
			setFlag_exit(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	//标识退出
	public void setFlag_exit(boolean b) {
		flag_exit = b;
	}
}
