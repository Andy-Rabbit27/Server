package ChatServer;

//导入工具类
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author Andy
 *
 * 窗体位置设置
 */
//将界面设置在屏幕中心
public class WinCenter {
	public static void center(Window win){
		//通过工具包的方法将硬盘上的图片拿到内存中来
		Toolkit tkit = Toolkit.getDefaultToolkit();
		//获取屏幕宽高信息
		Dimension sSize = tkit.getScreenSize();
		//获取尺寸
		Dimension wSize = win.getSize();
		//确定窗口界面的位置
		if(wSize.height > sSize.height){
			wSize.height = sSize.height;
		}
		if(wSize.width > sSize.width){
			wSize.width = sSize.width;
		}
		win.setLocation((sSize.width - wSize.width)/ 2, (sSize.height - wSize.height)/ 2);
	}
}
