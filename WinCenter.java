package ChatClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author Andy
 *
 * 窗口位置设置
 */
public class WinCenter {
	public static void center(Window win){
		//确定窗口界面位置
		Toolkit tkit = Toolkit.getDefaultToolkit();
		Dimension sSize = tkit.getScreenSize();
		Dimension wSize = win.getSize();
		if(wSize.height > sSize.height){
			wSize.height = sSize.height;
		}
		if(wSize.width > sSize.width){
			wSize.width = sSize.width;
		}
		win.setLocation((sSize.width - wSize.width)/ 2, (sSize.height - wSize.height)/ 2);
	}
}
